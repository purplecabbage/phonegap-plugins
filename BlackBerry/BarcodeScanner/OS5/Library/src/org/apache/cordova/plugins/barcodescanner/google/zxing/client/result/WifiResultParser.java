/*
 * Copyright 2010 ZXing authors
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

import org.apache.cordova.plugins.barcodescanner.google.zxing.Result;

/**
 * Parses a WIFI configuration string.  Strings will be of the form:
 * WIFI:T:WPA;S:mynetwork;P:mypass;;
 *
 * The fields can come in any order, and there should be tests to see
 * if we can parse them all correctly.
 *
 * @author Vikram Aggarwal
 */
final class WifiResultParser extends ResultParser {

  private WifiResultParser() {
  }

  public static WifiParsedResult parse(Result result) {
    String rawText = result.getText();

    if (rawText == null || !rawText.startsWith("WIFI:")) {
      return null;
    }

    // Don't remove leading or trailing whitespace
    boolean trim = false;
    String ssid = matchSinglePrefixedField("S:", rawText, ';', trim);
    String pass = matchSinglePrefixedField("P:", rawText, ';', trim);
    String type = matchSinglePrefixedField("T:", rawText, ';', trim);

    return new WifiParsedResult(type, ssid, pass);
  }
}