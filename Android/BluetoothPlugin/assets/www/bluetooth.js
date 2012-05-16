/*
   Copyright 2012 Wolfgang Koller - http://www.gofg.at/

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

if( !PhoneGap.hasResource("BluetoothPlugin") ) {
	PhoneGap.addResource("BluetoothPlugin");

	/**
	 * @returns instance of powermanagement
	 */
	var Bluetooth = function() {	
	}
	
	/**
	 * Enable bluetooth
	 * 
	 * @param successCallback function to be called when enabling of bluetooth was successfull
	 * @param errorCallback function to be called when enabling was not possible / did fail
	 */
	Bluetooth.prototype.enable = function(successCallback,failureCallback) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'enable', []);
	}
	
	/**
	 * Disable bluetooth
	 * 
	 * @param successCallback function to be called when disabling of bluetooth was successfull
	 * @param errorCallback function to be called when disabling was not possible / did fail
	 */
	Bluetooth.prototype.disable = function(successCallback,failureCallback) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'disable', []);
	}
	
	/**
	 * Search for devices  and list them
	 * 
	 * @param successCallback function to be called when discovery of other devices has finished. Passed parameter is a JSONArray containing JSONObjects with 'name' and 'address' property.
	 * @param errorCallback function to be called when there was a problem while discovering devices
	 */
	Bluetooth.prototype.discoverDevices = function(successCallback,failureCallback) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'discoverDevices', []);
	}
	
	/**
	 * Return list of available UUIDs for a given device
	 * 
	 * @param successCallback function to be called when listing of UUIDs has finished. Passed parameter is a JSONArray containing strings which represent the UUIDs
	 * @param errorCallback function to be called when there was a problem while listing UUIDs
	 */
	Bluetooth.prototype.getUUIDs = function(successCallback,failureCallback,address) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'getUUIDs', [address]);
	}
	
	/**
	 * Open an RFComm channel for a given device & uuid endpoint
	 * 
	 * @param successCallback function to be called when the connection was successfull. Passed parameter is an integer containing the socket id for the connection
	 * @param errorCallback function to be called when there was a problem while opening the connection
	 */
	Bluetooth.prototype.connect = function(successCallback,failureCallback,address,uuid) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'connect', [address, uuid]);
	}
	
	/**
	 * Read from a connected socket
	 * 
	 * @param successCallback function to be called when reading was successfull. Passed parameter is a string containing the read content
	 * @param errorCallback function to be called when there was a problem while reading
	 */
	Bluetooth.prototype.read = function(successCallback,failureCallback,socketid) {
	    return PhoneGap.exec(successCallback, failureCallback, 'BluetoothPlugin', 'read', [socketid]);
	}
	
	/**
	 * Register the plugin with PhoneGap
	 */
	PhoneGap.addConstructor(function() {
		// Register the PowerManagement plugin with PhoneGap
		PhoneGap.addPlugin('BluetoothPlugin', new Bluetooth());
	});
}
