package by.hut.flat.calendar.sausage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import by.hut.flat.calendar.anketa.AnketaActivity;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.widget.infobar.InfoBar;

public class SausageInfoBar extends InfoBar {
	
	private Context context;
	
	public SausageInfoBar(Context context) {
		this(context,null);
	}
	
	public SausageInfoBar(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public SausageInfoBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
		
	private void init(){
		SausageInfoMissPhoneName phoneName = new SausageInfoMissPhoneName(context);
		SausageInfoDebts debts = new SausageInfoDebts(context);
		SausageInfoPrepays prepays = new SausageInfoPrepays(context);
		SausageInfoWrongPayments payments = new SausageInfoWrongPayments(context);
		SausageInfoConvert converts = new SausageInfoConvert(context);
		this.addInfo(phoneName);
		this.addInfo(payments);
		this.addInfo(converts);
		this.addInfo(debts);
		this.addInfo(prepays);
	}
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return false;}
		switch (requestCode){
			case Dialog.DIALOG_FLOATING_SELECT_INTERVAL:{
				if (resultCode == Activity.RESULT_OK){
					int IntervalID = data.getExtras().getInt(Dialog.PARAM_INTERVAL_ID);
					AnketaActivity.show(context, IntervalID,true);
				}
				break;
			}
		}
		return true;
	}
	


}
