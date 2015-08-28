package by.hut.flat.calendar.dialog.floating.select.interval;

import by.hut.flat.calendar.dialog.DialogFloating;
import by.hut.flat.calendar.widget.list.simple.IEntry;
import by.hut.flat.calendar.widget.list.simple.List.OnEntryClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DialogFloatingSelectInterval extends DialogFloating implements OnEntryClickListener {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.floating.select.interval.DialogFloatingSelectInterval";
	
	public static final String PARAM_INTERVAL_IDS = "dialog_floating_select_interval";
	
	private int[] IntervalIDs;
			
	public static void show(Activity context, View view,Bundle extras) {
		DialogFloating.show(context, DIALOG_FLOATING_SELECT_INTERVAL,view, extras,DialogFloatingSelectInterval.class);
	}
	
	@Override
	public void onInitElements() {
		IntervalIDs = extras.getIntArray(PARAM_INTERVAL_IDS);
		if (IntervalIDs == null || IntervalIDs.length == 0) {
			finish();return;
		}
		IntervalList list = new IntervalList(context,IntervalIDs);
		list.setOnEntryClickListener(this);
		this.getLayout().addView(list.getView());
	}

	@Override
	public void onInitButtons() {
		
	}

	@Override
	public void onEntryClickListener(IEntry entry) {
		IntervalEntry intervalEntry = (IntervalEntry) entry;
		Intent intent = new Intent();
		int IntervalID = intervalEntry.getIntervalID();
		if (IntervalID >= 0){
			intent.putExtra(PARAM_INTERVAL_ID, IntervalID);
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

}
