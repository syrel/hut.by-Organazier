package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.HiddableLayout;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class HiddableMaidenAllTime extends HiddableLayout {

	public DBAdapter db;
	private String[][] maidenData;
	private Maiden maidenDB;
	private Cleanings cleaningsDB;
	
	public HiddableMaidenAllTime(Context context) {
		super(context);
	}

	@Override
	protected void onPreInitialize() {
		db = new DBAdapter(getContext());
		db.open();
		maidenDB = new Maiden(db);
		cleaningsDB = new Cleanings(db);
	}

	@Override
	protected void doInInitialization() {
		maidenData = maidenDB.getData(true);
		initAll();
	}

	private void initAll(){
		TitledTableLayout table = new TitledTableLayout(getContext());
		table.setTitleText("Все уборки:");
		table.addRow(new String[]{"Имя","Уборок"}).setBold(true);
		
		for (int i = 0,length = maidenData.length; i < length; i++){
			int cleanings = cleaningsDB.getCleaningsNum(Utils.Int(maidenData[i][Maiden.EID]), Config.INST.SYSTEM.TODAY);
			table.addRow(new String[]{maidenData[i][Maiden.EMPLOYEE_FIO],""+cleanings});
		}
		addView(table);
	}
	
	@Override
	protected void onPostInitialize() {
		db.close();
	}

}
