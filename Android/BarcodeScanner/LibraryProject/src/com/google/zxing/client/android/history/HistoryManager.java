/*
 * Copyright (C) 2009 ZXing authors
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

package com.google.zxing.client.android.history;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.PreferencesActivity;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Manages functionality related to scan history.</p>
 *
 * @author Sean Owen
 */
public final class HistoryManager {
  private static final String TAG = HistoryManager.class.getSimpleName();

  private static final int MAX_ITEMS = 500;

  private static final String[] GET_ITEM_COL_PROJECTION = {
      DBHelper.TEXT_COL,
      DBHelper.DISPLAY_COL,
      DBHelper.FORMAT_COL,
      DBHelper.TIMESTAMP_COL,
  };
  private static final String[] EXPORT_COL_PROJECTION = {
      DBHelper.TEXT_COL,
      DBHelper.DISPLAY_COL,
      DBHelper.FORMAT_COL,
      DBHelper.TIMESTAMP_COL,
  };
  private static final String[] ID_COL_PROJECTION = { DBHelper.ID_COL };
  private static final DateFormat EXPORT_DATE_TIME_FORMAT = DateFormat.getDateTimeInstance();

  private final CaptureActivity activity;

  public HistoryManager(CaptureActivity activity) {
    this.activity = activity;
  }

  public AlertDialog buildAlert() {
    SQLiteOpenHelper helper = new DBHelper(activity);
    List<Result> items = new ArrayList<Result>();
    List<String> dialogItems = new ArrayList<String>();
    SQLiteDatabase db = null;
    Cursor cursor = null;
    try {
      db = helper.getWritableDatabase();
      cursor = db.query(DBHelper.TABLE_NAME, GET_ITEM_COL_PROJECTION, null, null, null, null,
          DBHelper.TIMESTAMP_COL + " DESC");
      while (cursor.moveToNext()) {
        Result result = new Result(cursor.getString(0), null, null,
            BarcodeFormat.valueOf(cursor.getString(2)), cursor.getLong(3));
        items.add(result);
        String display = cursor.getString(1);
        if (display == null || display.length() == 0) {
          display = result.getText();
        }
        dialogItems.add(display);
      }
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      if (db != null) {
        db.close();
      }
    }

    Resources res = activity.getResources();
    dialogItems.add(res.getString(R.string.history_send));
    dialogItems.add(res.getString(R.string.history_clear_text));
    DialogInterface.OnClickListener clickListener = new HistoryClickListener(this, activity, items);
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.history_title);
    builder.setItems(dialogItems.toArray(new String[dialogItems.size()]), clickListener);
    return builder.create();
  }

  public void addHistoryItem(Result result, ResultHandler handler) {
    // Do not save this item to the history if the preference is turned off, or the contents are
    // considered secure.
    if (!activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY, true) ||
        handler.areContentsSecure()) {
      return;
    }

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    if (!prefs.getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES, false)) {
      deletePrevious(result.getText());
    }

    SQLiteOpenHelper helper = new DBHelper(activity);
    SQLiteDatabase db;
    try {
      db = helper.getWritableDatabase();
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
      return;
    }
    try {
      // Insert the new entry into the DB.
      ContentValues values = new ContentValues();
      values.put(DBHelper.TEXT_COL, result.getText());
      values.put(DBHelper.FORMAT_COL, result.getBarcodeFormat().toString());
      values.put(DBHelper.DISPLAY_COL, handler.getDisplayContents().toString());
      values.put(DBHelper.TIMESTAMP_COL, System.currentTimeMillis());
      db.insert(DBHelper.TABLE_NAME, DBHelper.TIMESTAMP_COL, values);
    } finally {
      db.close();
    }
  }

  private void deletePrevious(String text) {
    SQLiteOpenHelper helper = new DBHelper(activity);
    SQLiteDatabase db;
    try {
      db = helper.getWritableDatabase();
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
      return;
    }
    try {
      db.delete(DBHelper.TABLE_NAME, DBHelper.TEXT_COL + "=?", new String[] { text });
    } finally {
      db.close();
    }
  }

  public void trimHistory() {
    SQLiteOpenHelper helper = new DBHelper(activity);
    SQLiteDatabase db;
    try {
      db = helper.getWritableDatabase();
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
      return;
    }
    Cursor cursor = null;
    try {
      cursor = db.query(DBHelper.TABLE_NAME,
                        ID_COL_PROJECTION,
                        null, null, null, null,
                        DBHelper.TIMESTAMP_COL + " DESC");
      int count = 0;
      while (count < MAX_ITEMS && cursor.moveToNext()) {
        count++;
      }
      while (cursor.moveToNext()) {
        db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '=' + cursor.getString(0), null);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      db.close();
    }
  }

  /**
   * <p>Builds a text representation of the scanning history. Each scan is encoded on one
   * line, terminated by a line break (\r\n). The values in each line are comma-separated,
   * and double-quoted. Double-quotes within values are escaped with a sequence of two
   * double-quotes. The fields output are:</p>
   *
   * <ul>
   *  <li>Raw text</li>
   *  <li>Display text</li>
   *  <li>Format (e.g. QR_CODE)</li>
   *  <li>Timestamp</li>
   *  <li>Formatted version of timestamp</li>
   * </ul>
   */
  CharSequence buildHistory() {
    StringBuilder historyText = new StringBuilder(1000);
    SQLiteOpenHelper helper = new DBHelper(activity);
    SQLiteDatabase db;
    try {
      db = helper.getWritableDatabase();
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
      return "";
    }
    Cursor cursor = null;
    try {
      cursor = db.query(DBHelper.TABLE_NAME,
                        EXPORT_COL_PROJECTION,
                        null, null, null, null,
                        DBHelper.TIMESTAMP_COL + " DESC");
      while (cursor.moveToNext()) {
        for (int col = 0; col < EXPORT_COL_PROJECTION.length; col++) {
          historyText.append('"').append(massageHistoryField(cursor.getString(col))).append("\",");
        }
        // Add timestamp again, formatted
        long timestamp = cursor.getLong(EXPORT_COL_PROJECTION.length - 1);
        historyText.append('"').append(massageHistoryField(
            EXPORT_DATE_TIME_FORMAT.format(new Date(timestamp)))).append("\"\r\n");
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      db.close();
    }
    return historyText;
  }

  static Uri saveHistory(String history) {
    File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
    File historyRoot = new File(bsRoot, "History");
    if (!historyRoot.exists() && !historyRoot.mkdirs()) {
      Log.w(TAG, "Couldn't make dir " + historyRoot);
      return null;
    }
    File historyFile = new File(historyRoot, "history-" + System.currentTimeMillis() + ".csv");
    OutputStreamWriter out = null;
    try {
      out = new OutputStreamWriter(new FileOutputStream(historyFile), Charset.forName("UTF-8"));
      out.write(history);
      return Uri.parse("file://" + historyFile.getAbsolutePath());
    } catch (IOException ioe) {
      Log.w(TAG, "Couldn't access file " + historyFile + " due to " + ioe);
      return null;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException ioe) {
          // do nothing
        }
      }
    }
  }

  private static String massageHistoryField(String value) {
    return value.replace("\"","\"\"");
  }

  void clearHistory() {
    SQLiteOpenHelper helper = new DBHelper(activity);
    SQLiteDatabase db;
    try {
      db = helper.getWritableDatabase();
    } catch (SQLiteException sqle) {
      Log.w(TAG, "Error while opening database", sqle);
      return;
    }
    try {
      db.delete(DBHelper.TABLE_NAME, null, null);
    } finally {
      db.close();
    }
  }

}
