
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
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

EmailComposer.prototype.showEmailComposer = function(subject,body,toRecipients,ccRecipients,bccRecipients,bIsHTML)
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
	if(bIsHTML)
		args.bIsHTML = bIsHTML;

	PhoneGap.exec("com.phonegap.emailComposer.showEmailComposer",args);
}

// this will be forever known as the orch-func -jm
EmailComposer.prototype.showEmailComposerWithCB = function(cbFunction,subject,body,toRecipients,ccRecipients,bccRecipients,bIsHTML)
{
  this.resultCallback = cbFunction;
  this.showEmailComposer.apply(this,[subject,body,toRecipients,ccRecipients,bccRecipients,bIsHTML]);
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