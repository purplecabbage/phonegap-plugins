PhoneGap.addConstructor(function () {

    /**
    * Implements DateTime picker to select a date or time. 
    * Initial value for date or time could be set via 'value' option
    * 
    * Usage examples:
    *   navigator.plugins.dateTimePicker.selectDate(success, error,  {value: new Date()});
    */
    navigator.plugins.dateTimePicker =
    {
        /**
        * Open DateTime picker to select a date
        *
        * @param {Function} successCallback
        * @param {Function} errorCallback
        * @param {Object} options - additional options: 'value' - initial value for date
        */
        selectDate: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("DateTimePicker Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("DateTimePicker Error: errorCallback is not a function");
                return;
            }

            PhoneGap.exec(function (res) {

                successCallback(new Date(res));

            },errorCallback, "DateTimePicker", "selectDate", options);
        },

        /**
        * Open DateTime picker to select a time
        *
        * @param {Function} successCallback
        * @param {Function} errorCallback
        * @param {Object} options - additional options: 'value' - initial value for time
        */
        selectTime: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("DateTimePicker Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("DateTimePicker Error: errorCallback is not a function");
                return;
            }

            PhoneGap.exec(function (res) {

                successCallback(new Date(res));

            }, errorCallback, "DateTimePicker", "selectTime", options);
        }
    }
});