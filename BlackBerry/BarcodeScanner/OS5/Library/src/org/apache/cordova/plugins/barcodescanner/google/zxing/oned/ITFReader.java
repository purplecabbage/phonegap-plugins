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
import org.apache.cordova.plugins.barcodescanner.google.zxing.DecodeHintType;
import org.apache.cordova.plugins.barcodescanner.google.zxing.FormatException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.NotFoundException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.Result;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultPoint;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.BitArray;

/**
 * <p>Implements decoding of the ITF format, or Interleaved Two of Five.</p>
 *
 * <p>This Reader will scan ITF barcodes of certain lengths only.
 * At the moment it reads length 6, 10, 12, 14, 16, 24, and 44 as these have appeared "in the wild". Not all
 * lengths are scanned, especially shorter ones, to avoid false positives. This in turn is due to a lack of
 * required checksum function.</p>
 *
 * <p>The checksum is optional and is not applied by this Reader. The consumer of the decoded
 * value will have to apply a checksum if required.</p>
 *
 * <p><a href="http://en.wikipedia.org/wiki/Interleaved_2_of_5">http://en.wikipedia.org/wiki/Interleaved_2_of_5</a>
 * is a great reference for Interleaved 2 of 5 information.</p>
 *
 * @author kevin.osullivan@sita.aero, SITA Lab.
 */
public final class ITFReader extends OneDReader {

