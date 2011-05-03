package com.phonegap.googleanalytics;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class GoogleAnalytics extends Plugin {

	private String callback;
	private GoogleAnalyticsTracker mTracker;
	
	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		this.callback = callbackId;
		if(action.equals("startTracker"))
		{
			startTracker(args);
		}
		else if(action.equals("trackEvent"))
		{
			trackEvent(args);
		}
		else if(action.equals("trackPage"))
		{
			trackPage(args);
		}
		else if(action.equals("stop"))
		{
			stopTracker(args);
		}
		else
		{
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		}
		PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
		r.setKeepCallback(true);
		return r;			
	}

	private void stopTracker(JSONArray args) {
		mTracker.stop();		
	}

	private void startTracker(JSONArray args) {
		mTracker = GoogleAnalyticsTracker.getInstance();
		if(args.length() > 0)
		{
			String uaCode;
			try {
				uaCode = args.getString(0);
				mTracker.start(uaCode, ctx.getApplicationContext());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//TODO: Add FAIL Callback
		}
	}

	private void trackPage(JSONArray args) {
		if(args.length() > 0 && mTracker != null)
		{
			String page;
			try {
				page = args.getString(0);
				mTracker.trackPageView(page);
				mTracker.dispatch();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//TODO: Add FAIL callback
		}
	}

	private void trackEvent(JSONArray args) {
		if(args.length() > 1 && mTracker != null)
		{	
			try {
				String arg0;			
				arg0 = args.getString(0);
				String arg1 = args.getString(1);
				String arg2 = args.length() >= 2 ? args.getString(2) : "";
				int arg3 = args.length() == 3 ? args.getInt(3) : 0;
				mTracker.trackEvent(arg0, arg1, arg2, arg3);
				mTracker.dispatch();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//TODO: Add FAIL callback
		}
	}

	
}
