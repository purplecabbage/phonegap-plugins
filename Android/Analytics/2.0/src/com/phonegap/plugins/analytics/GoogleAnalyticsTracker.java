/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2006-2011 Worklight, Ltd.  
 */

package com.phonegap.plugins.analytics;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class GoogleAnalyticsTracker extends Plugin {
	public static final String START = "start";
	
	public static final String SEND_VIEW = "sendView";
	public static final String SEND_EVENT = "sendEvent";
    
	public static final int DISPATCH_INTERVAL = 20;
	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	
	public GoogleAnalyticsTracker() {
	}
	
	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId) {
		PluginResult result = null;
		if (START.equals(action)) {
			try {
				start(data.getString(0));
				result = new PluginResult(Status.OK);
			} catch (JSONException e) {
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
		} else if (SEND_VIEW.equals(action)) {
			try {
				sendView(data.getString(0));
				result = new PluginResult(Status.OK);
			} catch (JSONException e) {
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
		} else if (SEND_EVENT.equals(action)) {
			try {
				sendEvent(data.getString(0), data.getString(1), data.getString(2), data.getLong(3));
				result = new PluginResult(Status.OK);
			} catch (JSONException e) {
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
		}
		return result;
	}
	
	private void start(String accountId) { 
		mGaInstance = GoogleAnalytics.getInstance(this.cordova.getActivity());
		mGaTracker = mGaInstance.getTracker(accountId);
	}
	
	private void sendView(String key) {
		mGaTracker.sendView(key);
	}

	private void sendEvent(String category, String action, String label, Long value){
		mGaTracker.sendEvent(category, action, label, value);
	}
}