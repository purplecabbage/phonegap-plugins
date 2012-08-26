package com.phonegap.plugins.statusBarNotification;
// import com.yourapp.R;

import android.app.Notification.Builder;
import android.app.Notification;
import android.content.Context;
import android.util.Log;

public class StatusNotificationBuilder {
	public Notification buildNotification( Context context, CharSequence contentTitle, CharSequence contentText ) {
    int icon = R.drawable.notification;
    Notification noti = new Notification.Builder(context)
      .setContentTitle(contentTitle)
      .setContentText(contentText)
      .setSmallIcon(icon)
      .build();
    return noti;
	}
}
