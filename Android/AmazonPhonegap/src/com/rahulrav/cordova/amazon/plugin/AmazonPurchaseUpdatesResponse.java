package com.rahulrav.cordova.amazon.plugin;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.Receipt;
import com.rahulrav.cordova.amazon.util.Macros;

public class AmazonPurchaseUpdatesResponse implements JSONValue {

  private final PurchaseUpdatesResponse purchaseUpdatesResponse;

  public AmazonPurchaseUpdatesResponse(final PurchaseUpdatesResponse purchaseUpdatesResponse) {
    if (purchaseUpdatesResponse == null) {
      throw new AmazonInAppException("'purchaseUpdatesResponse' cannot be null");
    }
    this.purchaseUpdatesResponse = purchaseUpdatesResponse;
  }

  @Override
  public JSONObject toJSON() throws JSONException {
    if (purchaseUpdatesResponse == null) {
      return null;
    }

    final JSONObject jobj = new JSONObject();
    jobj.put("requestId", purchaseUpdatesResponse.getRequestId());
    jobj.put("purchaseUpdatesRequestStatus", purchaseUpdatesResponse.getPurchaseUpdatesRequestStatus());
    jobj.put("userId", purchaseUpdatesResponse.getUserId());
    final Offset offset = purchaseUpdatesResponse.getOffset();
    if (offset != null) {
      jobj.put("offset", offset);
    }
    final Set<Receipt> receipts = purchaseUpdatesResponse.getReceipts();
    if (!Macros.isEmptyCollection(receipts)) {
      final JSONArray jReceipts = new JSONArray();
      for (final Receipt receipt : receipts) {
        final AmazonReceipt amazonReceipt = new AmazonReceipt(receipt);
        jReceipts.put(amazonReceipt.toJSON());
      }
      jobj.put("receipts", jReceipts);
    }

    final Set<String> revokedSkus = purchaseUpdatesResponse.getRevokedSkus();
    if (!Macros.isEmptyCollection(revokedSkus)) {
      jobj.put("revokedSkus", new JSONArray(revokedSkus));
    }

    return jobj;
  }
}
