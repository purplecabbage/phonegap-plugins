/**
 * This class defines an AdMob object that is used to show ads natively in a
 * native Android application.
 * @constructor
 */
var AdMob = function() {
};

/**
 * This enum represents AdMob's supported ad sizes.  Use one of these
 * constants as the adSize when calling createBannerView.
 * @const
 */
AdMob.AD_SIZE = {
  BANNER: 'BANNER',
  IAB_MRECT: 'IAB_MRECT',
  IAB_BANNER: 'IAB_BANNER',
  IAB_LEADERBOARD: 'IAB_LEADERBOARD',
  SMART_BANNER: 'SMART_BANNER'
};

/** This piece of code adds AdMob as a Cordova plugin. */
cordova.addConstructor(function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.AdMob = new AdMob();
});

/**
 * Creates a new AdMob banner view.
 *
 * @param {!Object} options The options used to create a banner.  They should
 *        be specified similar to the following.
 *
 *        {
 *          'publisherId': 'MY_PUBLISHER_ID',
 *          'adSize': AdSize.BANNER,
 *          'positionAtTop': false
 *        }
 *
 *        publisherId is the publisher ID from your AdMob site, adSize
 *        is one of the AdSize constants, and positionAtTop is a boolean to
 *        determine whether to create the banner above or below the app content.
 *        A publisher ID and AdSize are required.  The default for postionAtTop
 *        is false, meaning the banner would be shown below the app content.
 * @param {function()} successCallback The function to call if the banner was
 *         created successfully.
 * @param {function()} failureCallback The function to call if create banner
 *         was unsuccessful.
 */
AdMob.prototype.createBannerView =
    function(options, successCallback, failureCallback) {
  var defaults = {
    'publisherId': undefined,
    'adSize': undefined,
    'positionAtTop': false
  };
  var requiredOptions = ['publisherId', 'adSize'];

  // Merge optional settings into defaults.
  for (var key in defaults) {
    if (typeof options[key] !== 'undefined') {
      defaults[key] = options[key];
    }
  }

  // Check for and merge required settings into defaults.
  requiredOptions.forEach(function(key) {
    if (typeof options[key] === 'undefined') {
      failureCallback('Failed to specify key: ' + key + '.');
      return;
    }
    defaults[key] = options[key];
  });

  cordova.exec(
      successCallback,
      failureCallback,
      'AdMobPlugin',
      'createBannerView',
      new Array(defaults)
  );
};

/**
 * Requests an AdMob ad for the banner.  This call should not be made until
 * after the banner view has been successfully created.
 *
 * @param {!Object} options The options used to request an ad.  They should
 *        be specified similar to the following.
 *
 *        {
 *          'isTesting': true|false,
 *          'extras': {
 *            'key': 'value'
 *          }
 *        }
 *
 *        isTesting is a boolean determining whether or not to request a
 *        test ad on an emulator, and extras represents the extras to pass
 *        into the request. If no options are passed, the request will have
 *        testing set to false and an empty extras.
 * @param {function()} successCallback The function to call if an ad was
 *        returned successfully.
 * @param {function()} failureCallback The function to call if an ad failed
 *        to return.
 */
AdMob.prototype.requestAd =
    function(options, successCallback, failureCallback) {
  var defaults = {
    'isTesting': false,
    'extras': {}
  };

  for (var key in defaults) {
    if (typeof options[key] !== 'undefined') {
      defaults[key] = options[key];
    }
  }

  cordova.exec(
      successCallback,
      failureCallback,
      'AdMobPlugin',
      'requestAd',
      new Array(defaults)
  );
};
