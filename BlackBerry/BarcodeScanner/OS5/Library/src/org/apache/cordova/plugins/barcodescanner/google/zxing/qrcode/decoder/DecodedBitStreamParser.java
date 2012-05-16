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


import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.cordova.plugins.barcodescanner.google.zxing.FormatException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.BitSource;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.CharacterSetECI;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.DecoderResult;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.StringUtils;

/**
 * <p>QR Codes can encode text as bits in one of several modes, and can use multiple modes
 * in one QR Code. This class decodes the bits back into text.</p>
 *
 * <p>See ISO 18004:2006, 6.4.3 - 6.4.7</p>
 *
 * @author Sean Owen
 */
final class DecodedBitStreamParser {

  /**
   * See ISO 18004:2006, 6.4.4 Table 5
   */
  private static final char[] ALPHANUMERIC_CHARS = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
      'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
      ' ', '$', '%', '*', '+', '-', '.', '/', ':'
  };
  private static final int GB2312_SUBSET = 1;

  private DecodedBitStreamParser() {
  }

  static DecoderResult decode(byte[] bytes, Version version, ErrorCorrectionLevel ecLevel, Hashtable hints)
      throws FormatException {
    BitSource bits = new BitSource(bytes);
    StringBuffer result = new StringBuffer(50);
    CharacterSetECI currentCharacterSetECI = null;
    boolean fc1InEffect = false;
    Vector byteSegments = new Vector(1);
    Mode mode;
    do {
      // While still another segment to read...
      if (bits.available() < 4) {
        // OK, assume we're done. Really, a TERMINATOR mode should have been recorded here
        mode = Mode.TERMINATOR;
      } else {
        try {
          mode = Mode.forBits(bits.readBits(4)); // mode is encoded by 4 bits
        } catch (IllegalArgumentException iae) {
          throw FormatException.getFormatInstance();
        }
      }
      if (!mode.equals(Mode.TERMINATOR)) {
        if (mode.equals(Mode.FNC1_FIRST_POSITION) || mode.equals(Mode.FNC1_SECOND_POSITION)) {
          // We do little with FNC1 except alter the parsed result a bit according to the spec
          fc1InEffect = true;
        } else if (mode.equals(Mode.STRUCTURED_APPEND)) {
          // not really supported; all we do is ignore it
          // Read next 8 bits (symbol sequence #) and 8 bits (parity data), then continue
          bits.readBits(16);
        } else if (mode.equals(Mode.ECI)) {
          // Count doesn't apply to ECI
          int value = parseECIValue(bits);
          currentCharacterSetECI = CharacterSetECI.getCharacterSetECIByValue(value);
          if (currentCharacterSetECI == null) {
            throw FormatException.getFormatInstance();
          }
        } else {
          // First handle Hanzi mode which does not start with character count
          if (mode.equals(Mode.HANZI)) {
        		//chinese mode contains a sub set indicator right after mode indicator
        		int subset = bits.readBits(4);
        		int countHanzi = bits.readBits(mode.getCharacterCountBits(version));
        		if (subset == GB2312_SUBSET) {
        			decodeHanziSegment(bits, result, countHanzi);
            }
          } else {
            // "Normal" QR code modes:
            // How many characters will follow, encoded in this mode?
            int count = bits.readBits(mode.getCharacterCountBits(version));
            if (mode.equals(Mode.NUMERIC)) {
              decodeNumericSegment(bits, result, count);
            } else if (mode.equals(Mode.ALPHANUMERIC)) {
              decodeAlphanumericSegment(bits, result, count, fc1InEffect);
            } else if (mode.equals(Mode.BYTE)) {
              decodeByteSegment(bits, result, count, currentCharacterSetECI, byteSegments, hints);
            } else if (mode.equals(Mode.KANJI)) {
              decodeKanjiSegment(bits, result, count);
            } else {
              throw FormatException.getFormatInstance();
            }
          }
        }
      }
    } while (!mode.equals(Mode.TERMINATOR));

    return new DecoderResult(bytes,
                             result.toString(),
                             byteSegments.isEmpty() ? null : byteSegments,
                             ecLevel == null ? null : ecLevel.toString());
  }

  /**
   * See specification GBT 18284-2000
   */
  private static void decodeHanziSegment(BitSource bits,
                                         StringBuffer result,
                                         int count) throws FormatException {
    // Don't crash trying to read more bits than we have available.
    if (count * 13 > bits.available()) {
      throw FormatException.getFormatInstance();
    }

    // Each character will require 2 bytes. Read the characters as 2-byte pairs
    // and decode as GB2312 afterwards
    byte[] buffer = new byte[2 * count];
    int offset = 0;
    while (count > 0) {
      // Each 13 bits encodes a 2-byte character
      int twoBytes = bits.readBits(13);
      int assembledTwoBytes = ((twoBytes / 0x060) << 8) | (twoBytes % 0x060);
      if (assembledTwoBytes < 0x003BF) {
        // In the 0xA1A1 to 0xAAFE range
        assembledTwoBytes += 0x0A1A1;
      } else {
        // In the 0xB0A1 to 0xFAFE range
        assembledTwoBytes += 0x0A6A1;
      }
      buffer[offset] = (byte) ((assembledTwoBytes >> 8) & 0xFF);
      buffer[offset + 1] = (byte) (assembledTwoBytes & 0xFF);
      offset += 2;
      count--;
    }

    try {
      result.append(new String(buffer, StringUtils.GB2312));
    } catch (UnsupportedEncodingException uee) {
      throw FormatException.getFormatInstance();
    }
  }

  private static void decodeKanjiSegment(BitSource bits,
                                         StringBuffer result,
                                         int count) throws FormatException {
    // Don't crash trying to read more bits than we have available.
    if (count * 13 > bits.available()) {
      throw FormatException.getFormatInstance();
    }

    // Each character will require 2 bytes. Read the characters as 2-byte pairs
    // and decode as Shift_JIS afterwards
    byte[] buffer = new byte[2 * count];
    int offset = 0;
    while (count > 0) {
      // Each 13 bits encodes a 2-byte character
      int twoBytes = bits.readBits(13);
      int assembledTwoBytes = ((twoBytes / 0x0C0) << 8) | (twoBytes % 0x0C0);
      if (assembledTwoBytes < 0x01F00) {
        // In the 0x8140 to 0x9FFC range
        assembledTwoBytes += 0x08140;
      } else {
        // In the 0xE040 to 0xEBBF range
        assembledTwoBytes += 0x0C140;
      }
      buffer[offset] = (byte) (assembledTwoBytes >> 8);
      buffer[offset + 1] = (byte) assembledTwoBytes;
      offset += 2;
      count--;
    }
    // Shift_JIS may not be supported in some environments:
    try {
      result.append(new String(buffer, StringUtils.SHIFT_JIS));
    } catch (UnsupportedEncodingException uee) {
      throw FormatException.getFormatInstance();
    }
  }

  private static void decodeByteSegment(BitSource bits,
                                        StringBuffer result,
                                        int count,
                                        CharacterSetECI currentCharacterSetECI,
                                        Vector byteSegments,
                                        Hashtable hints) throws FormatException {
    // Don't crash trying to read more bits than we have available.
    if (count << 3 > bits.available()) {
      throw FormatException.getFormatInstance();
    }

    byte[] readBytes = new byte[count];
    for (int i = 0; i < count; i++) {
      readBytes[i] = (byte) bits.readBits(8);
    }
    String encoding;
    if (currentCharacterSetECI == null) {
    // The spec isn't clear on this mode; see
    // section 6.4.5: t does not say which encoding to assuming
    // upon decoding. I have seen ISO-8859-1 used as well as
    // Shift_JIS -- without anything like an ECI designator to
    // give a hint.
      encoding = StringUtils.guessEncoding(readBytes, hints);
    } else {
      encoding = currentCharacterSetECI.getEncodingName();
    }
    try {
      result.append(new String(readBytes, encoding));
    } catch (UnsupportedEncodingException uce) {
      throw FormatException.getFormatInstance();
    }
    byteSegments.addElement(readBytes);
  }

  private static char toAlphaNumericChar(int value) throws FormatException {
    if (value >= ALPHANUMERIC_CHARS.length) {
      throw FormatException.getFormatInstance();
    }
    return ALPHANUMERIC_CHARS[value];
  }

  private static void decodeAlphanumericSegment(BitSource bits,
                                                StringBuffer result,
                                                int count,
                                                boolean fc1InEffect) throws FormatException {
    // Read two characters at a time
    int start = result.length();
    while (count > 1) {
      int nextTwoCharsBits = bits.readBits(11);
      result.append(toAlphaNumericChar(nextTwoCharsBits / 45));
      result.append(toAlphaNumericChar(nextTwoCharsBits % 45));
      count -= 2;
    }
    if (count == 1) {
      // special case: one character left
      result.append(toAlphaNumericChar(bits.readBits(6)));
    }
    // See section 6.4.8.1, 6.4.8.2
    if (fc1InEffect) {
      // We need to massage the result a bit if in an FNC1 mode:
      for (int i = start; i < result.length(); i++) {
        if (result.charAt(i) == '%') {
          if (i < result.length() - 1 && result.charAt(i + 1) == '%') {
            // %% is rendered as %
            result.deleteCharAt(i + 1);
          } else {
            // In alpha mode, % should be converted to FNC1 separator 0x1D
            result.setCharAt(i, (char) 0x1D);
          }
        }
      }
    }
  }

  private static void decodeNumericSegment(BitSource bits,
                                           StringBuffer result,
                                           int count) throws FormatException {
    // Read three digits at a time
    while (count >= 3) {
      // Each 10 bits encodes three digits
      int threeDigitsBits = bits.readBits(10);
      if (threeDigitsBits >= 1000) {
        throw FormatException.getFormatInstance();
      }
      result.append(toAlphaNumericChar(threeDigitsBits / 100));
      result.append(toAlphaNumericChar((threeDigitsBits / 10) % 10));
      result.append(toAlphaNumericChar(threeDigitsBits % 10));
      count -= 3;
    }
    if (count == 2) {
      // Two digits left over to read, encoded in 7 bits
      int twoDigitsBits = bits.readBits(7);
      if (twoDigitsBits >= 100) {
        throw FormatException.getFormatInstance();
      }
      result.append(toAlphaNumericChar(twoDigitsBits / 10));
      result.append(toAlphaNumericChar(twoDigitsBits % 10));
    } else if (count == 1) {
      // One digit left over to read
      int digitBits = bits.readBits(4);
      if (digitBits >= 10) {
        throw FormatException.getFormatInstance();
      }
      result.append(toAlphaNumericChar(digitBits));
    }
  }

  private static int parseECIValue(BitSource bits) {
    int firstByte = bits.readBits(8);
    if ((firstByte & 0x80) == 0) {
      // just one byte
      return firstByte & 0x7F;
    } else if ((firstByte & 0xC0) == 0x80) {
      // two bytes
      int secondByte = bits.readBits(8);
      return ((firstByte & 0x3F) << 8) | secondByte;
    } else if ((firstByte & 0xE0) == 0xC0) {
      // three bytes
      int secondThirdBytes = bits.readBits(16);
      return ((firstByte & 0x1F) << 16) | secondThirdBytes;
    }
    throw new IllegalArgumentException("Bad ECI bits starting with byte " + firstByte);
  }

}
