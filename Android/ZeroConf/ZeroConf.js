/**
 * ZeroConf plugin for Cordova/Phonecordova
 *
 * @author Matt Kane
 * Copyright (c) Triggertrap Ltd. 2012. All Rights Reserved.
 * Available under the terms of the MIT License.
 *
 */
var ZeroConf = function() { };

ZeroConf.prototype.watch = function(type, callback) {
    return cordova.exec(function(result) {
        if(callback) {
            callback(result);
        }

    }, ZeroConf.fail, "ZeroConf", "watch", [type]);
};

ZeroConf.prototype.list = function(type, timeout, callback) {
    return cordova.exec(function(result) {
        if(callback) {
            callback(result);
        }

    }, ZeroConf.fail, "ZeroConf", "list", [type,timeout]);
};

ZeroConf.prototype.unwatch = function(type) {
    return cordova.exec(null, ZeroConf.fail, "ZeroConf", "unwatch", [type]);
};

ZeroConf.prototype.close = function() {
    return cordova.exec(null, ZeroConf.fail, "ZeroConf", "close", []);
};

ZeroConf.prototype.register = function(type, name, port, text) {
    if(!type) {
        console.error("'type' is a required field");
        return;
    }
    return cordova.exec(null, ZeroConf.fail, "ZeroConf", "register", [type, name, port, text]);
};

ZeroConf.prototype.unregister = function() {
    return cordova.exec(null, ZeroConf.fail, "ZeroConf", "unregister", []);
};

ZeroConf.prototype.fail = function (o) {
    console.error("Error " + JSON.stringify(o));
};


if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.ZeroConf) {
    window.plugins.ZeroConf = new ZeroConf();
}
