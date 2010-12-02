/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2010, IBM Corporation
 */
package com.phonegap.plugins.childBrowser;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class ChildBrowser extends Plugin {

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        PluginResult.Status status = PluginResult.Status.OK;
        String result = "";

        try {
            if (action.equals("showWebPage")) {
                result = this.showWebPage(args.getString(0), args.optBoolean(1));
                if (result.length() > 0) {
                    status = PluginResult.Status.ERROR;
                }
            }
            return new PluginResult(status, result);
        } catch (JSONException e) {
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        }
    }

    /**
     * Identifies if action to be executed returns a value and should be run synchronously.
     *
     * @param action    The action to execute
     * @return          T=returns value
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
     * NOTE: If usePhoneGap is set, only trusted PhoneGap URLs should be loaded,
     *       since any PhoneGap API can be called by the loaded HTML page.
     *
     * @param url           The url to load.
     * @param usePhoneGap   Load url in PhoneGap webview.
     * @return              "" if ok, or error message.
     */
    public String showWebPage(String url, boolean usePhoneGap) {
        try {
            Intent intent = null;
            if (usePhoneGap) {
            	// Loads and displays a new PhoneGap app on top of current PhoneGap app.
            	// For the currently running PhoneGap app:
            	// 		If keepRunning=true (default), then the current app continues to run in background
            	// 		If keepRunning=false, then the current app is paused by Android
            	// When BACK is pressed, the current app has focus.
                intent = new Intent().setClass(this.ctx, com.phonegap.DroidGap.class);
                intent.setData(Uri.parse(url)); // This line will be removed in future.
                intent.putExtra("url", url);

                // Timeout parameter: 60 sec max - May be less if http device timeout is less.
                intent.putExtra("loadUrlTimeoutValue", 60000);

                // These parameters can be configured if you want to show the loading dialog
                intent.putExtra("loadingDialog", "Wait,Loading web page...");   // show loading dialog
                intent.putExtra("hideLoadingDialogOnPageLoad", true);           // hide it once page has completely loaded
                
            }
            else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
            }
            this.ctx.startActivity(intent);
            return "";
        } catch (android.content.ActivityNotFoundException e) {
            System.out.println("ChildBrowser: Error loading url "+url+":"+ e.toString());
            return e.toString();
        }
    }

}
