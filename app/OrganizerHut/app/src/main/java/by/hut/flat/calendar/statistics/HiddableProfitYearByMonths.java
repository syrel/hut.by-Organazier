package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.HiddableLayout;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class HiddableProfitYearByMonths extends HiddableLayout{
	
	private int year;
	public DBAdapter db;
	private String[][] flatsData;
	private Flat flatDB;
	private Payment paymentDB;
	
	public HiddableProfitYearByMonths(Context context,int year) {
		super(context);
		this.year = year;
	}	
	
	@Override
	protected void onPreInitialize() {
		db = new DBAdapter(getContext());
		db.open();
		flatDB = new Flat(db);
		paymentDB = new Payment(db);
	}

	@Override
	protected void doInInitialization() {
		flatsData = flatDB.getData(true);
		initYear();
	}
	
	private void initYear(){
		Date dateFrom = new Date(1,12,year);
		Date dateTo = new Date(1,1,year+1).prev();
		
		if (year == Config.INST.SYSTEM.TODAY.year){
			dateFrom = Config.INST.SYSTEM.TODAY.copy().setDay(1);
			dateTo = dateFrom.copy().jumpMonth(1).prev();
		}
		
		while (true){
			String title = "Доход за "+Config.INST.STRINGS.MONTHS_OF_YEAR[dateTo.month-1]+":";
			initProfitFlats(dateFrom, dateTo,title);
			dateFrom.jumpMonth(-1);
			dateTo.jumpMonth(-1);
			if (dateFrom.year != year){
				break;
			}
		}
		dateFrom = new Date(1,1,year);
		dateTo = new Date(1,1,year+1).prev();
		initProfitFlats(dateFrom, dateTo,"Доход за весь "+year+":");
	}
	
	private void initProfitFlats(Date dateFrom, Date dateTo,String title) {
		TitledTableLayout table = new TitledTableLayout(getContext());
		table.setTitleText(title);
		table.addRow(new String[]{"Квартира","Доход"}).setBold(true);
		
		for (int i = 0,length = flatsData.length; i < length; i++){
			String profit = paymentDB.getProfit(Utils.Int(flatsData[i][Flat.FID]),dateFrom,dateTo, Config.INST.SYSTEM.TODAY);
			table.addRow(new String[]{flatsData[i][Flat.ADDRESS],""+profit});
		}
		
		addView(table);
	}

	@Override
	protected void onPostInitialize() {}
}
