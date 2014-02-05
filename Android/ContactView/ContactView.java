package com.rearden;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Base64;

import org.apache.cordova.api.LOG;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;

public class ContactView extends Plugin {
	private static final int PICK_CONTACT = 0;
	private String callback;
	private final String pluginName = "ContactView";

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		LOG.d(pluginName, "ContactView called with options: " + args + " cb: "+ callbackId);
		startContactActivity();
		PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
		mPlugin.setKeepCallback(true);
		this.callback = callbackId;
		return mPlugin;
	}

	public void startContactActivity() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		this.cordova.startActivityForResult((Plugin) this, intent, PICK_CONTACT);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		String name = null;
		JSONArray phone = new JSONArray();
		String number = null;
		String email = null;
		LOG.d(pluginName, "Pick contact result? : " + (reqCode==PICK_CONTACT));
		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = this.cordova.getActivity().managedQuery(contactData,
						null, null, null, null);
				if (c.moveToFirst()) {
					long ContactID = c.getLong(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					ContentResolver cr = this.cordova.getActivity().getContentResolver();
					Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, ContactID);
					String photoBase64 = null;
					if (photoUri != null) {
				        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
				                cr, photoUri);
				        if (input != null) {
				            Bitmap bitmap = BitmapFactory.decodeStream(input);
				            ByteArrayOutputStream stream = new ByteArrayOutputStream();  
				            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				            byte[] image = stream.toByteArray();
				            photoBase64 = new String(Base64.encode(image, Base64.DEFAULT));
				        }
				    }
					if (Integer.parseInt(hasPhone) == 1) {
						Cursor phoneCursor = this.cordova
								.getActivity()
								.managedQuery(
										ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ "='" + ContactID + "'", null,
										null);
						while (phoneCursor.moveToNext()) {
							number = phoneCursor
									.getString(phoneCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phone.put(number);
						}
					}
					// get email address
					Cursor emailCur = this.cordova.getActivity().managedQuery(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ "='" + ContactID + "'", null, null);
					while (emailCur.moveToNext()) {
						// This would allow you get several email addresses
						// if the email addresses were stored in an array
						email = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						// String emailType = emailCur.getString(
						// emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					}
					//emailCur.close();

					name = c.getString(c
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					JSONObject contactObject = new JSONObject();
					try {
						//LOG.d(pluginName, "photo: " + photoBase64);
						contactObject.put("name", name);
						contactObject.put("phone", phone);
						contactObject.put("email", email);
						contactObject.put("photo", photoBase64!=null?photoBase64:JSONObject.NULL);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					this.success(new PluginResult(PluginResult.Status.OK,
							contactObject), this.callback);
				}
			}
			break;
		}
	}
}