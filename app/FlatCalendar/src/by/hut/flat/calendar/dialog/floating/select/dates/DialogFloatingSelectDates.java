package by.hut.flat.calendar.dialog.floating.select.dates;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalFlat;
import by.hut.flat.calendar.dialog.DialogAddCleaning;
import by.hut.flat.calendar.dialog.DialogFloating;
import by.hut.flat.calendar.dialog.date.DateSlider;
import by.hut.flat.calendar.dialog.date.DefaultDateSlider;
import by.hut.flat.calendar.dialog.date.SliderContainer;
import by.hut.flat.calendar.dialog.date.DateSlider.OnDateSetListener;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.list.simple.IEntry;
import by.hut.flat.calendar.widget.list.simple.List.OnEntryClickListener;

public class DialogFloatingSelectDates extends DialogFloating implements OnEntryClickListener {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.floating.select.dates.DialogFloatingSelectDates";
	public static final int DIALOG_DATE_FROM = 1;
	public static final int DIALOG_DATE_TO = 2;
	
	private int IntervalID;
	private DatesList list;
	public static void show(Activity context, View view,Bundle extras) {
		DialogFloating.show(context, DIALOG_FLOATING_SELECT_DATES,view, extras,DialogFloatingSelectDates.class);
	}
	
	@Override
	public void onInitElements() {
		IntervalID = extras.getInt(PARAM_INTERVAL_ID);
		if (IntervalID <= 0) {
			finish();return;
		}
		list = new DatesList(context, IntervalID);
		list.setOnEntryClickListener(this);
		this.getLayout().addView(list.getView());
	}

	
	@Override
	public void onInitButtons() {}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntryClickListener(IEntry entry) {
		DatesEntry datesEntry = (DatesEntry)entry;
		final int type = datesEntry.getID();
		switch(type){
			case DatesList.DATE_FROM:{
				this.showDialog(DIALOG_DATE_FROM);
				break;
			}
			case DatesList.DATE_TO:{
				this.showDialog(DIALOG_DATE_TO);
				break;
			}
		}
	}
	
	
	/*------------------------------------------------------------
	------------------- D A T E S   D I A L O G ------------------
	------------------------------------------------------------*/
	private OnDateSetListener sDateSetListener = new OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			Date newDateFrom = new Date(String.format("%tF",selectedDate));
			new DatesFromTask(context, IntervalID, newDateFrom).execute();
			finish();
		}
	};
	private OnDateSetListener fDateSetListener = new OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			Date newDateTo = new Date(String.format("%tF",selectedDate));
			new DatesToTask(context, IntervalID, newDateTo).execute();
			DBAdapter db = new DBAdapter(context);
			db.open();
			IntervalFlat intervalFlatDB = new IntervalFlat(db);
			int FlatID = intervalFlatDB.getData(IntervalID)[IntervalFlat.FID];
			
			DialogAddCleaning.show(
					context,
					new String[]{
						by.hut.flat.calendar.dialog.Dialog.PARAM_FLAT_ID,
						by.hut.flat.calendar.dialog.Dialog.PARAM_DATE,
						by.hut.flat.calendar.dialog.Dialog.PARAM_INTERVAL_ID
					},
					new String[]{
							""+FlatID,
							newDateTo.toString(),
							""+IntervalID
							}
			);
			finish();
		}
	};
	
	@Override
	public Dialog onCreateDialog(int dialogId) {
		Dialog dialog = null;
	    switch (dialogId) {
	       case DIALOG_DATE_FROM:
	    	   dialog = new DefaultDateSlider(context,sDateSetListener,Calendar.getInstance());
	    	   break;
	       case DIALOG_DATE_TO:
               dialog = new DefaultDateSlider(context,fDateSetListener,Calendar.getInstance());
               break;
	    }
	    return dialog;
	}
	@Override
	public void onPrepareDialog(int dialogId, Dialog dialog) {
		if (dialogId == DIALOG_DATE_FROM){
			if (list.getEntry(DatesList.DATE_FROM) == null || list.getEntry(DatesList.DATE_TO) == null) return;
			SliderContainer slider = (SliderContainer)dialog.findViewById(R.id.dateSliderContainer);
			
			Calendar c = Calendar.getInstance();
			Calendar maxDate = Calendar.getInstance();
			Date dateFrom = list.getEntry(DatesList.DATE_FROM).getDate();
			Date dateTo = list.getEntry(DatesList.DATE_TO).getDate().prev();
			
			maxDate.set(dateTo.year,dateTo.month-1,dateTo.day);
			c.set(dateFrom.year,dateFrom.month-1,dateFrom.day);
			
			slider.setTime(c);
			slider.setMaxTime(maxDate);
		}
		else if (dialogId == DIALOG_DATE_TO){
			if (list.getEntry(DatesList.DATE_FROM) == null || list.getEntry(DatesList.DATE_TO) == null) return;
			SliderContainer slider = (SliderContainer)dialog.findViewById(R.id.dateSliderContainer);
			
			Calendar c = Calendar.getInstance();
			Calendar minDate = Calendar.getInstance();
			Date dateFrom = list.getEntry(DatesList.DATE_FROM).getDate().next();
			Date dateTo = list.getEntry(DatesList.DATE_TO).getDate();
			
			minDate.set(dateFrom.year,dateFrom.month-1,dateFrom.day);
			c.set(dateTo.year,dateTo.month-1,dateTo.day);
			
			slider.setTime(c);
			slider.setMinTime(minDate);
		}
	}

}
