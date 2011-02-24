/**
 * Phonegap Context Holder
 * Omer Saatcioglu 2011
 *
 */
 
package com.saatcioglu.phonegap.clipboardmanager;

import android.content.Context;

/**
 * A method proposed to make the context reachable to the PhoneGap plug-ins
 * @author Omer Saatcioglu
 *
 */
public class ContextHolder{

	private static Context mContext;

	/**
	 * @return the context
	 */
	public static Context get() {
		return mContext;
	}

	/**
	 * @param context the context
	 */
	public static void set(Context context) {
		mContext = context;
	}

}
