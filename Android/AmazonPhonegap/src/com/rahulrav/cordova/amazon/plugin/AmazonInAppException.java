package com.rahulrav.cordova.amazon.plugin;

/**
 * Specific exceptions thrown by the {@link AmazonPurchasingObserver}
 * 
 * @author Rahul Ravikumar
 * 
 */
public class AmazonInAppException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AmazonInAppException(final String message) {
    super(message);
  }

  public AmazonInAppException(final Throwable e) {
    super(e);
  }

  public AmazonInAppException(final String message, final Throwable e) {
    super(message, e);
  }

}
