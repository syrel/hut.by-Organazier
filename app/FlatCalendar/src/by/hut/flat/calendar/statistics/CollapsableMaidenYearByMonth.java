package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.widget.layout.CollapsableLayout;

public class CollapsableMaidenYearByMonth extends CollapsableLayout{

	private Context context;
	private int year;
	
	public CollapsableMaidenYearByMonth(Context context,int year) {
		super(context);
		this.context = context;
		this.year = year;
		initView();
	}
	
	public void initView(){
		this.setTitleText("Уборки в "+year+"г.");
		this.setHiddable(new HiddableMaidenYearByMonth(context,year));
	}

}
