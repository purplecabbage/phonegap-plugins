/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2011, Nitobi Software Inc.
 * Copyright (c) 2011, Microsoft Corporation
 */

using System.Runtime.Serialization;
using WP7GapClassLib.PhoneGap;
using WP7GapClassLib.PhoneGap.Commands;
using WP7GapClassLib.PhoneGap.JSON;
using Microsoft.Phone.Shell;
using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Phone.Controls;
using System.Windows;
using Microsoft.Phone.Tasks;

namespace PhoneGap.Extension.Commands
{
    /// <summary>
    /// Represents command that allows the user to choose a date (day/month/year) or time (hour/minute/am/pm).
    /// </summary>
    public class DateTimePicker : BaseCommand
    {

        #region DateTimePicker Options

        /// <summary>
        /// Represents DateTimePicker options
        /// </summary>
        [DataContract]
        public class DateTimePickerOptions
        {
            /// <summary>
            /// Initial value for time or date
            /// </summary>
            [DataMember(IsRequired = false, Name = "value")]
            public DateTime Value { get; set; }   
         
                        /// <summary>
            /// Creates options object with default parameters
            /// </summary>
            public DateTimePickerOptions()
            {
                this.SetDefaultValues(new StreamingContext());
            }

            /// <summary>
            /// Initializes default values for class fields.
            /// Implemented in separate method because default constructor is not invoked during deserialization.
            /// </summary>
            /// <param name="context"></param>
            [OnDeserializing()]
            public void SetDefaultValues(StreamingContext context)
            {
                this.Value = DateTime.Now;
            }

        }
        #endregion

        /// <summary>
        /// Used to open datetime picker
        /// </summary>
        private DateTimePickerTask dateTimePickerTask;

        /// <summary>
        /// DateTimePicker options
        /// </summary>
        private DateTimePickerOptions dateTimePickerOptions;

        /// <summary>
        /// Triggers  special UI to select a date (day/month/year)
        /// </summary>
        public void selectDate(string options)
        {

            try
            {
                try
                {
                    this.dateTimePickerOptions = String.IsNullOrEmpty(options) ? new DateTimePickerOptions() : 
                        WP7GapClassLib.PhoneGap.JSON.JsonHelper.Deserialize<DateTimePickerOptions>(options);

                }
                catch (Exception ex)
                {
                    this.DispatchCommandResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, ex.Message));
                    return;
                }

                this.dateTimePickerTask = new DateTimePickerTask();
                dateTimePickerTask.Value = dateTimePickerOptions.Value;

                dateTimePickerTask.Completed += this.dateTimePickerTask_Completed;
                dateTimePickerTask.Show(DateTimePickerTask.DateTimePickerType.Date);
            }
            catch (Exception e)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, e.Message));
            }
        }

        /// <summary>
        /// Triggers  special UI to select a time (hour/minute/am/pm).
        /// </summary>
        public void selectTime(string options)
        {

            try
            {
                try
                {
                    this.dateTimePickerOptions = String.IsNullOrEmpty(options) ? new DateTimePickerOptions() :
                        WP7GapClassLib.PhoneGap.JSON.JsonHelper.Deserialize<DateTimePickerOptions>(options);

                }
                catch (Exception ex)
                {
                    this.DispatchCommandResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, ex.Message));
                    return;
                }

                this.dateTimePickerTask = new DateTimePickerTask();
                dateTimePickerTask.Value = dateTimePickerOptions.Value;

                dateTimePickerTask.Completed += this.dateTimePickerTask_Completed;
                dateTimePickerTask.Show(DateTimePickerTask.DateTimePickerType.Time);
            }
            catch (Exception e)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, e.Message));
            }
        }

        /// <summary>
        /// Handles datetime picker result
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e">stores information about current captured image</param>
        private void dateTimePickerTask_Completed(object sender, DateTimePickerTask.DateTimeResult e)
        {

            if (e.Error != null)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR));
                return;
            }            

            switch (e.TaskResult)
            {
                case TaskResult.OK:
                    try
                    {
                        DispatchCommandResult(new PluginResult(PluginResult.Status.OK, e.Value.Value.ToString()));
                    }
                    catch (Exception ex)
                    {
                        DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, "Datetime picker error. " + ex.Message));
                    }
                    break;

                case TaskResult.Cancel:
                    DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, "Canceled."));
                    break;               
            }

            this.dateTimePickerTask = null;
        }       

    }
}