/*
 * Copyright 2007 ZXing authors
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

package org.apache.cordova.plugins.barcodescanner.google.zxing.qrcode.decoder;

import org.apache.cordova.plugins.barcodescanner.google.zxing.FormatException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.BitMatrix;

/**
 * @author Sean Owen
 */
final class BitMatrixParser {

  private final BitMatrix bitMatrix;
  private Version parsedVersion;
  private FormatInformation parsedFormatInfo;

  /**
   * @param bitMatrix {@link BitMatrix} to parse
   * @throws FormatException if dimension is not >= 21 and 1 mod 4
   */
  BitMatrixParser(BitMatrix bitMatrix) throws FormatException {
    int dimension = bitMatrix.getHeight();
    if (dimension < 21 || (dimension & 0x03) != 1) {
      throw FormatException.getFormatInstance();
    }
    this.bitMatrix = bitMatrix;
  }

  /**
   * <p>Reads format information from one of its two locations within the QR Code.</p>
   *
   * @return {@link FormatInformation} encapsulating the QR Code's format info
   * @throws FormatException if both format information locations cannot be parsed as
   * the valid encoding of format information
   */
  FormatInformation readFormatInformation() throws FormatException {

    if (parsedFormatInfo != null) {
      return parsedFormatInfo;
    }

    // Read top-left format info bits
    int formatInfoBits1 = 0;
    for (int i = 0; i < 6; i++) {
      formatInfoBits1 = copyBit(i, 8, formatInfoBits1);
    }
    // .. and skip a bit in the timing pattern ...
    formatInfoBits1 = copyBit(7, 8, formatInfoBits1);
    formatInfoBits1 = copyBit(8, 8, formatInfoBits1);
    formatInfoBits1 = copyBit(8, 7, formatInfoBits1);
    // .. and skip a bit in the timing pattern ...
    for (int j = 5; j >= 0; j--) {
      formatInfoBits1 = copyBit(8, j, formatInfoBits1);
    }

    // Read the top-right/bottom-left pattern too
    int dimension = bitMatrix.getHeight();
    int formatInfoBits2 = 0;
    int jMin = dimension - 7;
    for (int j = dimension - 1; j >= jMin; j--) {
      formatInfoBits2 = copyBit(8, j, formatInfoBits2);
    }
    for (int i = dimension - 8; i < dimension; i++) {
      formatInfoBits2 = copyBit(i, 8, formatInfoBits2);
    }

    parsedFormatInfo = FormatInformation.decodeFormatInformation(formatInfoBits1, formatInfoBits2);
    if (parsedFormatInfo != null) {
      return parsedFormatInfo;
    }
    throw FormatException.getFormatInstance();
  }

  /**
   * <p>Reads version information from one of its two locations within the QR Code.</p>
   *
   * @return {@link Version} encapsulating the QR Code's version
   * @throws FormatException if both version information locations cannot be parsed as
   * the valid encoding of version information
   */
  Version readVersion() throws FormatException {

    if (parsedVersion != null) {
      return parsedVersion;
    }

    int dimension = bitMatrix.getHeight();

    int provisionalVersion = (dimension - 17) >> 2;
    if (provisionalVersion <= 6) {
      return Version.getVersionForNumber(provisionalVersion);
    }

    // Read top-right version info: 3 wide by 6 tall
    int versionBits = 0;
    int ijMin = dimension - 11;
    for (int j = 5; j >= 0; j--) {
      for (int i = dimension - 9; i >= ijMin; i--) {
        versionBits = copyBit(i, j, versionBits);
      }
    }

    parsedVersion = Version.decodeVersionInformation(versionBits);
    if (parsedVersion != null && parsedVersion.getDimensionForVersion() == dimension) {
      return parsedVersion;
    }

    // Hmm, failed. Try bottom left: 6 wide by 3 tall
    versionBits = 0;
    for (int i = 5; i >= 0; i--) {
      for (int j = dimension - 9; j >= ijMin; j--) {
        versionBits = copyBit(i, j, versionBits);
      }
    }

    parsedVersion = Version.decodeVersionInformation(versionBits);
    if (parsedVersion != null && parsedVersion.getDimensionForVersion() == dimension) {
      return parsedVersion;
    }
    throw FormatException.getFormatInstance();
  }

  private int copyBit(int i, int j, int versionBits) {
    return bitMatrix.get(i, j) ? (versionBits << 1) | 0x1 : versionBits << 1;
  }

  /**
   * <p>Reads the bits in the {@link BitMatrix} representing the finder pattern in the
   * correct order in order to reconstitute the codewords bytes contained within the
   * QR Code.</p>
   *
   * @return bytes encoded within the QR Code
   * @throws FormatException if the exact number of bytes expected is not read
   */
  byte[] readCodewords() throws FormatException {

    FormatInformation formatInfo = readFormatInformation();
    Version version = readVersion();

    // Get the data mask for the format used in this QR Code. This will exclude
    // some bits from reading as we wind through the bit matrix.
    DataMask dataMask = DataMask.forReference((int) formatInfo.getDataMask());
    int dimension = bitMatrix.getHeight();
    dataMask.unmaskBitMatrix(bitMatrix, dimension);

    BitMatrix functionPattern = version.buildFunctionPattern();

    boolean readingUp = true;
    byte[] result = new byte[version.getTotalCodewords()];
    int resultOffset = 0;
    int currentByte = 0;
    int bitsRead = 0;
    // Read columns in pairs, from right to left
    for (int j = dimension - 1; j > 0; j -= 2) {
      if (j == 6) {
        // Skip whole column with vertical alignment pattern;
        // saves time and makes the other code proceed more cleanly
        j--;
      }
      // Read alternatingly from bottom to top then top to bottom
      for (int count = 0; count < dimension; count++) {
        int i = readingUp ? dimension - 1 - count : count;
        for (int col = 0; col < 2; col++) {
          // Ignore bits covered by the function pattern
          if (!functionPattern.get(j - col, i)) {
            // Read a bit
            bitsRead++;
            currentByte <<= 1;
            if (bitMatrix.get(j - col, i)) {
              currentByte |= 1;
            }
            // If we've made a whole byte, save it off
            if (bitsRead == 8) {
              result[resultOffset++] = (byte) currentByte;
              bitsRead = 0;
              currentByte = 0;
            }
          }
        }
      }
      readingUp ^= true; // readingUp = !readingUp; // switch directions
    }
    if (resultOffset != version.getTotalCodewords()) {
      throw FormatException.getFormatInstance();
    }
    return result;
  }

}