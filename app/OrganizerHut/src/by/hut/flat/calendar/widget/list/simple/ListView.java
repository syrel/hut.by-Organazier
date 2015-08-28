package by.hut.flat.calendar.widget.list.simple;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class ListView extends LinearLayout{

	public ListView(Context context) {
		super(context);
		initView();
	}
	
	protected abstract void initView();

}
