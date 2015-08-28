﻿/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 *
 * Default DateSlider which allows for an easy selection of a date
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package by.hut.flat.calendar.dialog.date;

import java.util.Calendar;

import by.hut.flat.calendar.R;

import android.content.Context;

public class DefaultDateSlider extends DateSlider {
	
	public DefaultDateSlider(Context context, OnDateSetListener l, Calendar calendar, 
			Calendar minTime, Calendar maxTime) {
        super(context, R.layout.dialog_datetime_slider, l, calendar, minTime, maxTime);
    }
	
    public DefaultDateSlider(Context context, OnDateSetListener l, Calendar calendar) {
        super(context, R.layout.dialog_datetime_slider, l, calendar);
    }
}
