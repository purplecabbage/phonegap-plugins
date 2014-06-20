package com.rahulrav.cordova.amazon.util;

import android.util.Log;

/**
 * A proxy to the Android {@link Log}
 */
public class Logger {

  private static final String TAG = "AmazonInAppPurchasing";

  // info
  public static void i(final String message) {
    Log.i(TAG, message);
  }

  public static void i(final String message, final Throwable throwable) {
    Log.i(TAG, message, throwable);
  }

  // debug
  public static void d(final String message) {
    Log.d(TAG, message);
  }

  public static void d(final String message, final Throwable throwable) {
    Log.d(TAG, message, throwable);
  }

  // error
  public static void e(final String message) {
    Log.e(TAG, message);
  }

  public static void e(final String message, final Throwable throwable) {
    Log.e(TAG, message, throwable);
  }

  // verbose
  public static void v(final String message) {
    Log.v(TAG, message);
  }

  public static void v(final String message, final Throwable throwable) {
    Log.v(TAG, message, throwable);
  }

  // warning
  public static void w(final String message) {
    Log.w(TAG, message);
  }

  public static void w(final String message, final Throwable throwable) {
    Log.w(TAG, message, throwable);
  }
}