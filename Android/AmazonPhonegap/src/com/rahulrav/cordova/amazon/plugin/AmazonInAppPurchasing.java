package com.rahulrav.cordova.amazon.plugin;

import java.util.HashSet;
import java.util.Set;

import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

import android.app.Activity;

import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.phonegap.api.Plugin;
import com.rahulrav.cordova.amazon.util.Logger;
import com.rahulrav.cordova.amazon.util.Macros;

/**
 * Represents the main In-App Purchasing Plugin definition.
 * 
 * @author Rahul Ravikumar
 * 
 */
public class AmazonInAppPurchasing extends Plugin {

  /**
   * @see Plugin#execute(String, JSONArray, String)
   */
  @Override
  public PluginResult execute(final String request, final JSONArray args, final String callbackId) {

    Logger.d(String.format("Executing (%s, %s, %s)", request, args, callbackId));

    try {
      // register purchase observer
      final Activity activityContext = cordova.getActivity();
      final AmazonPurchasingObserver purchaseObserver = AmazonPurchasingObserver.getPurchasingObserver(this, activityContext);

      if (Macros.isEmpty(request) || !request.matches("(?i)(initialize|itemData|purchase|purchaseUpdates|userId)")) {
        Logger.e(String.format("Invalid Request %s", request));
        return new PluginResult(Status.INVALID_ACTION);
      }

      // initialize request
      if (request.matches("(?i)initialize")) {

        // prevent repeated initialization
        if (purchaseObserver.isAlreadyInitialized()) {
          // send the old response back
          return new PluginResult(Status.OK, purchaseObserver.sdkAvailableResponse());
        }

        // sdk initialization request
        purchaseObserver.registerInitialization(callbackId);
        PurchasingManager.registerObserver(purchaseObserver);
        final PluginResult result = new PluginResult(Status.NO_RESULT);
        result.setKeepCallback(true);
        return result;
      }

      // purchase request
      if (request.matches("(?i)purchase")) {
        if (Macros.isEmptyJSONArray(args)) {
          Logger.e("Invalid purchase request");
          return new PluginResult(Status.ERROR);
        }

        final String sku = args.optString(0);
        final String requestId = PurchasingManager.initiatePurchaseRequest(sku);
        return completeRequest(purchaseObserver, requestId, callbackId);
      }

      // purchase updates request
      if (request.matches("(?i)purchaseUpdates")) {
        final String strOffset = !Macros.isEmptyJSONArray(args) ? args.optString(0, null) : null;
        final Offset offset = Macros.isEmpty(strOffset) ? Offset.BEGINNING : Offset.fromString(strOffset);
        final String requestId = PurchasingManager.initiatePurchaseUpdatesRequest(offset);
        return completeRequest(purchaseObserver, requestId, callbackId);
      }

      // item data request
      if (request.matches("(?i)itemData")) {
        if (Macros.isEmptyJSONArray(args)) {
          Logger.e("Invalid item data request");
          return new PluginResult(Status.ERROR);
        }

        final JSONArray skus = args.optJSONArray(0);

        final Set<String> skuSet = new HashSet<String>();
        for (int i = 0; i < skus.length(); i++) {
          skuSet.add(skus.optString(i));
        }

        final String requestId = PurchasingManager.initiateItemDataRequest(skuSet);
        return completeRequest(purchaseObserver, requestId, callbackId);
      }

      // user id request
      if (request.matches("(?i)userId")) {
        final String requestId = PurchasingManager.initiateGetUserIdRequest();
        return completeRequest(purchaseObserver, requestId, callbackId);
      }

    } catch (final Exception e) {
      Logger.e(String.format("Unable to execute request (%s. %s. %s)", request, args != null ? args.toString() : "", callbackId), e);
    }

    return new PluginResult(Status.ERROR);

  }

  /*
   * a helper method that completes a request
   */
  private PluginResult completeRequest(final AmazonPurchasingObserver purchaseObserver, final String requestId, final String callbackId) {
    purchaseObserver.addRequest(requestId, callbackId);
    final PluginResult result = new PluginResult(Status.NO_RESULT);
    result.setKeepCallback(true);
    return result;
  }
}