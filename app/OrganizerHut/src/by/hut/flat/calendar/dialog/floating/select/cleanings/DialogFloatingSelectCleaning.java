package by.hut.flat.calendar.dialog.floating.select.cleanings;

import by.hut.flat.calendar.dialog.DialogFloating;
import by.hut.flat.calendar.widget.list.simple.IEntry;
import by.hut.flat.calendar.widget.list.simple.List.OnEntryClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DialogFloatingSelectCleaning extends DialogFloating implements OnEntryClickListener {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.floating.select.cleanings.DialogFloatingSelectCleaning";
	
	public static final String PARAM_INTERVAL_IDS = "dialog_floating_select_cleanings";
				
	public static void show(Activity context, View view,Bundle extras) {
		DialogFloating.show(context, DIALOG_FLOATING_SELECT_CLEANINGS,view, extras,DialogFloatingSelectCleaning.class);
	}
	
	@Override
	public void onInitElements() {
		CleaningList list = new CleaningList(context);
		list.setOnEntryClickListener(this);
		this.getLayout().addView(list.getView());
	}

	@Override
	public void onEntryClickListener(IEntry entry) {
		CleaningEntry cleaningEntry = (CleaningEntry) entry;
		Intent intent = new Intent();
		int CleaningID = cleaningEntry.getCleaningID();
		if (CleaningID >= 0){
			intent.putExtra(PARAM_CLEANING_ID, CleaningID);
			setResult(RESULT_OK, intent);
			finish();
		}
		else {
			finish();
		}
	}

	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}

	@Override public void onInitButtons() {}
}
