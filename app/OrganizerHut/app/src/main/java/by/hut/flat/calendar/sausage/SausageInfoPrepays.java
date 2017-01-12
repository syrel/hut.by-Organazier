package by.hut.flat.calendar.sausage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Debt;
import by.hut.flat.calendar.core.DBAdapter.tables.Prepay;
import by.hut.flat.calendar.dialog.floating.select.interval.DialogFloatingSelectInterval;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.infobar.Info;


public class SausageInfoPrepays extends Info{

	private Context context;
	private Prepay prepayDB;
	private DBAdapter db;
	
	public SausageInfoPrepays(Context context) {
		super(context);
		this.context = context;
		db = new DBAdapter(this.context);
		db.open();
		prepayDB = new Prepay(db);
		init();
		db.close();
	}
	
	private void init(){
		
		final int[][] dData = prepayDB.getData();
		final int[] IntervalIDs = Utils.getRow(Utils.transpose(dData),Debt.INTERVAL_IID);
		
		if (dData.length == 0) {
			hide();
			return;
		}
		
		this.setInfoText(dData.length + " " + Utils.getString(context, R.string.sausage_infobar_prepay));
		this.setInfoTextSize(Config.INST.SAUSAGE.INFO_BAR_TEXT_SIZE);
		this.setBackgroundColor(0xffde0000);
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
