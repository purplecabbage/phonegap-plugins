This plugin allows you to leave the PhoneGap webview and enter into the native android date and time picker. 
Once the user has selected time or date, they will be sent back into the PhoneGap webview with selected value available.


How to use:

plugins.DatePicker.exec(options, callback);

Options is an object with the following fields: 
  date: A javascript Date used to set the initial date and time. Defaults to now. 
  mode: Can be 'time', 'date', or 'datetime'. If 'time', a timepicker is displayed, if 'date', a datepicker is displayed, if 'datetime', displays a datepicker first, then a timepicker. Defaults (including arbitrary strings not 'date' or 'time' will use 'datetime').
  
The callback should expect a javascript time object. 

Example:

	document.querySelector("#mypickdatebutton").addEventListener("tap", function() {
	    window.plugins.datePickerPlugin.showDateOrTime(
			'date',
			function(time){
				document.getElementById("mydatetargetfield").value = time.getDay() + "/" + time.getMonth() + "/" + time.getYear();
			},
			function(e){console.log(e);}
		);
    }, false);
	
	document.querySelector("#mypickdatebutton").addEventListener("tap", function() {
	    window.plugins.datePickerPlugin.showDateOrTime(
			'time',
			function(time){
				document.getElementById("mytimetargetfield").value = time.getHour() + "h" + time.getMinute();
			},
			function(e){
				console.log(e);
			}
		);
    }, false);
    
   

For the current files to work, you'll need to create a package (folders) called com.ngapplication.plugin. 
You can change this to whatever you like, just update the datePickerPlugin.js and datePickerPlugin.java.

datePickerPlugin.js should go in the asset folder and should be referenced in your index.html file.
