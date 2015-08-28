package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.HiddableLayout;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class HiddableProfitAllTime extends HiddableLayout{

	public DBAdapter db;
	private Payment paymentDB;
	private Flat flatDB;
	private String[][] flatsData;
	
	public HiddableProfitAllTime(Context context) {
		super(context);
	}

	@Override
	protected void onPreInitialize() {
		db = new DBAdapter(getContext());
		db.open();
		paymentDB = new Payment(db);
		flatDB = new Flat(db);
	}

	@Override
	protected void doInInitialization() {
		flatsData = flatDB.getData(false);
		initAll();
	}
	
	private void initAll(){
		TitledTableLayout table = new TitledTableLayout(getContext());
		table.setTitleText("Весь доход:");
		table.addRow(new String[]{"Квартира","Доход"}).setBold(true);
		
		for (int i = 0,length = flatsData.length; i < length; i++){
			String profit = paymentDB.getProfit(Utils.Int(flatsData[i][Flat.FID]), Config.INST.SYSTEM.TODAY);
			table.addRow(new String[]{flatsData[i][Flat.ADDRESS],profit});
		}
		addView(table);
	}

	@Override
	protected void onPostInitialize() {
		db.close();
	}

}
