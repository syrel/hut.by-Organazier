package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class ProfitInterval extends TitledTableLayout{

	public ProfitInterval(Context context,Date dateFrom, Date dateTo,Payment paymentDB,String[][] flatsData,String title) {
		super(context);
		init(dateFrom, dateTo,paymentDB,flatsData, title);
	}
	
	private void init(Date dateFrom, Date dateTo,Payment paymentDB,String[][] flatsData,String title) {
		setTitleText(title);
		addRow(new String[]{"Квартира","Доход"}).setBold(true);
		
		for (int i = 0,length = flatsData.length; i < length; i++){
			String profit = paymentDB.getProfit(Utils.Int(flatsData[i][Flat.FID]),dateFrom,dateTo, Config.INST.SYSTEM.TODAY);
			addRow(new String[]{flatsData[i][Flat.ADDRESS],""+profit});
		}
	}

}
