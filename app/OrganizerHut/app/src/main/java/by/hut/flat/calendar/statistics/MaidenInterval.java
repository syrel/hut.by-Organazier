package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class MaidenInterval extends TitledTableLayout {
	
	public MaidenInterval(Context context, Date dateFrom, Date dateTo, Cleanings cleaningsDB, String[][] maidenData, String title) {
		super(context);
		init(dateFrom, dateTo, cleaningsDB, maidenData,title);
	}

	private void init(Date dateFrom, Date dateTo, Cleanings cleaningsDB,String[][] maidenData,String title) {
		this.setTitleText(title);
		addRow(new String[]{"Имя","Уборок"}).setBold(true);
		for (int i = 0,length = maidenData.length; i < length;i++){
			int cleanings = cleaningsDB.getCleaningsNum(Utils.Int(maidenData[i][Maiden.EID]),dateFrom,dateTo,Config.INST.SYSTEM.TODAY);
			addRow(new String[]{maidenData[i][Maiden.EMPLOYEE_FIO],""+cleanings});
		}
	}

}
