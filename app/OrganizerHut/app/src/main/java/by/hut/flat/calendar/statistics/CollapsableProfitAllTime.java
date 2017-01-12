package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.widget.layout.CollapsableLayout;

public class CollapsableProfitAllTime extends CollapsableLayout{

	public CollapsableProfitAllTime(Context context) {
		super(context);
		init();
	}
	
	public void init(){
		this.setTitleText("Доход за все время");
		this.setHiddable(new HiddableProfitAllTime(getContext()));
	}
}
