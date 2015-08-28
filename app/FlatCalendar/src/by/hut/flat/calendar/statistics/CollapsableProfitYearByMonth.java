package by.hut.flat.calendar.statistics;

import by.hut.flat.calendar.widget.layout.CollapsableLayout;
import android.content.Context;

public class CollapsableProfitYearByMonth extends CollapsableLayout {
	
	private int year;
	
	public CollapsableProfitYearByMonth(Context context,int year) {
		super(context);
		this.year = year;
		initView();
	}
	
	public void initView(){
		this.setTitleText("Доход в "+year+"г.");
		this.setHiddable(new HiddableProfitYearByMonths(getContext(),year));
	}
}