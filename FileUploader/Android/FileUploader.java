/**
* 
*/
package com.beetight;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import android.net.Uri;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.webkit.CookieManager;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

/**
* @author matt
*
*/
public class FileUploader extends Plugin {

	/* (non-Javadoc)
	* @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	*/
	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
				
		try {
			String server = args.getString(0);
			String file = args.getString(1);
			JSONObject params = args.getJSONObject(2);
			
			String fileKey = "file";
			String fileName = "image.jpg";
			String mimeType = "image/jpeg";
			if(args.length() > 3) {
				fileKey = args.getString(3);
			}
			if(args.length() > 4) {
				fileName = args.getString(4);
			}
			if(args.length() > 5) {
				mimeType = args.getString(5);
			}
			
			if (action.equals("upload")) {
				upload(file, server, params, fileKey, fileName, mimeType, callbackId);
			} else if (action.equals("uploadByUri")) {
				Uri uri = Uri.parse(file);
				upload(uri, server, params, fileKey, fileName, mimeType, callbackId);
			} else {
	            return new PluginResult(PluginResult.Status.INVALID_ACTION);
			}
			PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
			r.setKeepCallback(true);
			return r;
		} catch (JSONException e) {
			e.printStackTrace();
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}

	}
	
	public void upload(Uri uri, String server, JSONObject params, final String fileKey, final String fileName, final String mimeType, String callbackId) {
		try {
			InputStream fileInputStream=this.ctx.getContentResolver().openInputStream(uri);
			upload(fileInputStream, server, params, fileKey, fileName, mimeType, callbackId);
		} catch (FileNotFoundException e) {
			Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
		}
	}

	public void upload(String filename, String server, JSONObject params, final String fileKey, final String fileName, final String mimeType, String callbackId) {
		File uploadFile = new File(filename);
		try {
			FileInputStream fileInputStream = new FileInputStream(uploadFile);
			upload(fileInputStream, server, params, fileKey, fileName, mimeType, callbackId);
		} catch (FileNotFoundException e) {
			Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
		}

	}
	
	
	public void upload(InputStream fileInputStream, String server, JSONObject params, final String fileKey, final String fileName, final String mimeType, final String callbackId) {
		try {

			String lineEnd = "\r\n"; 
			String td = "--"; 
			String boundary = "*****com.beetight.formBoundary"; 
			
			URL url = new URL(server);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
//			Get cookies that have been set in our webview
			CookieManager cm = CookieManager.getInstance();
			String cookie = cm.getCookie(server);
			
			// allow inputs 
			conn.setDoInput(true); 
			// allow outputs
			conn.setDoOutput(true); 
			// don't use a cached copy
			conn.setUseCaches(false);
			// use a post method 
			conn.setRequestMethod("POST"); 
			// set post headers 
			conn.setRequestProperty("Connection","Keep-Alive"); 
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary); 
			conn.setRequestProperty("Cookie", cookie);
			// open data output stream 
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream()); 
			
			try {
				for (Iterator iter = params.keys(); iter.hasNext();) {
					Object key = iter.next();
					dos.writeBytes(td + boundary + lineEnd); 
					dos.writeBytes("Content-Disposition: form-data; name=\"" +  key + "\"; ");
					dos.writeBytes(lineEnd + lineEnd); 
					dos.writeBytes(params.getString(key.toString()));
					dos.writeBytes(lineEnd); 
				}
			} catch (JSONException e) {
				Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			}

			
			dos.writeBytes(td + boundary + lineEnd); 
			dos.writeBytes("Content-Disposition: form-data; name=\"" + fileKey + "\";filename=\"" + fileName + "\"" + lineEnd); 
			dos.writeBytes("Content-Type: " + mimeType + lineEnd); 
			
			dos.writeBytes(lineEnd); 
			// create a buffer of maximum size 
			int bytesAvailable = fileInputStream.available(); 
			final int total = bytesAvailable;
			Log.e("PhoneGapLog", "available: " + bytesAvailable); 

			int maxBufferSize = 1024; 
			int bufferSize = Math.min(bytesAvailable, maxBufferSize); 
			byte[] buffer = new byte[bufferSize]; 
			// read file and write it into form... 
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
			int progress = bytesRead;
			int send = 0;
			while (bytesRead > 0) 
			{ 
				dos.write(buffer, 0, bufferSize); 
				bytesAvailable = fileInputStream.available(); 
				bufferSize = Math.min(bytesAvailable, maxBufferSize); 
				bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
				progress += bytesRead;
				final int prog = progress;
				Log.e("PhoneGapLog", "read " + progress + " of " + total); 
				
//				Sending every progress event is overkill
				if (send++ % 20 == 0) { 
					ctx.runOnUiThread(new Runnable () {
					public void run() {
						try {
							JSONObject result = new JSONObject();
							result.put("status", FileUploader.Status.PROGRESS); 
							result.put("progress", prog);
							result.put("total", total);
							PluginResult progressResult = new PluginResult(PluginResult.Status.OK, result);
							progressResult.setKeepCallback(true);
							success(progressResult, callbackId);
						} catch (JSONException e) {
							Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
						}
					}
				});
//					Give a chance for the progress to be sent to javascript
				Thread.sleep(100);
				} 
			} 
			// send multipart form data necessary after file data... 
			dos.writeBytes(lineEnd); 
			dos.writeBytes(td + boundary + td + lineEnd); 

			// close streams 
			fileInputStream.close(); 
			dos.flush(); 
			InputStream is = conn.getInputStream(); 
			int ch; 
			StringBuffer b =new StringBuffer(); 
			while( ( ch = is.read() ) != -1 ) { 
				b.append( (char)ch ); 
			} 
			String s=b.toString(); 
			dos.close(); 
			JSONObject result = new JSONObject();
			result.put("status", FileUploader.Status.COMPLETE);

			result.put("progress", progress);
			result.put("total", total);
			result.put("result", s);
			PluginResult progressResult = new PluginResult(PluginResult.Status.OK, result);
			progressResult.setKeepCallback(true);
			success(progressResult, callbackId);

		} 
		catch (MalformedURLException e) { 
			 Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			 PluginResult result = new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION, e.getMessage());
			 error(result, callbackId);
		} 
		catch (FileNotFoundException e) {
			 Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			 PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
			 error(result, callbackId);
		}
		catch (IOException e) { 
			 Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			 PluginResult result = new PluginResult(PluginResult.Status.IO_EXCEPTION, e.getMessage());
			 error(result, callbackId);
		} catch (InterruptedException e) {
			 Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			 PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
			 error(result, callbackId);
		} catch (JSONException e) {
			 Log.e("PhoneGapLog", "error: " + e.getMessage(), e); 
			 PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage());
			 error(result, callbackId);
		} 
	}
	
	public enum Status {
		PROGRESS,
		COMPLETE
	}


}