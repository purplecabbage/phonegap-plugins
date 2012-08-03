package com.rahulrav.cordova.amazon.plugin;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.GetUserIdResponse.GetUserIdRequestStatus;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingObserver;

/**
 * Provides a basic implementation of the basic {@link PurchasingObserver}
 * 
 * @author Rahul Ravikumar
 * 
 */
public class AmazonPurchasingObserver extends PurchasingObserver {

  private static AmazonPurchasingObserver instance;

  public static AmazonPurchasingObserver getPurchasingObserver(final AmazonInAppPurchasing plugin, final Context context) {
    if (instance == null) {
      instance = new AmazonPurchasingObserver(plugin, context);
    }
    return instance;
  }

  private final AmazonInAppPurchasing plugin;

  // is set to true when the sdk initialization is complete
  private boolean isInitialized;
  private JSONObject sdkAvailableResponse;

  // is the callback requestId used to initialize the PurchaseObserver
  private String initCallbackId;

  // represents the mapping from requestId -> callbackIds
  private final ConcurrentHashMap<String, String> requestCallbacks;

  /**
   * Associate the {@link AmazonPurchasingObserver} with the In-App Purchasing
   * plugin
   */
  private AmazonPurchasingObserver(final AmazonInAppPurchasing plugin, final Context context) {
    super(context);
    this.plugin = plugin;
    requestCallbacks = new ConcurrentHashMap<String, String>();
    isInitialized = false;
    sdkAvailableResponse = null;
  }

  protected AmazonInAppPurchasing getPlugin() {
    return plugin;
  }

  public boolean isAlreadyInitialized() {
    return isInitialized;
  }

  public JSONObject sdkAvailableResponse() {
    return sdkAvailableResponse;
  }

  public void addRequest(final String requestId, final String callbackId) {
    requestCallbacks.put(requestId, callbackId);
  }

  public void registerInitialization(final String initCallbackId) {
    this.initCallbackId = initCallbackId;
  }

  @Override
  public void onGetUserIdResponse(final GetUserIdResponse userIdResponse) {
    if (userIdResponse == null) {
      // should never happen
      throw new AmazonInAppException("'null' userId response.");
    }

    final String requestId = userIdResponse.getRequestId();
    final String callbackId = requestCallbacks.remove(requestId);
    try {
      final JSONObject jobj = new JSONObject();
      final GetUserIdRequestStatus userIdRequestStatus = userIdResponse.getUserIdRequestStatus();
      jobj.put("requestId", userIdResponse.getRequestId());
      jobj.put("userIdRequestStatus", userIdRequestStatus);
      if (userIdRequestStatus == GetUserIdRequestStatus.SUCCESSFUL) {
        jobj.put("userId", userIdResponse.getUserId());
      }
      final PluginResult pluginResult = new PluginResult(Status.OK, jobj);
      plugin.success(pluginResult, callbackId);
    } catch (final JSONException e) {
      final PluginResult pluginInResult = new PluginResult(Status.JSON_EXCEPTION);
      plugin.error(pluginInResult, callbackId);
    }
  }

  @Override
  public void onItemDataResponse(final ItemDataResponse itemDataResponse) {
    if (itemDataResponse == null) {
      // should never happen
      throw new AmazonInAppException("'null' item data response.");
    }

    final String requestId = itemDataResponse.getRequestId();
    final String callbackId = requestCallbacks.remove(requestId);
    try {
      final AmazonItemDataResponse amazonItemDataResponse = new AmazonItemDataResponse(itemDataResponse);
      final PluginResult pluginResult = new PluginResult(Status.OK, amazonItemDataResponse.toJSON());
      plugin.success(pluginResult, callbackId);
    } catch (final JSONException e) {
      final PluginResult pluginInResult = new PluginResult(Status.JSON_EXCEPTION);
      plugin.error(pluginInResult, callbackId);
    }

  }

  @Override
  public void onPurchaseResponse(final PurchaseResponse purchaseResponse) {
    if (purchaseResponse == null) {
      // should never happen
      throw new AmazonInAppException("'null' purchase response.");
    }

    final String requestId = purchaseResponse.getRequestId();
    final String callbackId = requestCallbacks.remove(requestId);
    try {
      final AmazonPurchaseResponse amazonPurchaseResponse = new AmazonPurchaseResponse(purchaseResponse);
      final PluginResult pluginResult = new PluginResult(Status.OK, amazonPurchaseResponse.toJSON());
      plugin.success(pluginResult, callbackId);
    } catch (final JSONException e) {
      final PluginResult pluginInResult = new PluginResult(Status.JSON_EXCEPTION);
      plugin.error(pluginInResult, callbackId);
    }
  }

  @Override
  public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse purchaseUpdatesResponse) {
    if (purchaseUpdatesResponse == null) {
      // should never happen
      throw new AmazonInAppException("'null' purchase updates response.");
    }

    final String requestId = purchaseUpdatesResponse.getRequestId();
    final String callbackId = requestCallbacks.remove(requestId);
    try {
      final AmazonPurchaseUpdatesResponse amazonPurchaseUpdatesResponse = new AmazonPurchaseUpdatesResponse(purchaseUpdatesResponse);
      final PluginResult pluginResult = new PluginResult(Status.OK, amazonPurchaseUpdatesResponse.toJSON());
      plugin.success(pluginResult, callbackId);
    } catch (final JSONException e) {
      final PluginResult pluginInResult = new PluginResult(Status.JSON_EXCEPTION);
      plugin.error(pluginInResult, callbackId);
    }
  }

  @Override
  public void onSdkAvailable(final boolean isSdkAvailable) {
    isInitialized = true;
    try {
      final JSONObject jobj = new JSONObject();
      jobj.put("isSdkAvailable", isSdkAvailable);
      // update the sdk available response
      sdkAvailableResponse = jobj;
      plugin.success(jobj, initCallbackId);
    } catch (final JSONException e) {
      final PluginResult pluginInResult = new PluginResult(Status.JSON_EXCEPTION);
      plugin.error(pluginInResult, initCallbackId);
    }
  }
}
