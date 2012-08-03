package com.rahulrav.cordova.amazon.plugin;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.rahulrav.cordova.amazon.util.Macros;

public class AmazonItemDataResponse implements JSONValue {

  private final ItemDataResponse itemDataResponse;

  public AmazonItemDataResponse(final ItemDataResponse itemDataResponse) {
    if (itemDataResponse == null) {
      throw new AmazonInAppException("'itemDataResponse' cannot be null");
    }
    this.itemDataResponse = itemDataResponse;
  }

  @Override
  public JSONObject toJSON() throws JSONException {
    if (itemDataResponse == null) {
      return null;
    }

    final JSONObject jobj = new JSONObject();

    jobj.put("itemDataRequestStatus", itemDataResponse.getItemDataRequestStatus());
    jobj.put("requestId", itemDataResponse.getRequestId());

    final Map<String, Item> itemData = itemDataResponse.getItemData();
    if (itemData != null) {
      final JSONObject jItemData = new JSONObject();
      for (final String key : itemData.keySet()) {
        final JSONObject jItem = new JSONObject();
        final Item item = itemData.get(key);
        jItem.put("itemType", item.getItemType());
        jItem.put("price", item.getPrice());
        jItem.put("title", item.getTitle());
        jItem.put("description", item.getDescription());
        jItem.put("smallIconUrl", item.getSmallIconUrl());
        jItemData.put(key, jItem);
      }
      jobj.put("itemData", jItemData);
    }

    final Set<String> unavailableSkus = itemDataResponse.getUnavailableSkus();
    if (!Macros.isEmptyCollection(unavailableSkus)) {
      jobj.put("unavailableSkus", new JSONArray(unavailableSkus));
    }

    return jobj;
  }

}
