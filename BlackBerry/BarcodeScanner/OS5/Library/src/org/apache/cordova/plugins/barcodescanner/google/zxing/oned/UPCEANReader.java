/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cordova.plugins.barcodescanner.google.zxing.oned;


import java.util.Hashtable;

import org.apache.cordova.plugins.barcodescanner.google.zxing.BarcodeFormat;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ChecksumException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.DecodeHintType;
import org.apache.cordova.plugins.barcodescanner.google.zxing.FormatException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.NotFoundException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ReaderException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.Result;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultMetadataType;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultPoint;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultPointCallback;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.BitArray;

/**
 * <p>Encapsulates functionality and implementation that is common to UPC and EAN families
 * of one-dimensional barcodes.</p>
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * @author alasdair@google.com (Alasdair Mackintosh)
 */
public abstract class UPCEANReader extends OneDReader {

  // These two values are critical for determining how permissive the decoding will be.
  // We've arrived at these values through a lot of trial and error. Setting them any higher
  // lets false positives creep in quickly.
  private static final int MAX_AVG_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.42f);
  private static final int MAX_INDIVIDUAL_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.7f);

  /**
   * Start/end guard pattern.
   */
  static final int[] START_END_PATTERN = {1, 1, 1,};

  /**
   * Pattern marking the middle of a UPC/EAN pattern, separating the two halves.
   */
  static final int[] MIDDLE_PATTERN = {1, 1, 1, 1, 1};

  /**
   * "Odd", or "L" patterns used to encode UPC/EAN digits.
   */
  static final int[][] L_PATTERNS = {
      {3, 2, 1, 1}, // 0
      {2, 2, 2, 1}, // 1
      {2, 1, 2, 2}, // 2
      {1, 4, 1, 1}, // 3
      {1, 1, 3, 2}, // 4
      {1, 2, 3, 1}, // 5
      {1, 1, 1, 4}, // 6
      {1, 3, 1, 2}, // 7
      {1, 2, 1, 3}, // 8
      {3, 1, 1, 2}  // 9
  };

  /**
   * As above but also including the "even", or "G" patterns used to encode UPC/EAN digits.
   */
  static final int[][] L_AND_G_PATTERNS;

  static {
    L_AND_G_PATTERNS = new int[20][];
    for (int i = 0; i < 10; i++) {
      L_AND_G_PATTERNS[i] = L_PATTERNS[i];
    }
    for (int i = 10; i < 20; i++) {
      int[] widths = L_PATTERNS[i - 10];
      int[] reversedWidths = new int[widths.length];
      for (int j = 0; j < widths.length; j++) {
        reversedWidths[j] = widths[widths.length - j - 1];
      }
      L_AND_G_PATTERNS[i] = reversedWidths;
    }
  }

  private final StringBuffer decodeRowStringBuffer;
  private final UPCEANExtensionSupport extensionReader;
  private final EANManufacturerOrgSupport eanManSupport;

  protected UPCEANReader() {
    decodeRowStringBuffer = new StringBuffer(20);
    extensionReader = new UPCEANExtensionSupport();
    eanManSupport = new EANManufacturerOrgSupport();
  }

  static int[] findStartGuardPattern(BitArray row) throws NotFoundException {
    boolean foundStart = false;
    int[] startRange = null;
    int nextStart = 0;
    while (!foundStart) {
      startRange = findGuardPattern(row, nextStart, false, START_END_PATTERN);
      int start = startRange[0];
      nextStart = startRange[1];
      // Make sure there is a quiet zone at least as big as the start pattern before the barcode.
      // If this check would run off the left edge of the image, do not accept this barcode,
      // as it is very likely to be a false positive.
      int quietStart = start - (nextStart - start);
      if (quietStart >= 0) {
        foundStart = row.isRange(quietStart, start, false);
      }
    }
    return startRange;
  }

  public Result decodeRow(int rowNumber, BitArray row, Hashtable hints)
      throws NotFoundException, ChecksumException, FormatException {
    return decodeRow(rowNumber, row, findStartGuardPattern(row), hints);
  }

  /**
   * <p>Like {@link #decodeRow(int, BitArray, java.util.Hashtable)}, but
   * allows caller to inform method about where the UPC/EAN start pattern is
   * found. This allows this to be computed once and reused across many implementations.</p>
   */
  public Result decodeRow(int rowNumber, BitArray row, int[] startGuardRange, Hashtable hints)
      throws NotFoundException, ChecksumException, FormatException {

    ResultPointCallback resultPointCallback = hints == null ? null :
        (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);

    if (resultPointCallback != null) {
      resultPointCallback.foundPossibleResultPoint(new ResultPoint(
          (startGuardRange[0] + startGuardRange[1]) / 2.0f, rowNumber
      ));
    }

    StringBuffer result = decodeRowStringBuffer;
    result.setLength(0);
    int endStart = decodeMiddle(row, startGuardRange, result);

    if (resultPointCallback != null) {
      resultPointCallback.foundPossibleResultPoint(new ResultPoint(
          endStart, rowNumber
      ));
    }

    int[] endRange = decodeEnd(row, endStart);

    if (resultPointCallback != null) {
      resultPointCallback.foundPossibleResultPoint(new ResultPoint(
          (endRange[0] + endRange[1]) / 2.0f, rowNumber
      ));
    }


    // Make sure there is a quiet zone at least as big as the end pattern after the barcode. The
    // spec might want more whitespace, but in practice this is the maximum we can count on.
    int end = endRange[1];
    int quietEnd = end + (end - endRange[0]);
    if (quietEnd >= row.getSize() || !row.isRange(end, quietEnd, false)) {
      throw NotFoundException.getNotFoundInstance();
    }

    String resultString = result.toString();
    if (!checkChecksum(resultString)) {
      throw ChecksumException.getChecksumInstance();
    }

    float left = (float) (startGuardRange[1] + startGuardRange[0]) / 2.0f;
    float right = (float) (endRange[1] + endRange[0]) / 2.0f;
    BarcodeFormat format = getBarcodeFormat();
    Result decodeResult = new Result(resultString,
        null, // no natural byte representation for these barcodes
        new ResultPoint[]{
            new ResultPoint(left, (float) rowNumber),
            new ResultPoint(right, (float) rowNumber)},
        format);

    try {
      Result extensionResult = extensionReader.decodeRow(rowNumber, row, endRange[1]);
      decodeResult.putAllMetadata(extensionResult.getResultMetadata());
      decodeResult.addResultPoints(extensionResult.getResultPoints());
    } catch (ReaderException re) {
      // continue
    }

    if (BarcodeFormat.EAN_13.equals(format) || BarcodeFormat.UPC_A.equals(format)) {
      String countryID = eanManSupport.lookupCountryIdentifier(resultString);
      if (countryID != null) {
        decodeResult.putMetadata(ResultMetadataType.POSSIBLE_COUNTRY, countryID);
      }
    }

    return decodeResult;
  }

  /**
   * @return {@link #checkStandardUPCEANChecksum(String)}
   */
  boolean checkChecksum(String s) throws ChecksumException, FormatException {
    return checkStandardUPCEANChecksum(s);
  }

  /**
   * Computes the UPC/EAN checksum on a string of digits, and reports
   * whether the checksum is correct or not.
   *
   * @param s string of digits to check
   * @return true iff string of digits passes the UPC/EAN checksum algorithm
   * @throws FormatException if the string does not contain only digits
   */
  private static boolean checkStandardUPCEANChecksum(String s) throws FormatException {
    int length = s.length();
    if (length == 0) {
      return false;
    }

    int sum = 0;
    for (int i = length - 2; i >= 0; i -= 2) {
      int digit = (int) s.charAt(i) - (int) '0';
      if (digit < 0 || digit > 9) {
        throw FormatException.getFormatInstance();
      }
      sum += digit;
    }
    sum *= 3;
    for (int i = length - 1; i >= 0; i -= 2) {
      int digit = (int) s.charAt(i) - (int) '0';
      if (digit < 0 || digit > 9) {
        throw FormatException.getFormatInstance();
      }
      sum += digit;
    }
    return sum % 10 == 0;
  }

  int[] decodeEnd(BitArray row, int endStart) throws NotFoundException {
    return findGuardPattern(row, endStart, false, START_END_PATTERN);
  }

  /**
   * @param row row of black/white values to search
   * @param rowOffset position to start search
   * @param whiteFirst if true, indicates that the pattern specifies white/black/white/...
   * pixel counts, otherwise, it is interpreted as black/white/black/...
   * @param pattern pattern of counts of number of black and white pixels that are being
   * searched for as a pattern
   * @return start/end horizontal offset of guard pattern, as an array of two ints
   * @throws NotFoundException if pattern is not found
   */
  static int[] findGuardPattern(BitArray row, int rowOffset, boolean whiteFirst, int[] pattern)
      throws NotFoundException {
    int patternLength = pattern.length;
    int[] counters = new int[patternLength];
    int width = row.getSize();
    boolean isWhite = false;
    while (rowOffset < width) {
      isWhite = !row.get(rowOffset);
      if (whiteFirst == isWhite) {
        break;
      }
      rowOffset++;
    }

    int counterPosition = 0;
    int patternStart = rowOffset;
    for (int x = rowOffset; x < width; x++) {
      boolean pixel = row.get(x);
      if (pixel ^ isWhite) {
        counters[counterPosition]++;
      } else {
        if (counterPosition == patternLength - 1) {
          if (patternMatchVariance(counters, pattern, MAX_INDIVIDUAL_VARIANCE) < MAX_AVG_VARIANCE) {
            return new int[]{patternStart, x};
          }
          patternStart += counters[0] + counters[1];
          for (int y = 2; y < patternLength; y++) {
            counters[y - 2] = counters[y];
          }
          counters[patternLength - 2] = 0;
          counters[patternLength - 1] = 0;
          counterPosition--;
        } else {
          counterPosition++;
        }
        counters[counterPosition] = 1;
        isWhite = !isWhite;
      }
    }
    throw NotFoundException.getNotFoundInstance();
  }

  /**
   * Attempts to decode a single UPC/EAN-encoded digit.
   *
   * @param row row of black/white values to decode
   * @param counters the counts of runs of observed black/white/black/... values
   * @param rowOffset horizontal offset to start decoding from
   * @param patterns the set of patterns to use to decode -- sometimes different encodings
   * for the digits 0-9 are used, and this indicates the encodings for 0 to 9 that should
   * be used
   * @return horizontal offset of first pixel beyond the decoded digit
   * @throws NotFoundException if digit cannot be decoded
   */
  static int decodeDigit(BitArray row, int[] counters, int rowOffset, int[][] patterns)
      throws NotFoundException {
    recordPattern(row, rowOffset, counters);
    int bestVariance = MAX_AVG_VARIANCE; // worst variance we'll accept
    int bestMatch = -1;
    int max = patterns.length;
    for (int i = 0; i < max; i++) {
      int[] pattern = patterns[i];
      int variance = patternMatchVariance(counters, pattern, MAX_INDIVIDUAL_VARIANCE);
      if (variance < bestVariance) {
        bestVariance = variance;
        bestMatch = i;
      }
    }
    if (bestMatch >= 0) {
      return bestMatch;
    } else {
      throw NotFoundException.getNotFoundInstance();
    }
  }

  /**
   * Get the format of this decoder.
   *
   * @return The 1D format.
   */
  abstract BarcodeFormat getBarcodeFormat();

  /**
   * Subclasses override this to decode the portion of a barcode between the start
   * and end guard patterns.
   *
   * @param row row of black/white values to search
   * @param startRange start/end offset of start guard pattern
   * @param resultString {@link StringBuffer} to append decoded chars to
   * @return horizontal offset of first pixel after the "middle" that was decoded
   * @throws NotFoundException if decoding could not complete successfully
   */
  protected abstract int decodeMiddle(BitArray row, int[] startRange, StringBuffer resultString)
      throws NotFoundException;

}
