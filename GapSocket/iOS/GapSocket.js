



function GapSocket(host,port)
{
	// Callback funx
	this.onopen = null;
	this.onmessage = null;
	this.onerror = null;
	this.onclose = null;
	
	this.host = host;
	this.port = port;
	this.sockId = (++GapSocket.nextIndex);
	
	GapSocket.Sockets[this.sockId] = this;
	this.bufferedAmount = 0;
	this.readyState = GapSocket.CONNECTING;
	
	PhoneGap.exec("GapSocketCommand.connect",this.host,this.port,this.sockId);
	
}

GapSocket.CONNECTING	= 0;
GapSocket.OPEN			= 1;
GapSocket.CLOSING		= 2;
GapSocket.CLOSED		= 3;


// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onOpen = function(sockId)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.readyState = GapSocket.OPEN;
		if(sock.onopen != null)
		{
			sock.onopen();
		}
	}
}

// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onConnecting = function(sockId)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.readyState = GapSocket.CONNECTING;
	}
}

// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onClosing = function(sockId)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.readyState = GapSocket.CLOSING;
	}
}

// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onClosed = function(sockId)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.readyState = GapSocket.CLOSED;
		sock.onclose();
		delete GapSocket.Sockets[sock.sockId];
		sock = null;
		
	}
}

// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onError = function(sockId,errMsg)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.onerror(errMsg);
	}
}

// Static Callback for ALL sockets
// sockId is used to route to the correct socket
GapSocket.__onMessage = function(sockId,msg)
{
	var sock = GapSocket.Sockets[sockId];
	if(sock != null)
	{
		sock.onmessage(msg);
	}
}


GapSocket.Sockets = {};
GapSocket.nextIndex = -1;

GapSocket.prototype.send = function(data)
{
	PhoneGap.exec("GapSocketCommand.send",this.sockId,data + "\r\n");
}

GapSocket.prototype.close = function()
{
	this.readyState = GapSocket.CLOSING;
}











/*
PhoneGap.addConstructor(function() {
    if (typeof navigator.network == "undefined") navigator.network = new Network();
});
 */
