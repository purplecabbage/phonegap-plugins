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

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;

import java.util.List;

final class HistoryClickListener implements DialogInterface.OnClickListener {

  private final HistoryManager historyManager;
  private final CaptureActivity activity;
  private final List<Result> items;

  /**
   * Handles clicks in the History dialog.
   *
   * @author dswitkin@google.com (Daniel Switkin)
   * @author Sean Owen
   */
  HistoryClickListener(HistoryManager historyManager, CaptureActivity activity, List<Result> items) {
    this.historyManager = historyManager;
    this.activity = activity;
    this.items = items;
  }

  public void onClick(DialogInterface dialogInterface, int i) {
    if (i == items.size()) {
      // Share history.
      CharSequence history = historyManager.buildHistory();
      Uri historyFile = HistoryManager.saveHistory(history.toString());
      if (historyFile == null) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.msg_unmount_usb);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
        return;
      }
      Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      String subject = activity.getResources().getString(R.string.history_email_title);
      intent.putExtra(Intent.EXTRA_SUBJECT, subject);
      intent.putExtra(Intent.EXTRA_TEXT, subject);
      intent.putExtra(Intent.EXTRA_STREAM, historyFile);
      intent.setType("text/csv");
      activity.startActivity(intent);
    } else if (i == items.size() + 1) {
      // Clear history.
      historyManager.clearHistory();
    } else {
      // Display a single history entry.
      Result result = items.get(i);
      Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, result);
      message.sendToTarget();
    }
  }

}
