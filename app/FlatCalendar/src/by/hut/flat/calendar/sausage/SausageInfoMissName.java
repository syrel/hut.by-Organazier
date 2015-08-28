package by.hut.flat.calendar.sausage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.dialog.floating.select.interval.DialogFloatingSelectInterval;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.infobar.Info;

public class SausageInfoMissName extends Info{

	private Context context;
	private DBAdapter db;
	private IntervalAnketa intervaAnketalDB;
	
	public SausageInfoMissName(Context context) {
		super(context);
		this.context = context;
		db = new DBAdapter(this.context);
		db.open();
		intervaAnketalDB = new IntervalAnketa(db);
		init();
		db.close();
	}

	private void init(){
		final int[] intervalIDs = intervaAnketalDB.getIntervalIDsMissName();
		
		if (intervalIDs.length == 0) {
			hide();
			return;
		}

		this.setInfoText(intervalIDs.length + " " + Utils.getString(context, R.string.sausage_infobar_name));
		this.setInfoTextSize(Config.INST.SAUSAGE.INFO_BAR_TEXT_SIZE);
		this.setBackgroundColor(0xffff6500);
		this.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Bundle extras = new Bundle();
				extras.putIntArray(DialogFloatingSelectInterval.PARAM_INTERVAL_IDS,intervalIDs);
				DialogFloatingSelectInterval.show(getActivity(), view, extras);
			}
		});
		show();
	}
	
	@Override
	public void refresh() {
		db.open();
		init();
		db.close();
	}

}
