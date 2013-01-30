/*
 	Author: Vishal Rajpal
 	Filename: ExtractZipFilePlugin.java
 	Created Date: 21-02-2012
 	Modified Date: 31-01-2013
*/

package org.apache.cordova.plugin.ExtractZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;

public class ExtractZipFilePlugin extends CordovaPlugin {

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("extract")){
			try {
				String filename = URLDecoder.decode(args.getString(0), "UTF-8");
				if (filename.startsWith("file://")){
					filename = filename.substring(7, filename.length());
				}else if (filename.startsWith("/")){
					filename = filename.substring(1, filename.length());
				}
				String destination = URLDecoder.decode(args.getString(1), "UTF-8");
				if (destination.startsWith("file://")){
					destination = destination.substring(7, destination.length());
				}else if (destination.startsWith("/")){
					destination = destination.substring(1, destination.length());
				}
				PluginResult res = this.unzip(filename, destination);
	        	callbackContext.sendPluginResult(res);            
			} catch (UnsupportedEncodingException e) {}     	
			return true;
        }else {
        	return false;
        }      
    }
	
	public PluginResult unzip(String filename, String destination){
		File file = new File(filename);
		if (!destination.endsWith("/")){
			destination = destination + "/";
		}
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile;
		try {
			zipfile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			while (e.hasMoreElements()) 
			  {
				  entry = (ZipEntry) e.nextElement();
				  is = new BufferedInputStream(zipfile.getInputStream(entry));
				  int count;
				  byte data[] = new byte[102222];
				  String fileName = destination + entry.getName();
				  File outFile = new File(fileName);
				  if (entry.isDirectory()) 
				  {
					  outFile.mkdirs();
				  } 
				  else 
				  {
					  FileOutputStream fos = new FileOutputStream(outFile);
					  dest = new BufferedOutputStream(fos, 102222);
					  while ((count = is.read(data, 0, 102222)) != -1)
					  {
						  dest.write(data, 0, count);
					  }
					  dest.flush();
					  dest.close();
					  is.close();
				  }
			  }
		} catch (ZipException e1) {
			return new PluginResult(PluginResult.Status.ERROR, "Invalid zip file");
		} catch (IOException e1) {
			return new PluginResult(PluginResult.Status.ERROR, "I/O error");
		}
		String encoding;
		try {
			encoding = URLEncoder.encode(destination, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			encoding = "";
		}
		return new PluginResult(PluginResult.Status.OK, encoding);
	}
	
}
