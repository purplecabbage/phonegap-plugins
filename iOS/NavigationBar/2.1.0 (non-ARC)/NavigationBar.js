function NavigationBar() {
    this.leftButtonCallback = null;
    this.rightButtonCallback = null;
}

/**
 * Create a navigation bar.
 *
 * @param style: One of "BlackTransparent", "BlackOpaque", "Black" or "Default". The latter will be used if no style is given.
 */
NavigationBar.prototype.create = function(style, options)
{
    options = options || {};
    if(!("style" in options))
        options.style = style || "Default";

    cordova.exec("NavigationBar.create", options);
};

/**
 * Must be called before any other method in order to initialize the plugin.
 */
NavigationBar.prototype.init = function()
{
    cordova.exec("NavigationBar.init");
};

NavigationBar.prototype.resize = function() {
    cordova.exec("NavigationBar.resize");
};

/**
 * Assign either title or image to the left navigation bar button, and assign the tap callback
*/
NavigationBar.prototype.setupLeftButton = function(title, image, onselect, options)
{
    this.leftButtonCallback = onselect;
    cordova.exec("NavigationBar.setupLeftButton", title || "", image || "", options || {});
};

/**
 * @param options: May contain the key "animated" (boolean)
 */
NavigationBar.prototype.hideLeftButton = function(options)
{
    options = options || {}
    if(!("animated" in options))
        options.animated = false

    cordova.exec("NavigationBar.hideLeftButton", options)
};

NavigationBar.prototype.setLeftButtonTitle = function(title)
{
    cordova.exec("NavigationBar.setLeftButtonTitle", title)
};

NavigationBar.prototype.showLeftButton = function(options)
{
    options = options || {}
    if(!("animated" in options))
        options.animated = false

    cordova.exec("NavigationBar.showLeftButton", options)
};

/**
 * Internal function called by the plugin
 */
NavigationBar.prototype.leftButtonTapped = function()
{
    if(typeof(this.leftButtonCallback) === "function")
        this.leftButtonCallback()
};

/**
 * Assign either title or image to the right navigation bar button, and assign the tap callback
*/
NavigationBar.prototype.setupRightButton = function(title, image, onselect, options)
{
    this.rightButtonCallback = onselect;
    cordova.exec("NavigationBar.setupRightButton", title || "", image || "", options || {});
};


NavigationBar.prototype.hideRightButton = function(options)
{
    options = options || {}
    if(!("animated" in options))
        options.animated = false

    cordova.exec("NavigationBar.hideRightButton", options)
};

NavigationBar.prototype.setRightButtonTitle = function(title)
{
    cordova.exec("NavigationBar.setRightButtonTitle", title)
};

NavigationBar.prototype.showRightButton = function(options)
{
    options = options || {}
    if(!("animated" in options))
        options.animated = false

    cordova.exec("NavigationBar.showRightButton", options)
};

/**
 * Internal function called by the plugin
 */
NavigationBar.prototype.rightButtonTapped = function()
{
    if(typeof(this.rightButtonCallback) === "function")
        this.rightButtonCallback()
};

NavigationBar.prototype.setTitle = function(title)
{
    cordova.exec("NavigationBar.setTitle", title);
};

NavigationBar.prototype.setLogo = function(imageURL)
{
    cordova.exec("NavigationBar.setLogo", imageURL);
};

/**
 * Shows the navigation bar. Make sure you called create() first.
 */
NavigationBar.prototype.show = function() {
    cordova.exec("NavigationBar.show");
};

/**
 * Hides the navigation bar. Make sure you called create() first.
 */
NavigationBar.prototype.hide = function() {

    cordova.exec("NavigationBar.hide");
};

cordova.addConstructor(function()
{
    if(!window.plugins)
        window.plugins = {}
    window.plugins.navigationBar = new NavigationBar()
});
