package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.widget.layout.CollapsableLayout;

public class StatisticsProfit extends CollapsableLayout{

	public StatisticsProfit(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		this.setTitleText("Доход");
		HiddableProfitWeeks profitWeeks = new HiddableProfitWeeks(getContext());
		profitWeeks.show();
		this.setHiddable(profitWeeks);
		this.setLongHiddable(new HiddableProfitAllYears(getContext()));
	}
}