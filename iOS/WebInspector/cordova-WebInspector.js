// Licensed under the Apache License, Version 2.0. See footer for details
   
// lurvely boilerplate
;(function(){

var PLUGIN_ID = "org.apache.cordova.WebInspector"

//------------------------------------------------------------------------------
function onDeviceReady() {
    cordova.exec(null, null, PLUGIN_ID, "enable", [])
}

//------------------------------------------------------------------------------
function onLoad() {
    document.addEventListener("deviceready", onDeviceReady, false)
}

//------------------------------------------------------------------------------
window.addEventListener("load", onLoad, false)

// lurvely boilerplate
})();

//------------------------------------------------------------------------------
// Copyright 2012 IBM
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//    http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//------------------------------------------------------------------------------
