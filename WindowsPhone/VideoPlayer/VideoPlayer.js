/*
 * Copyright (C) 2011 by Emergya
 *
 * Author: Alejandro Leiva <aleiva@emergya.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
*/

/**
 * Constructor
 */
function VideoPlayer() {
};

/**
 * Define method VideoPlayer::play().
 */
VideoPlayer.prototype.play = function(successCallback, errorCallback, options) {

	if (successCallback && (typeof successCallback !== "function")) {
		console.log("VideoPlayer Error: successCallback is not a function");
		return;
	}

	if (errorCallback && (typeof errorCallback !== "function")) {
		console.log("VideoPlayer Error: errorCallback is not a function");
		return;
	}

	PhoneGap.exec(sucessCallback, errorCallback, "VideoPlayer", "playVideo", options);
};

/**
 * Load VideoPlayer
 */
PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("VideoPlayer", new VideoPlayer());
});
