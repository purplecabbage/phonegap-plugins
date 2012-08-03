package com.rahulrav.cordova.amazon.plugin;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseResponse.PurchaseRequestStatus;
import com.amazon.inapp.purchasing.Receipt;

public class AmazonPurchaseResponse implements JSONValue {

  private final PurchaseResponse purchaseResponse;

  protected PurchaseResponse getPurchaseResponse() {
    return purchaseResponse;
  }

  public AmazonPurchaseResponse(final PurchaseResponse purchaseResponse) {
    if (purchaseResponse == null) {
      throw new AmazonInAppException("'purchaseResponse' cannot be null");
    }

    this.purchaseResponse = purchaseResponse;
  }

  @Override
  public JSONObject toJSON() throws JSONException {
    if (purchaseResponse == null) {
      return null;
    }

    final JSONObject jobj = new JSONObject();
    final PurchaseRequestStatus status = purchaseResponse.getPurchaseRequestStatus();
    // requestId
    jobj.put("requestId", purchaseResponse.getRequestId());
    // purchase request status
    jobj.put("purchaseRequestStatus", status);
    // logged in userId
    jobj.put("userId", purchaseResponse.getUserId());

    // we have a receipt only if the request was successful
    if (status == PurchaseRequestStatus.SUCCESSFUL) {
      final Receipt receipt = purchaseResponse.getReceipt();
      if (receipt != null) {
        final AmazonReceipt amazonReceipt = new AmazonReceipt(receipt);
        // receipt
        jobj.put("receipt", amazonReceipt.toJSON());
      }
    }

    return jobj;
  }
}
