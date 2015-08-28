package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.widget.layout.CollapsableLayout;

public class CollapsableMaidenAllTime extends CollapsableLayout{

	public CollapsableMaidenAllTime(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		this.setTitleText("Уборки за все время");
		this.setHiddable(new HiddableMaidenAllTime(getContext()));
	}

}
