/**
 * Phonegap DatePicker Plugin Copyright (c) Greg Allen 2011 MIT Licensed
 * Reused and ported to Android plugin by Daniel van 't Oever
 */
if (typeof PhoneGap !== "undefined") {

  /**
   * Constructor
   */
  function DatePicker() {
    this._callback;
  }

  /**
   * show  
   */
  DatePicker.prototype.show = function(options, callback) {
    if (typeof options.date == "undefined") {
        options.date = new Date();
    }
    var datestamp = options.date.getTime();
    var timeHandler = function(returnDateString) {
      var timeOptions = options;
      var returnDate = timeStringToTime(returnDateString);
      timeOptions.mode = "time";
      timeOptions.date = new Date(datestamp);
      DatePicker.prototype.show( timeOptions, (function(returnTime) { callback(mergeDateTime(returnDate,returnTime)) } ));
    },
    cb = (function(timestring) { callback(timeStringToTime(timestring)); });

    options.date = timeToISO(options.date);

    // set default to datetime
    if (options.mode != 'time' && options.mode != 'date')
    {
       options.mode = 'date';
       cb = timeHandler;
    }

    this._callback = cb;

    return PhoneGap.exec(cb, failureCallback, 'DatePickerPlugin', options.mode, new Array(options));

  };

  function failureCallback(err) {
    console.log("datePickerPlugin.js failed: " + err);
  }

  function padDate(date) {
    var dateString = String(date);
    if (dateString.length == 1) {
      return ("0" + dateString);
    } else { 
      return dateString;
    }
  }

  function mergeDateTime( date, time ) {
    date.setHours(time.getHours());
    date.setMinutes(time.getMinutes());
    date.setSeconds(time.getSeconds());
    return date;
  }

  function timeStringToTime(timeString) {
    return (new Date(timeString));
  }

  function timeToISO(time) {
    var ISOTime = time.getFullYear() + "-" + padDate(time.getMonth()+1) + 
    "-" + padDate(time.getDate()) + "T" + 
    padDate(time.getHours()) + ":" + padDate(time.getMinutes()) + ":00Z";
    return ISOTime;
  } 

  PhoneGap.addConstructor(function() {
    if (!window.plugins) {
      window.plugins = {};
    }
    window.plugins.datePicker = new DatePicker();
  });
};