  private static final int MAX_AVG_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.42f);
  private static final int MAX_INDIVIDUAL_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.8f);

  private static final int W = 3; // Pixel width of a wide line
  private static final int N = 1; // Pixed width of a narrow line

  private static final int[] DEFAULT_ALLOWED_LENGTHS = { 6, 8, 10, 12, 14, 16, 20, 24, 44 };

  // Stores the actual narrow line width of the image being decoded.
  private int narrowLineWidth = -1;

  /**
   * Start/end guard pattern.
   *
   * Note: The end pattern is reversed because the row is reversed before
   * searching for the END_PATTERN
   */
  private static final int[] START_PATTERN = {N, N, N, N};
  private static final int[] END_PATTERN_REVERSED = {N, N, W};

  /**
   * Patterns of Wide / Narrow lines to indicate each digit
   */
  static final int[][] PATTERNS = {
      {N, N, W, W, N}, // 0
      {W, N, N, N, W}, // 1
      {N, W, N, N, W}, // 2
      {W, W, N, N, N}, // 3
      {N, N, W, N, W}, // 4
      {W, N, W, N, N}, // 5
      {N, W, W, N, N}, // 6
      {N, N, N, W, W}, // 7
      {W, N, N, W, N}, // 8
      {N, W, N, W, N}  // 9
  };

  public Result decodeRow(int rowNumber, BitArray row, Hashtable hints) throws FormatException, NotFoundException {

    // Find out where the Middle section (payload) starts & ends
    int[] startRange = decodeStart(row);
    int[] endRange = decodeEnd(row);

    StringBuffer result = new StringBuffer(20);
    decodeMiddle(row, startRange[1], endRange[0], result);
    String resultString = result.toString();

    int[] allowedLengths = null;
    if (hints != null) {
      allowedLengths = (int[]) hints.get(DecodeHintType.ALLOWED_LENGTHS);

    }
    if (allowedLengths == null) {
      allowedLengths = DEFAULT_ALLOWED_LENGTHS;
    }

    // To avoid false positives with 2D barcodes (and other patterns), make
    // an assumption that the decoded string must be 6, 10 or 14 digits.
    int length = resultString.length();
    boolean lengthOK = false;
    for (int i = 0; i < allowedLengths.length; i++) {
      if (length == allowedLengths[i]) {
        lengthOK = true;
        break;
      }

    }
    if (!lengthOK) {
      throw FormatException.getFormatInstance();
    }

    return new Result(
        resultString,
        null, // no natural byte representation for these barcodes
        new ResultPoint[] { new ResultPoint(startRange[1], (float) rowNumber),
                            new ResultPoint(endRange[0], (float) rowNumber)},
        BarcodeFormat.ITF);
  }

  /**
   * @param row          row of black/white values to search
   * @param payloadStart offset of start pattern
   * @param resultString {@link StringBuffer} to append decoded chars to
   * @throws NotFoundException if decoding could not complete successfully
   */
  private static void decodeMiddle(BitArray row, int payloadStart, int payloadEnd,
      StringBuffer resultString) throws NotFoundException {

    // Digits are interleaved in pairs - 5 black lines for one digit, and the
    // 5
    // interleaved white lines for the second digit.
    // Therefore, need to scan 10 lines and then
    // split these into two arrays
    int[] counterDigitPair = new int[10];
    int[] counterBlack = new int[5];
    int[] counterWhite = new int[5];

    while (payloadStart < payloadEnd) {

      // Get 10 runs of black/white.
      recordPattern(row, payloadStart, counterDigitPair);
      // Split them into each array
      for (int k = 0; k < 5; k++) {
        int twoK = k << 1;
        counterBlack[k] = counterDigitPair[twoK];
        counterWhite[k] = counterDigitPair[twoK + 1];
      }

      int bestMatch = decodeDigit(counterBlack);
      resultString.append((char) ('0' + bestMatch));
      bestMatch = decodeDigit(counterWhite);
      resultString.append((char) ('0' + bestMatch));

      for (int i = 0; i < counterDigitPair.length; i++) {
        payloadStart += counterDigitPair[i];
      }
    }
  }

  /**
   * Identify where the start of the middle / payload section starts.
   *
   * @param row row of black/white values to search
   * @return Array, containing index of start of 'start block' and end of
   *         'start block'
   * @throws NotFoundException
   */
  int[] decodeStart(BitArray row) throws NotFoundException {
    int endStart = skipWhiteSpace(row);
    int[] startPattern = findGuardPattern(row, endStart, START_PATTERN);

    // Determine the width of a narrow line in pixels. We can do this by
    // getting the width of the start pattern and dividing by 4 because its
    // made up of 4 narrow lines.
    this.narrowLineWidth = (startPattern[1] - startPattern[0]) >> 2;

    validateQuietZone(row, startPattern[0]);

    return startPattern;
  }

  /**
   * The start & end patterns must be pre/post fixed by a quiet zone. This
   * zone must be at least 10 times the width of a narrow line.  Scan back until
   * we either get to the start of the barcode or match the necessary number of
   * quiet zone pixels.
   *
   * Note: Its assumed the row is reversed when using this method to find
   * quiet zone after the end pattern.
   *
   * ref: http://www.barcode-1.net/i25code.html
   *
   * @param row bit array representing the scanned barcode.
   * @param startPattern index into row of the start or end pattern.
   * @throws NotFoundException if the quiet zone cannot be found, a ReaderException is thrown.
   */
  private void validateQuietZone(BitArray row, int startPattern) throws NotFoundException {

    int quietCount = this.narrowLineWidth * 10;  // expect to find this many pixels of quiet zone

    for (int i = startPattern - 1; quietCount > 0 && i >= 0; i--) {
      if (row.get(i)) {
        break;
      }
      quietCount--;
    }
    if (quietCount != 0) {
      // Unable to find the necessary number of quiet zone pixels.
      throw NotFoundException.getNotFoundInstance();
    }
  }

  /**
   * Skip all whitespace until we get to the first black line.
   *
   * @param row row of black/white values to search
   * @return index of the first black line.
   * @throws NotFoundException Throws exception if no black lines are found in the row
   */
  private static int skipWhiteSpace(BitArray row) throws NotFoundException {
    int width = row.getSize();
    int endStart = 0;
    while (endStart < width) {
      if (row.get(endStart)) {
        break;
      }
      endStart++;
    }
    if (endStart == width) {
      throw NotFoundException.getNotFoundInstance();
    }

    return endStart;
  }

  /**
   * Identify where the end of the middle / payload section ends.
   *
   * @param row row of black/white values to search
   * @return Array, containing index of start of 'end block' and end of 'end
   *         block'
   * @throws NotFoundException
   */

  int[] decodeEnd(BitArray row) throws NotFoundException {

    // For convenience, reverse the row and then
    // search from 'the start' for the end block
    row.reverse();
    try {
      int endStart = skipWhiteSpace(row);
      int[] endPattern = findGuardPattern(row, endStart, END_PATTERN_REVERSED);

      // The start & end patterns must be pre/post fixed by a quiet zone. This
      // zone must be at least 10 times the width of a narrow line.
      // ref: http://www.barcode-1.net/i25code.html
      validateQuietZone(row, endPattern[0]);

      // Now recalculate the indices of where the 'endblock' starts & stops to
      // accommodate
      // the reversed nature of the search
      int temp = endPattern[0];
      endPattern[0] = row.getSize() - endPattern[1];
      endPattern[1] = row.getSize() - temp;

      return endPattern;
    } finally {
      // Put the row back the right way.
      row.reverse();
    }
  }

  /**
   * @param row       row of black/white values to search
   * @param rowOffset position to start search
   * @param pattern   pattern of counts of number of black and white pixels that are
   *                  being searched for as a pattern
   * @return start/end horizontal offset of guard pattern, as an array of two
   *         ints
   * @throws NotFoundException if pattern is not found
   */
  private static int[] findGuardPattern(BitArray row, int rowOffset, int[] pattern) throws NotFoundException {

    // TODO: This is very similar to implementation in UPCEANReader. Consider if they can be
    // merged to a single method.
    int patternLength = pattern.length;
    int[] counters = new int[patternLength];
    int width = row.getSize();
    boolean isWhite = false;

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
   * Attempts to decode a sequence of ITF black/white lines into single
   * digit.
   *
   * @param counters the counts of runs of observed black/white/black/... values
   * @return The decoded digit
   * @throws NotFoundException if digit cannot be decoded
   */
  private static int decodeDigit(int[] counters) throws NotFoundException {

    int bestVariance = MAX_AVG_VARIANCE; // worst variance we'll accept
    int bestMatch = -1;
    int max = PATTERNS.length;
    for (int i = 0; i < max; i++) {
      int[] pattern = PATTERNS[i];
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

}
