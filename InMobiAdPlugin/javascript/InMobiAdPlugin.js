/**
 * Constructor
 */
function InMobiAdPlugin()
{
	this.metaInfo = 
	{
		// allow the plugin to request the user's location
		isLocationInquiryAllowed:false,
		// operate in testMode
		testMode:false,
		
		/* Context info to describe the current state of the application */

		// comma delimited string list of keywords to describe page contents
		keywords:null,
		// identify a particular state in your app for categorization ex. 'login' or 'settings'
		searchString:null,
		
		/* Optional Demographic info */
		
		postalCode:null,					// user's postal code
		areaCode:null,						// user's phone area code
		dateOfBirth:new Date(),				// user's date of birth
		gender:InMobiAdPlugin.Gender.None,	// user's gender
		income:null,						// user's income
		// user's education level
		education:InMobiAdPlugin.Education.None,
		// user's ethnicity
		ethnicity:InMobiAdPlugin.Ethnicity.None,
		// user's age
		age:null,
		// comma seperated list of keywords denoting user's area of interest. ex. "cars,sports,F1,stocks"
		interests:null,
	};
}

// Enums
InMobiAdPlugin.Gender = { None:0, M:1, F:2 };

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
		adOptions.meta = this.metaInfo;
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