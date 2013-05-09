/**
 * ZeroConf plugin for Cordova/Phonegap
 *
 * @author Matt Kane
 * Copyright (c) Triggertrap Ltd. 2012. All Rights Reserved.
 * Available under the terms of the MIT License.
 *
 */

package com.triggertrap;

import java.io.IOException;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiManager;
import android.util.Log;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class ZeroConf extends CordovaPlugin {
    WifiManager.MulticastLock lock;
    private JmDNS jmdns = null;
    private ServiceListener listener;
    private CallbackContext callbackContext;
    private static final String LOG_TAG = "ZeroConf";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        if (action.equals("watch")) {
            String type = args.optString(0);
            if (type != null) {
                watch(type);
                PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            } else {
                callbackContext.error("Service type not specified.");
            }
        } else if (action.equals("unwatch")) {
            String type = args.optString(0);
            if (type != null) {
                unwatch(type);
            } else {
                callbackContext.error("Service type not specified.");
            }
        } else if (action.equals("register")) {
            JSONObject obj = args.optJSONObject(0);
            if (obj != null) {
                String type = obj.optString("type");
                String name = obj.optString("name");
                int port = obj.optInt("port");
                String text = obj.optString("text");
                if(type == null) {
                    callbackContext.error("Missing required service info.");
                }
                register(type, name, port, text);
            } else {
                callbackContext.error("Missing required service info.");
            }

        } else if (action.equals("close")) {
            if(jmdns != null) {
                try {
                    jmdns.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }  else if (action.equals("unregister")) {
            if(jmdns != null) {
                jmdns.unregisterAllServices();
            }

        } else {
            Log.e(LOG_TAG, "Invalid action: " + action);
            return false;
        }
        PluginResult result = new PluginResult(Status.NO_RESULT);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
        return true;
    }

    private void watch(String type) {
        if(jmdns == null) {
            setupWatcher();
        }
        Log.d(LOG_TAG, "Watch " + type);
        Log.d(LOG_TAG, "Name: " + jmdns.getName() + " host: " + jmdns.getHostName());
        jmdns.addServiceListener(type, listener);
    }
    private void unwatch(String type) {
        if(jmdns == null) {
            return;
        }
        jmdns.removeServiceListener(type, listener);
    }

    private void register (String type, String name, int port, String text) {
        if(name == null) {
            name = "";
        }

        if(text == null) {
            text = "";
        }

        try {
            ServiceInfo service = ServiceInfo.create(type, name, port, text);
            jmdns.registerService(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupWatcher() {
        Log.d(LOG_TAG, "Setup watcher");
        WifiManager wifi = (WifiManager) this.cordova.getActivity().getSystemService(android.content.Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("ZeroConfPluginLock");
        lock.setReferenceCounted(true);
        lock.acquire();
        try {
            jmdns = JmDNS.create();
            listener = new ServiceListener() {

                public void serviceResolved(ServiceEvent ev) {
                    Log.d(LOG_TAG, "Resolved");
                    sendCallback("added", ev.getInfo());
                }

                public void serviceRemoved(ServiceEvent ev) {
                    Log.d(LOG_TAG, "Removed");
                    sendCallback("removed", ev.getInfo());
                }

                public void serviceAdded(ServiceEvent event) {
                    Log.d(LOG_TAG, "Added");
                    // Force serviceResolved to be called again
                    jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                }
            };

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void sendCallback(String action, ServiceInfo info) {
        JSONObject status = new JSONObject();
        try {
            status.put("action", action);
            status.put("service", jsonifyService(info));
            Log.d(LOG_TAG, "Sending result: " + status.toString());
            PluginResult result = new PluginResult(PluginResult.Status.OK, status);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public static JSONObject jsonifyService(ServiceInfo info) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("application", info.getApplication());
            obj.put("domain", info.getDomain());
            obj.put("port", info.getPort());
            obj.put("name", info.getName());
            obj.put("server", info.getServer());
            obj.put("description", info.getNiceTextString());
            obj.put("protocol", info.getProtocol());
            obj.put("qualifiedname", info.getQualifiedName());
            obj.put("type", info.getType());

            JSONArray addresses = new JSONArray();
            String[] add = info.getHostAddresses();
            for(int i = 0; i < add.length; i++) {
                addresses.put(add[i]);
            }
            obj.put("addresses", addresses);
            JSONArray urls = new JSONArray();

            String[] url = info.getURLs();
            for(int i = 0; i < url.length; i++) {
                urls.put(url[i]);
            }
            obj.put("urls", urls);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return obj;

    }

}
