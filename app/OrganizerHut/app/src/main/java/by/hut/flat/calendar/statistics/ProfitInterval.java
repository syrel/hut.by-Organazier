package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.TitledTableLayout;

public class ProfitInterval extends TitledTableLayout{

	public ProfitInterval(Context context, Date dateFrom, Date dateTo,Payment paymentDB,String[][] flatsData, String title) {
		super(context);
		init(dateFrom, dateTo,paymentDB,flatsData, title);
	}

    public ProfitInterval(Context context) {
        super(context);
    }

    private void init(Date dateFrom, Date dateTo, Payment paymentDB, String[][] flatsData, String title) {
		setTitleText(title);
		addRow("Квартира", "Доход").setBold(true);
		for (String[] flatData : flatsData) {
			String profit = paymentDB.getProfit(Utils.Int(flatData[Flat.FID]),dateFrom,dateTo, Config.INST.SYSTEM.TODAY);
			addRow(flatData[Flat.ADDRESS], profit);
		}
	}
}