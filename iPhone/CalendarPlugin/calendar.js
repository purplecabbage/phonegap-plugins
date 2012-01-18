//
// PhoneGap Calendar Plugin
// Author: Felix Montanez 
// Created: 01-17-2012
//

function calendarPlugin()
{
};


calendarPlugin.prototype.createEvent = function() {
    PhoneGap.exec('calendarPlugin.createEvent');
};


calendarPlugin.install = function()
{
    if(!window.plugins)
    {
        window.plugins = {};
    }
    
    window.plugins.calendarPlugin = new calendarPlugin();
    return window.plugins.calendarPlugin;
};

PhoneGap.addConstructor(calendarPlugin.install);
