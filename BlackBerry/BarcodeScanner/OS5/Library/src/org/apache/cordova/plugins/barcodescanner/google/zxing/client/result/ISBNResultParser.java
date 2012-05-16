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

package org.apache.cordova.plugins.barcodescanner.google.zxing.client.result;

import org.apache.cordova.plugins.barcodescanner.google.zxing.BarcodeFormat;
import org.apache.cordova.plugins.barcodescanner.google.zxing.Result;

/**
 * Parses strings of digits that represent a ISBN.
 * 
 * @author jbreiden@google.com (Jeff Breidenbach)
 */
public class ISBNResultParser extends ResultParser {

  private ISBNResultParser() {
  }

  // ISBN-13 For Dummies 
  // http://www.bisg.org/isbn-13/for.dummies.html
  public static ISBNParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (!BarcodeFormat.EAN_13.equals(format)) {
      return null;
    }
    String rawText = result.getText();
    if (rawText == null) {
      return null;
    }
    int length = rawText.length();
    if (length != 13) {
      return null;
    }
    if (!rawText.startsWith("978") && !rawText.startsWith("979")) {
      return null;
    }
   
    return new ISBNParsedResult(rawText);
  }

}
