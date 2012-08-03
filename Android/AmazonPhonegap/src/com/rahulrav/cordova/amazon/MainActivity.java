package com.rahulrav.cordova.amazon;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;

import android.os.Bundle;

public class MainActivity extends DroidGap {

  private CordovaWebView webView;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    webView = (CordovaWebView) findViewById(R.id.webView);
    webView.loadUrl("file:///android_asset/web/index.html");
  }

  protected CordovaWebView getWebView() {
    return webView;
  }
}
