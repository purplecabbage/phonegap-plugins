package com.phonegap.plugins.statusBarNotification;
// import com.yourapp.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class StatusNotificationIntent {
	public Notification buildNotification( Context ctx, CharSequence contentTitle, CharSequence contentText ) {
    int icon = R.drawable.notification;
    long when = System.currentTimeMillis();
    Notification noti = new Notification(icon, contentTitle, when);
  	Intent notificationIntent = new Intent(ctx, ctx.getClass());
    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
    noti.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);
    return noti;
	}
}
