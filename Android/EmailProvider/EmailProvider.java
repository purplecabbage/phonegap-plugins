package com.phonegap.plugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class EmailProvider extends Plugin {
	private static final int SEND_EMAIL = 1;
	private String callback;

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		this.callback = callbackId;
		try {
			startSendMailActivity(args);

			PluginResult mPlugin = new PluginResult(
					PluginResult.Status.NO_RESULT);
			mPlugin.setKeepCallback(true);
			return mPlugin;
		} catch (JSONException e) {
			Log.e("ContactAdd", "execute args error", e);
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}
	}

	private void startSendMailActivity(JSONArray args) throws JSONException {
		String toemail = args.getString(0);
		String tosubject = args.getString(1);
		String content = args.getString(2);

		final Intent intent = new Intent(android.content.Intent.ACTION_SEND);

		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { toemail });
		intent.putExtra(Intent.EXTRA_SUBJECT, tosubject);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		this.ctx.startActivityForResult((Plugin) this,
				Intent.createChooser(intent, tosubject), SEND_EMAIL);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
		case (SEND_EMAIL):
			endAddContact(resultCode, intent);
			break;
		}
	}

	private void endAddContact(int resultCode, Intent intent) {
		this.success(new PluginResult(PluginResult.Status.OK), this.callback);
	}
}
