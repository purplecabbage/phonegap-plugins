package com.rahulrav.cordova.amazon.plugin;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.inapp.purchasing.Receipt;
import com.amazon.inapp.purchasing.SubscriptionPeriod;

public class AmazonReceipt implements JSONValue {

  private final Receipt receipt;

  public AmazonReceipt(final Receipt receipt) {
    if (receipt == null) {
      throw new AmazonInAppException("'receipt' cannot be null");
    }
    this.receipt = receipt;
  }

  protected Receipt getReceipt() {
    return receipt;
  }

  @Override
  public JSONObject toJSON() throws JSONException {
    if (receipt == null) {
      return null;
    }

    final JSONObject jobj = new JSONObject();
    // sku
    jobj.put("sku", receipt.getSku());
    // itemType
    jobj.put("itemType", receipt.getItemType());
    // purchase token
    jobj.put("purchaseToken", receipt.getPurchaseToken());
    // subscription period
    final SubscriptionPeriod subscriptionPeriod = receipt.getSubscriptionPeriod();
    if (subscriptionPeriod != null) {
      final Date startDate = subscriptionPeriod.getStartDate();
      if (startDate != null) {
        jobj.put("startDate", startDate.getTime());
      }
      final Date endDate = subscriptionPeriod.getEndDate();
      if (endDate != null) {
        jobj.put("endDate", endDate.getTime());
      }
    }
    return jobj;
  }

}
