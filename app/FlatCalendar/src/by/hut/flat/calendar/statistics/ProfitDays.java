package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class ProfitDays extends TitledTableLayout{
	
	public ProfitDays(Context context,Date date, Payment paymentDB, String title) {
		super(context);
		init(date,paymentDB,title);
	}

	private void init(Date date,Payment paymentDB,String title) {
		setTitleText(title);
		addRow(new String[]{"Дни недели","Доход"}).setBold(true);
		for (int i = 0; i < 7; i++){
			String profit = paymentDB.getProfit(date,date, Config.INST.SYSTEM.TODAY);
			addRow(new String[]{Config.INST.STRINGS.DAYS_OF_WEEK_SHORT[i],""+profit});
			date.next();
		}
	}
}
