/* Copyright (c) 2011 - Andago
 * 
 * author: Daniel Tizón
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



function bluetooth() { 
}


/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.listDevices = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'listDevices', [argument]);       
};



/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.isBTEnabled = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'isBTEnabled', [argument]);       
};



/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.enableBT = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'enableBT', [argument]);       
};



/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.disableBT = function(argument,successCallback, failureCallback) {

    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'disableBT', [argument]);       
};


/**
 * @param argument Argument that we are going to pass to the plugin, you need pass the MAC address of the mobile with wich you want to pair
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.pairBT = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'pairBT', [argument]);    
};



/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.listBoundDevices = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'listBoundDevices', [argument]);      
};



/**
 * @param argument Argument that we are going to pass to the plugin - for this method no arguments are needed
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.stopDiscovering = function(argument,successCallback, failureCallback) {

    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'stopDiscovering', [argument]);      
};


/**
 * @param argument Argument that we are going to pass to the plugin, you need pass the MAC address of the mobile that you want to know if it is bound
 * @param successCallback The callback which will be called when listDevices is successful
 * @param failureCallback The callback which will be called when listDevices encouters an error
 */
bluetooth.prototype.isBound = function(argument,successCallback, failureCallback) {
	
    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'isBound', [argument]);   
};



 window.plugins.bluetooth = new bluetooth();

