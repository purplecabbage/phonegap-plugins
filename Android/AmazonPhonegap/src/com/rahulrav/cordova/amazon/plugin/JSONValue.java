package com.rahulrav.cordova.amazon.plugin;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONValue {

  public JSONObject toJSON() throws JSONException;

}
