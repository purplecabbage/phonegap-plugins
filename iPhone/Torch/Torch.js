//
//  Torch.js
//  PhoneGap Plugin
//
//  Only supported in iOS4, and flash capable device (currently iPhone 4 only)
//
// Created by Shazron Abdullah May 26th 2011
//

function Torch() {
    this._isOn = false;
    this._checkForLight = false;
    var self = this;

    this.__defineGetter__("isOn", function () {
        return self._isOn;
    });

    this.__defineGetter__("checkForLight", function () {
        return self._checkForLight;
    });

}

Torch.prototype.turnOn = function()
{
	PhoneGap.exec("Torch.turnOn");
};

Torch.prototype.turnOff = function()
{
	PhoneGap.exec("Torch.turnOff");
};

Torch.prototype.checkLight = function () {
    PhoneGap.exec("Torch.checkLight");
};

Torch.install = function()
{
	if(!window.plugins) {
		window.plugins = {};
	}
	window.plugins.torch = new Torch();
};

PhoneGap.addConstructor(Torch.install);