package com.rahulrav.cordova.amazon.util;

import java.util.Collection;

import org.json.JSONArray;

/**
 * A set of reusable macros
 * 
 * @author Rahul Ravikumar
 * 
 */
public final class Macros {

  /**
   * Returns <code>true</true> if the {@link String} is empty
   */
  public static boolean isEmpty(final String str) {
    return str == null || str.length() <= 0;
  }

  /**
   * Returns <code>true</true> if the array is empty
   */
  public static <T> boolean isEmptyArray(final T[] arr) {
    return arr == null || arr.length <= 0;
  }

  /**
   * Returns <code>true</true> if the {@link JSONArray} is empty
   */
  public static boolean isEmptyJSONArray(final JSONArray jarr) {
    return jarr == null || jarr.length() <= 0;
  }

  /**
   * Returns <code>true</true> if the {@link Collection} is empty
   */
  public static <T> boolean isEmptyCollection(final Collection<T> collection) {
    return collection == null || collection.size() <= 0;
  }

}
