/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 * 
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2010, IBM Corporation
 */
package com.phonegap.plugins.facebook;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.content.Context;
import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

import com.facebook.android.Facebook;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;

public class FacebookAuth extends Plugin {
	
	public static final String APP_ID = "175729095772478";
	private Facebook mFb;
    
	/**
	 * Executes the request and returns PluginResult.
	 * 
	 * @param action 		The action to execute.
	 * @param args 			JSONArry of arguments for the plugin.
	 * @param callbackId	The callback id used when calling back into JavaScript.
	 * @return 				A PluginResult object with a status and message.
	 */
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		PluginResult.Status status = PluginResult.Status.OK;
		String result = "";
		System.out.println("execute: "+ action);
		
		try {
			if (action.equals("authorize")) {
				
				result = this.authorize(args.getString(0));
				if (result.length() > 0) {
					status = PluginResult.Status.ERROR;
				}
				System.out.println("result: "+ result);
				
			}
			
			return new PluginResult(status, result);
		} catch (JSONException e) {
			System.out.println("exception: "+ action);
			
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}
	}

	/**
	 * Identifies if action to be executed returns a value and should be run synchronously.
	 * 
	 * @param action	The action to execute
	 * @return			T=returns value
	 */
	public boolean isSynch(String action) {
		return false;
	}
    
    /**
     * Called by AccelBroker when listener is to be shut down.
     * Stop listener.
     */
    public void onDestroy() { 	
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------
    
    /**
     * Display a new browser with the specified URL.
     * 
     * @param url			The url to load.
     * @param usePhoneGap	Load url in PhoneGap webview
     * @return				"" if ok, or error message.
     */
    public String authorize(String url) {
	
		this.mFb = new Facebook(APP_ID);	
        this.mFb.authorize((Activity) this.ctx, new AuthorizeListener());
		return "string";
    }

	class AuthorizeListener implements DialogListener {
	  public void onComplete(Bundle values) {
	   //  Handle a successful login
	}
	   public void onFacebookError(FacebookError e) {
	        e.printStackTrace();
	    }

	    public void onError(DialogError e) {
	        e.printStackTrace();        
	    }

	    public void onCancel() {        
	    }
	}

    
}
