//
// Cordova Calendar Plugin
// Author: Felix Montanez 
// Created: 01-17-2012
//
// Contributors:
// Michael Brooks, Trevor Cox


function calendarPlugin()
{
}



calendarPlugin.prototype.createEvent = function(title,location,notes,startDate,endDate,calendarName,reminderMinutes,success,fail) {
    console.log("calendarPlugin.createEvent");
    cordova.exec(success,fail,"calendarPlugin","createEvent", [title,location,notes,startDate,endDate,calendarName,reminderMinutes]);
};

calendarPlugin.prototype.getCalendarList = function(response, err) {
    console.log("getting calendars");
    cordova.exec(response, err, "calendarPlugin", "getCalendarList",[]);
};

// More methods will need to be added like fetch events, delete event, edit event

calendarPlugin.install = function()
{
    if(!window.plugins)
    {
        window.plugins = {};
    }
    
    window.plugins.calendarPlugin = new calendarPlugin();
    return window.plugins.calendarPlugin;
};

cordova.addConstructor(calendarPlugin.install);
