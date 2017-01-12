package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.widget.layout.CollapsableLayout;

public class StatisticsMaiden extends CollapsableLayout {
	public static final int CLEANING_FIRST_DAY = 21;
	public static final int CLEANING_LAST_DAY = 20;
	
	public StatisticsMaiden(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		this.setTitleText("Уборки");
		HiddableMaidenCurrentPreviousMonths currentPrevious = new HiddableMaidenCurrentPreviousMonths(getContext());
		currentPrevious.show();
		this.setHiddable(currentPrevious);
		this.setLongHiddable(new HiddableMaidenAllYears(getContext()));
	}
}