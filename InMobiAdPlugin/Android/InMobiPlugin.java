package com.phonegap.inmobi;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.location.Location;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;

import com.inmobi.androidsdk.EducationType;
import com.inmobi.androidsdk.EthnicityType;
import com.inmobi.androidsdk.GenderType;
import com.inmobi.androidsdk.InMobiAdDelegate;
import com.inmobi.androidsdk.impl.InMobiAdView;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class InMobiPlugin extends Plugin implements InMobiAdDelegate {

	private String callback;
	private Button testButton;
	private LinearLayout root;
	private InMobiAdView adView;
	private Timer adRefreshTimer;
	private String siteId;
	
    private static final int adRefreshTime = 30000; //30 seconds
	
	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		this.callback = callbackId;
		
		if(action.equals("load"))
		{
			this.loadAd();
		}
		else if(action.equals("refresh"))
		{
			this.refreshAd();
		}
		else if(action.equals("close"))
		{
			this.closeAd();
		}
		else
		{
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		}
		PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
		r.setKeepCallback(true);
		return r;
	}

	private void closeAd() {
		// TODO Auto-generated method stub
	}

	private void refreshAd() {
		// TODO Auto-generated method stub
	}

	private void loadAd() {
		//This gets weird fast!
		final InMobiAdDelegate that = (InMobiAdDelegate) this;
		ctx.runOnUiThread(new Runnable() {			
			public void run() {
				adView = new InMobiAdView(ctx, null);
				//If they ever do deprecate this, I have no idea how the browser will work
				//(WebView is derived from AbsoluteLayout)
				@SuppressWarnings("deprecation")
				android.widget.AbsoluteLayout.LayoutParams adLayout = new AbsoluteLayout.LayoutParams(320, 48, 0, 0);
				adView.setLayoutParams(adLayout);
		        adView.initialize(ctx.getApplicationContext(), that, ctx, InMobiAdDelegate.INMOBI_AD_UNIT_320X48);
		        root = (LinearLayout) webView.getParent();
		        webView.addView(adView);
		        adView.loadNewAd();
		        adRefreshTimer = new Timer();
		        adRefreshTimer.schedule(new InMobiAdRefreshTimerTask(), adRefreshTime, adRefreshTime);
			}			
		});
        
	}
	
	private class InMobiAdRefreshTimerTask extends TimerTask {
		
		@Override
		public void run() {
			adView.loadNewAd();
		}
    	
    }

	public void adRequestCompleted(InMobiAdView arg0) {
		// TODO Auto-generated method stub
		
	}

	public void adRequestFailed(InMobiAdView arg0) {
		// TODO Auto-generated method stub
		
	}

	public int age() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String areaCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public Location currentLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date dateOfBirth() {
		// TODO Auto-generated method stub
		return null;
	}

	public EducationType education() {
		// TODO Auto-generated method stub
		return null;
	}

	public EthnicityType ethnicity() {
		// TODO Auto-generated method stub
		return null;
	}

	public GenderType gender() {
		// TODO Auto-generated method stub
		return null;
	}

	public int income() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String interests() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLocationInquiryAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPublisherProvidingLocation() {
		// TODO Auto-generated method stub
		return false;
	}

	public String keywords() {
		// TODO Auto-generated method stub
		return null;
	}

	public String postalCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String searchString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String siteId() {
		//this must be prefilled
		return "4028cba62f6890a9012f7007a91b004d";
	}

	public boolean testMode() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
