package com.phonegap.plugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents.Insert;
import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

/**
 * Interactive with android contact provider
 * 
 * @author ray
 * 
 */
public class ContactProvider extends Plugin {
	private static final int ADD_CONTACT = 1;
	private static final int PICK_CONTACT = 2;
	private String callback;

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		this.callback = callbackId;
		try {
			if (action.trim().toLowerCase().equals("add")) {
				startAddContactActivity(args);
			} else if (action.trim().toLowerCase().equals("pick")) {
				startPickContactActivity(args);
			}

			PluginResult mPlugin = new PluginResult(
					PluginResult.Status.NO_RESULT);
			mPlugin.setKeepCallback(true);
			return mPlugin;
		} catch (JSONException e) {
			Log.e("ContactAdd", "execute args error", e);
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}
	}

	private void startAddContactActivity(JSONArray args) throws JSONException {
		String contactName = args.getString(0);
		String contactEmail = args.getString(1);

		final Intent intent = new Intent(
				ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
				Uri.parse(String.format("mailto: %s", contactEmail)));

		intent.putExtra(Insert.NAME, contactName);
		intent.putExtra(Insert.EMAIL, contactEmail);
		intent.putExtra(Insert.EMAIL_TYPE,
				ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER);
		intent.putExtra(Insert.EMAIL_ISPRIMARY, "true");
		this.ctx.startActivityForResult((Plugin) this, intent, ADD_CONTACT);
	}

	private void startPickContactActivity(JSONArray args) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		this.ctx.startActivityForResult((Plugin) this, intent, PICK_CONTACT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
		case (ADD_CONTACT):
			endAddContact(resultCode, intent);
			break;
		case (PICK_CONTACT):
			endPickContact(resultCode, intent);
			break;
		}
	}

	private void endAddContact(int resultCode, Intent intent) {
		this.success(new PluginResult(PluginResult.Status.OK), this.callback);
	}

	private void endPickContact(int resultCode, Intent intent) {
		String name = null;
		String number = null;
		if (resultCode == Activity.RESULT_OK) {
			Uri contactData = intent.getData();
			Cursor c = this.ctx.managedQuery(contactData, null, null, null,
					null);
			if (c.moveToFirst()) {
				String ContactID = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				String hasPhone = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (Integer.parseInt(hasPhone) == 1) {
					Cursor phoneCursor = this.ctx.managedQuery(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "='" + ContactID + "'", null, null);
					while (phoneCursor.moveToNext()) {
						number = phoneCursor
								.getString(phoneCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
				}

				name = c.getString(c
						.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				JSONObject contactObject = new JSONObject();
				try {
					contactObject.put("name", name);
					contactObject.put("phone", number);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				this.success(new PluginResult(PluginResult.Status.OK,
						contactObject), this.callback);
			}
		}
	}
}
