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

package org.apache.cordova.plugins.barcodescanner.google.zxing;

import java.util.Hashtable;

/**
 * Enumerates barcode formats known to this package. Please keep alphabetized.
 *
 * @author Sean Owen
 */
public final class BarcodeFormat {

  // No, we can't use an enum here. J2ME doesn't support it.

  private static final Hashtable VALUES = new Hashtable();

  /** Aztec 2D barcode format. */
  public static final BarcodeFormat AZTEC = new BarcodeFormat("AZTEC");

  /** CODABAR 1D format. */
  public static final BarcodeFormat CODABAR = new BarcodeFormat("CODABAR");

  /** Code 39 1D format. */
  public static final BarcodeFormat CODE_39 = new BarcodeFormat("CODE_39");

  /** Code 93 1D format. */
  public static final BarcodeFormat CODE_93 = new BarcodeFormat("CODE_93");

  /** Code 128 1D format. */
  public static final BarcodeFormat CODE_128 = new BarcodeFormat("CODE_128");

  /** Data Matrix 2D barcode format. */
  public static final BarcodeFormat DATA_MATRIX = new BarcodeFormat("DATA_MATRIX");

  /** EAN-8 1D format. */
  public static final BarcodeFormat EAN_8 = new BarcodeFormat("EAN_8");

  /** EAN-13 1D format. */
  public static final BarcodeFormat EAN_13 = new BarcodeFormat("EAN_13");

  /** ITF (Interleaved Two of Five) 1D format. */
  public static final BarcodeFormat ITF = new BarcodeFormat("ITF");

  /** PDF417 format. */
  public static final BarcodeFormat PDF_417 = new BarcodeFormat("PDF_417");

  /** QR Code 2D barcode format. */
  public static final BarcodeFormat QR_CODE = new BarcodeFormat("QR_CODE");

  /** RSS 14 */
  public static final BarcodeFormat RSS_14 = new BarcodeFormat("RSS_14");

  /** RSS EXPANDED */
  public static final BarcodeFormat RSS_EXPANDED = new BarcodeFormat("RSS_EXPANDED");

  /** UPC-A 1D format. */
  public static final BarcodeFormat UPC_A = new BarcodeFormat("UPC_A");

  /** UPC-E 1D format. */
  public static final BarcodeFormat UPC_E = new BarcodeFormat("UPC_E");

  /** UPC/EAN extension format. Not a stand-alone format. */
  public static final BarcodeFormat UPC_EAN_EXTENSION = new BarcodeFormat("UPC_EAN_EXTENSION");

  private final String name;

  private BarcodeFormat(String name) {
    this.name = name;
    VALUES.put(name, this);
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public static BarcodeFormat valueOf(String name) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException();
    }
    BarcodeFormat format = (BarcodeFormat) VALUES.get(name);
    if (format == null) {
      throw new IllegalArgumentException();
    }
    return format;
  }

}
