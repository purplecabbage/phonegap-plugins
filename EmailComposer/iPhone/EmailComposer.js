
// window.plugins.emailComposer
													  
function EmailComposer()
{
	this.resultCallback = null; // Function
}

EmailComposer.ComposeResultType =
{
	Cancelled:0,
	Saved:1,
	Sent:2,
	Failed:3,
	NotSent:4
}



// showEmailComposer : all args optional

EmailComposer.prototype.showEmailComposer = function(subject,body,toRecipients,ccRecipients,bccRecipients)
{
	var args = {};
	if(toRecipients)
		args.toRecipients = toRecipients;
	if(ccRecipients)
		args.ccRecipients = ccRecipients;
	if(bccRecipients)
		args.bccRecipients = bccRecipients;
	if(subject)
		args.subject = subject;
	if(body)
		args.body = body;
	
	PhoneGap.exec("EmailComposer.showEmailComposer",args);
}

EmailComposer.prototype._didFinishWithResult = function(res)
{
	this.resultCallback(res);
}



PhoneGap.addConstructor(function() 
{
	if(!window.plugins)
	{
		window.plugins = {};
	}
	window.plugins.emailComposer = new EmailComposer();
});
