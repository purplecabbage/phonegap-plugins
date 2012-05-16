/*
 * Copyright (C) 2008 ZXing authors
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

package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.client.android.R;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.WifiParsedResult;

/**
 * Handles address book entries.
 *
 * @author viki@google.com (Vikram Aggarwal)
 */
public final class WifiResultHandler extends ResultHandler {

  private final Activity parent;

  public WifiResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
    parent = activity;
  }

  @Override
  public int getButtonCount() {
    // We just need one button, and that is to configure the wireless.  This could change in the future.
    return 1;
  }

  @Override
  public int getButtonText(int index) {
    if (index == 0) {
      return R.string.button_wifi;
    }
    throw new ArrayIndexOutOfBoundsException();
  }

  @Override
  public void handleButtonPress(int index) {
    // Get the underlying wifi config
    WifiParsedResult wifiResult = (WifiParsedResult) getResult();
    if (index == 0) {
      wifiConnect(wifiResult);
    }
  }

  // Display the name of the network and the network type to the user.
  @Override
  public CharSequence getDisplayContents() {
    WifiParsedResult wifiResult = (WifiParsedResult) getResult();
    StringBuffer contents = new StringBuffer(50);
    String wifiLabel = parent.getString(R.string.wifi_ssid_label);
    ParsedResult.maybeAppend(wifiLabel + '\n' + wifiResult.getSsid(), contents);
    String typeLabel = parent.getString(R.string.wifi_type_label);
    ParsedResult.maybeAppend(typeLabel + '\n' + wifiResult.getNetworkEncryption(), contents);
    return contents.toString();
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_wifi;
  }
}