package com.salman.plugins.thumbnailcreator;
/*****************************************************************************
 * 
 * ThumbnailCreator
 * Author: Kerri Shotts, June 1 2012
 *    for: Salman FF, PhoneGap Community
 * 
 * portions from http://www.java2s.com/Code/Android/2D-Graphics/SaveloadBitmap.htm
 * and phonegap-plugins/blob/master/Android/Downloader/Downloader.java (as template)
 * 
 * This plugin will take an incoming image (specified by souceImageUrl) and resize it
 * according to the incoming width and height and quality. It will then save it to 
 * the incoming target url.
 * 
 * Installation: Create a directory named "com.salman.plugins.thumbnailcreator"
 * under the src directory in your project; copy ThumbnailCreator.java to that
 * directory in your project. Register the plugin in the plugins.xml file
 * 
 *  <plugin name="ThumbnailCreator" value="com.salman.plugins.thumbnailcreator.ThumbnailCreator">
 * 
 * Include the .js file in your assets/www directory
 * and then use
 * 
 *     window.plugin.ThumbnailCreator.createThumbnail ( 
 *         "/the/full/path/to/the/source/image.jpg",
 *         "/the/full/path/to/the/target/image.jpg",
 *         300, // the new width
 *         200, // the new height
 *         80,  // the jpeg quality
 *         success, // the success function
 *         failure  // the failure function
 *     );
 *     
 * Failure Codes: IO_EXCEPTION (raised for any file related problem),
 *                MALFORMED_URL_EXCEPTION (not passing anything for the source or target)
 *                JSON_EXCEPTION (error in the JSON created by createThumbnail() )
 * Success Code:  OK (image resized, and saved to location.)
 * 
 */
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;


public class ThumbnailCreator extends Plugin {

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		
		if (!action.equals("createThumbnail"))
		{
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		}
		
		// the intent is to create a thumbnail with the following parameters:
		//     sourceImageUrl: the full path to the source image
		//     targetImageUrl: the full path to the target (file must not already exist)
		//     newWidth:       the new Width to resize the source image
		//     newHeight:      the new Height to resize the source image
		
		try
		{
			JSONObject params = args.getJSONObject (1);
			String sourceImageUrl = params.has ("sourceImageUrl") ? params.getString ( "sourceImageUrl" ) : "";
			String targetImageUrl = params.has ("targetImageUrl") ? params.getString ( "targetImageUrl" ) : "";
			Integer newWidth = params.has ("newWidth") ? params.getInt("newWidth") : 1024;
			Integer newHeight = params.has ("newHeight") ? params.getInt("newHeight") : 768;
			Integer quality = params.has ("quality") ? params.getInt("quality") : 80;
			
			if (sourceImageUrl.equals("") || targetImageUrl.equals(""))
			{
				return new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION);
			}
			
			// next, load the source image
			File theInputFile = null;
			FileInputStream theInputStream;
			Bitmap theSourceBitmap;
			try
			{
				theInputFile = new File (sourceImageUrl);
				if (theInputFile.canRead())
				{
					// we can read the file, now load it into the bitmap
					theInputStream = new FileInputStream(theInputFile);
					if (theInputStream != null && theInputStream.available() > 0)
					{
						theSourceBitmap = BitmapFactory.decodeStream (theInputStream);
					}
					else
					{
						return new PluginResult(PluginResult.Status.IO_EXCEPTION);
					}
				}
				else
				{
					return new PluginResult(PluginResult.Status.IO_EXCEPTION);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				return new PluginResult(PluginResult.Status.IO_EXCEPTION);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				return new PluginResult(PluginResult.Status.IO_EXCEPTION);
			}
			
			// now that we have the source image loaded, let's resize the image
			Bitmap theTargetBitmap;
			theTargetBitmap = Bitmap.createScaledBitmap(theSourceBitmap, newWidth, newHeight, true);
			
			// let's try to save it next
			
			FileOutputStream theOutputStream;
			try
			{
				File theOutputFile = new File (targetImageUrl);
				if (theOutputFile.createNewFile() && theOutputFile.canWrite())
				{
					// we've created the new file, and can write to it
					theOutputStream = new FileOutputStream ( theOutputFile );
					if (theOutputStream != null)
					{
						theTargetBitmap.compress(CompressFormat.JPEG, quality, theOutputStream);
					}
					else
					{
						return new PluginResult(PluginResult.Status.IO_EXCEPTION);
					}
				}
				else
				{
					return new PluginResult(PluginResult.Status.IO_EXCEPTION);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return new PluginResult(PluginResult.Status.IO_EXCEPTION);
			}
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage());
		}

		// we loaded the file, resized it, saved it again, and we're here --
		// it must be completed! Return OK
		return new PluginResult(PluginResult.Status.OK);
	}

}
