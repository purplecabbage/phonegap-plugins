PhoneGap.addConstructor(function () {

    navigator.plugins.liveTiles =
    {
        updateAppTile: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("LiveTiles Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("LiveTiles Error: errorCallback is not a function");
                return;
            }
            PhoneGap.exec(successCallback, errorCallback, "LiveTiles", "updateAppTile", options);
        },

        createSecondaryTile: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("LiveTiles Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("LiveTiles Error: errorCallback is not a function");
                return;
            }
            PhoneGap.exec(successCallback, errorCallback, "LiveTiles", "createSecondaryTile", options);
        },
        updateSecondaryTile: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("LiveTiles Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("LiveTiles Error: errorCallback is not a function");
                return;
            }
            PhoneGap.exec(successCallback, errorCallback, "LiveTiles", "updateSecondaryTile", options);
        },
        deleteSecondaryTile: function (successCallback, errorCallback, options) {
            if (successCallback && (typeof successCallback !== "function")) {
                console.log("LiveTile Error: successCallback is not a function");
                return;
            }

            if (errorCallback && (typeof errorCallback !== "function")) {
                console.log("LiveTiles Error: errorCallback is not a function");
                return;
            }
            PhoneGap.exec(successCallback, errorCallback, "LiveTiles", "deleteSecondaryTile", options);
        }
    }
});