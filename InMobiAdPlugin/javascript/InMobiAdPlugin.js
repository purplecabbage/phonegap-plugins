/**
 * Constructor
 */
function InMobiAdPlugin()
{
	// Currently unused ... jm
	this.metaInfo = 
	{
		isLocationInquiryAllowed:false,
		currentLocation:null,
		testMode:false,
		postalCode:"",
		areaCode:"",
		dateOfBirth:"",
		gender:InMobiAdPlugin.Gender.None,
		keywords:"",
		searchString:"",
		income:-1,
		education:InMobiAdPlugin.Education.None,
		ethnicity:InMobiAdPlugin.Ethnicity.None,
		age:-1,
		interests:""
	};
}

// Enums
InMobiAdPlugin.Gender = 
{
	None:0,
	M:1,
	F:2
};

InMobiAdPlugin.Ethnicity = 
{
	None:0,
	Mixed:1,
	Asian:2,
	Black:3,
	Hispanic:4,
	NativeAmerican:5,
	White:6,
	Other:7
};

InMobiAdPlugin.Education = 
{
	None:0,
	HighSchool:1,
	SomeCollege:2,
	InCollege:3,
	BachelorsDegree:4,
	MastersDegree:5,
	DoctoralDegree:6,
	Other:7
};

InMobiAdPlugin.prototype = 
{
    /**
     * show - true to show the ad, false to hide the ad
     */
    showAd:function(bShow)
    {
        PhoneGap.exec("InMobiAdPlugin.showAd", !!bShow);
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
};

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