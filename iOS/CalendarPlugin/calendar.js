//
// Cordova Calendar Plugin
// Author: Felix Montanez 
// Created: 01-17-2012
//
// Contributors:
// Michael Brooks


function calendarPlugin()
{
}



calendarPlugin.prototype.createEvent = function(title,location,notes,startDate,endDate,reminderMinutes,success,fail) {
    console.log("calendarPlugin.createEvent");
    cordova.exec(success,fail,"calendarPlugin","createEvent", [title,location,notes,startDate,endDate,reminderMinutes]);
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
