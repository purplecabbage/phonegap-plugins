Sample Use:
===============

&lt;script type="text/javascript" charset="utf-8" src="PGSocialShare.js"&gt;&lt;/script&gt


        function shareStatus()
        {
            navigator.plugins.pgSocialShare.shareStatus("This was shared from JS+PhoneGap+WP7 Yo!");
        }

        function shareLink()
        {
        navigator.plugins.pgSocialShare.shareLink("WP7 PhoneGap Plugins",
        "https://github.com/purplecabbage/phonegap-plugins/tree/master/WindowsPhone",
        "Watch for updates here soon! Shared from JavaScript");
        }



    &lt;input style="display:block;margin:40px 0px" type="button" onclick="shareLink()" value="Share a Link"/&gt;
    &lt;input style="display:block;margin:40px 0px" type="button" onclick="shareStatus()" value="Update your Status"/&gt;
