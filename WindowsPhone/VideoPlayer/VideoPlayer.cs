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

using System;
using System.IO;
using System.IO.IsolatedStorage;
using System.Windows;
using System.Runtime.Serialization;
using Microsoft.Phone.Tasks;
using WP7CordovaClassLib.Cordova;
using WP7CordovaClassLib.Cordova.Commands;
using WP7CordovaClassLib.Cordova.JSON;


namespace WP7CordovaClassLib.Cordova.Commands
{
    public class VideoPlayer : BaseCommand
    {
        private MediaPlayerLauncher mediaPlayer;

        [DataContract]
        public class VideoPlayerOptions
        {
            [DataMember]
            public string url;
        }

        public void playVideo(string options)
        {
            VideoPlayerOptions opts = JSON.JsonHelper.Deserialize<VideoPlayerOptions>(options);

            Uri uri = new Uri(opts.url);

            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                try
                {
                    this.play(opts.url);

                    DispatchCommandResult(new PluginResult(PluginResult.Status.OK));
                }
                catch (Exception e)
                {
                    DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, e.Message));
                }
            });
        }

        private void play(string url)
        {
            this.mediaPlayer = new MediaPlayerLauncher();

            Uri uri = new Uri(url, UriKind.RelativeOrAbsolute);

            if (uri.IsAbsoluteUri)
            {
                this.mediaPlayer.Media = uri;
            }
            else
            {
                using (IsolatedStorageFile isoFile = IsolatedStorageFile.GetUserStoreForApplication())
                {
                    if (isoFile.FileExists(url))
                    {
                        this.mediaPlayer.Location = MediaLocationType.Data;
                        this.mediaPlayer.Media = uri;
                    }
                    else
                    {
                        throw new ArgumentException("Media location doesn't exists.");
                    }
                }
            }

            this.mediaPlayer.Show();
        }
    }
}
