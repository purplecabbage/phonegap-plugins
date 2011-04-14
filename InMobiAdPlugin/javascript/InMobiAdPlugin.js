/**
 * Constructor
 */
function InMobiAdPlugin(){}

InMobiAdPlugin.prototype = 
{
    /**
     * show - true to show the ad, false to hide the ad
     */
    showAd:function(bShow)
    {
        PhoneGap.exec("InMobiAdPlugin.showAd", bShow);
    },
    
    /**
     * adOptions.atBottom - true to put the ad at the bottom, false to put the ad at the top
     * default value is false
     */
    init:function(siteId,adOptions)
    {
        adOptions = adOptions || {};
        if(!adOptions.atBottom)
        {
            adOptions.atBottom = false;
        }
    	PhoneGap.exec("InMobiAdPlugin.init",siteId, adOptions);
    }
}

/**
 * Install function
 */
InMobiAdPlugin.install = function()
{
	if ( !window.plugins ) 
	{
		window.plugins = {}; 
	}
	if ( !window.plugins.inMobiAdPlugin ) 
	{
		window.plugins.inMobiAdPlugin = new InMobiAdPlugin();
	}
}

/**
 * Add to PhoneGap constructor
 */
PhoneGap.addConstructor(InMobiAdPlugin.install);