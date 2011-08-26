/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2011, Nitobi Software Inc.
 * Copyright (c) 2010-2011, IBM Corporation
 */
package com.phonegap.plugins.childBrowser;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ibm.filcontact.R;
import com.phonegap.api.PhonegapActivity;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class ChildBrowser extends Plugin {
    
    private Dialog dialog;
    private WebView webview;

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
                result = this.showWebPage(args.getString(0));
                if (result.length() > 0) {
                    status = PluginResult.Status.ERROR;
                }
            }
            else if (action.equals("close")) {
                closeDialog();
            }
            else if (action.equals("openExternal")) {
                result = this.openExternal(args.getString(0), args.optBoolean(1));
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
     * Display a new browser with the specified URL.
     *
     * @param url           The url to load.
     * @param usePhoneGap   Load url in PhoneGap webview
     * @return              "" if ok, or error message.
     */
    public String openExternal(String url, boolean usePhoneGap) {
        try {
            Intent intent = null;
            if (usePhoneGap) {
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
            System.out.println("ChildDialog: Error loading url "+url+":"+ e.toString());
            return e.toString();
        }
    }

    /**
     * Closes the dialog
     */
    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * Checks to see if it is possible to go back one page in history, then does so.
     */
    private void goBack() {
        if (this.webview.canGoBack()) {
            this.webview.goBack();
        }
    }

    /**
     * Checks to see if it is possible to go forward one page in history, then does so.
     */
    private void goForward() {
        if (this.webview.canGoForward()) {
            this.webview.goForward();
        }
    }

    /**
     * Display a new browser with the specified URL.
     *
     * @param url           The url to load.
     */
    public String showWebPage(final String url) {
        Runnable runnable = new Runnable() {
            public void run() {
                /**/
                dialog = new Dialog(ctx);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.childbrowser);
                dialog.setTitle("Child Browser");
                dialog.setCancelable(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            ctx.sendJavascript("window.plugins.childDialog._onClose();");
                        }
                });

                Button back = (Button) dialog.findViewById(R.id.backBtn);
                back.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        goBack();
                    }
                });
                Button forward = (Button) dialog.findViewById(R.id.forwardBtn);
                forward.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        goForward();
                    }
                });
                Button button = (Button) dialog.findViewById(R.id.closeBtn);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        closeDialog();
                    }
                });

                webview = (WebView) dialog.findViewById(R.id.childBrowser);
                webview.getSettings().setJavaScriptEnabled(true);
                WebViewClient client = new ChildBrowserClient(ctx);
                webview.setWebViewClient(client);                
                webview.loadUrl(url);
                
                dialog.show();
            }
        };
        this.ctx.runOnUiThread(runnable);
        return "";
    }

    /**
     * The webview client receives notifications about appView
     */
    public class ChildBrowserClient extends WebViewClient {

        private static final String LOG_TAG = "ChildBrowser";
        PhonegapActivity ctx;

        /**
         * Constructor.
         * 
         * @param mContext
         */
        public ChildBrowserClient(PhonegapActivity mContext) {
            this.ctx = mContext;
        }       

        /**
         * Notify the host application that a page has started loading.
         * 
         * @param view          The webview initiating the callback.
         * @param url           The url of the page.
         */
        @Override
        public void onPageStarted(WebView view, String url,  Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(LOG_TAG, "Hey I got a page started for url = " + url);
            ctx.sendJavascript("window.plugins.childDialog._onLocationChange('" + url + "');");
        }
    }
}
