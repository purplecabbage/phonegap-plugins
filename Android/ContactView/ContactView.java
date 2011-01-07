package com.rearden;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class ContactView extends Plugin {
	private static final int PICK_CONTACT = 1;
	private String callback;

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		startContactActivity();
		PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
		mPlugin.setKeepCallback(true);
		this.callback = callbackId;
		return mPlugin;
	}

	public void startContactActivity() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		this.ctx.startActivityForResult((Plugin) this, intent, PICK_CONTACT);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		String name = null;
		switch (reqCode) {
			case (PICK_CONTACT):
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor c = this.ctx.managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						this.success(new PluginResult(PluginResult.Status.OK, name),this.callback);
					}
				}
				break;
			}
		}
	}
