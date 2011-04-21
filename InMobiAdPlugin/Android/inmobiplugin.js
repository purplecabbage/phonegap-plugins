var InMobi = function()
{
}

InMobi.prototype.init = function(success, fail, options)
{
    console.log('InMobi: Load the component');
    return PhoneGap.exec(function(args) {
        success(args);
    }, 
    function(args)
    {
        fail(args);
    }, 'InMobiPlugin', 'load', []);
}

InMobi.prototype.hide = function(success, fail, options)
{
}

InMobi.prototype.refresh = function(success, fail, options)
{
}

PhoneGap.addConstructor(function() {
    console.log('InMobi: Adding Constructor');
    PhoneGap.addPlugin('inMobi', new InMobi());
    navigator.app.addService('InMobiPlugin', 'com.phonegap.inmobi.InMobiPlugin');
});
