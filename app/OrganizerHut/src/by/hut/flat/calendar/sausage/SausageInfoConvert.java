package by.hut.flat.calendar.sausage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.dialog.floating.select.interval.DialogFloatingSelectInterval;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.infobar.Info;

public class SausageInfoConvert extends Info{

	private Context context;
	private DBAdapter db;
	private Interval intervalDB;
	
	public SausageInfoConvert(Context context) {
		super(context);
		this.context = context;
		db = new DBAdapter(this.context);
		db.open();
		intervalDB = new Interval(db);
		init();
		db.close();
	}

	private void init(){
		final int[][] iData = intervalDB.findNotConvertedIntervals(new Date());
		final int[] IntervalIDs = Utils.getRow(Utils.transpose(iData),Interval.IID);
		
		if (iData.length == 0) {
			hide();
			return;
		}

		this.setInfoText(iData.length + " " + Utils.getString(context, R.string.sausage_infobar_convert));
		this.setInfoTextSize(Config.INST.SAUSAGE.INFO_BAR_TEXT_SIZE);
		this.setBackgroundColor(0xff16d716);
		this.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Bundle extras = new Bundle();
				extras.putIntArray(DialogFloatingSelectInterval.PARAM_INTERVAL_IDS,IntervalIDs);
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
