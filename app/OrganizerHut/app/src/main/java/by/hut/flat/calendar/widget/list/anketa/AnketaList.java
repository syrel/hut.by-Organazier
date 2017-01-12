package by.hut.flat.calendar.widget.list.anketa;

import java.util.ArrayList;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.List;

public class AnketaList extends List{

	private Context context;
	private ArrayList<String[]> list;
	
	public AnketaList(Context context,ArrayList<String[]> list) {
		super(context);
		assert context != null;
		this.context = context;
		this.list = list;
		initList();
	}
	
	@Override
	protected void initList() {
		for (int i = 0,length = list.size(); i < length; i++){
			initSearchResult(list,i);
		}
	}
	
	private void initSearchResult(ArrayList<String[]> result,int index){
		AnketaEntry entry = new AnketaEntry(context,Utils.Int(result.get(index)[IntervalAnketa.IID]));
		entry.setTelephoneLeft(result.get(index)[IntervalAnketa.TELEPHONE_LEFT]);
		entry.setTelephoneRight(result.get(index)[IntervalAnketa.TELEPHONE_RIGHT]);
		entry.setNameLeft(result.get(index)[IntervalAnketa.NAME_LEFT]);
		entry.setNameRight(result.get(index)[IntervalAnketa.NAME_RIGHT]);
		entry.setFlatAddress(result.get(index)[IntervalAnketa.FLAT_SHORT_ADDRESS]);
		Date dateFrom = new Date(Utils.Int(result.get(index)[IntervalAnketa.INTERVAL_DATE_FROM]));
		Date dateTo = new Date(Utils.Int(result.get(index)[IntervalAnketa.INTERVAL_DATE_TO]));
		
		boolean diffMonth = (dateFrom.month != dateTo.month);
		boolean diffYear = (dateFrom.year != dateTo.year);
		if (diffYear)diffMonth = true;
		
		StringBuilder date = new StringBuilder();
		date.append(dateFrom.day);
		if (diffMonth)date.append('.');
		if (diffMonth)date.append(dateFrom.month);
		if (diffYear)date.append('.');
		if (diffYear)date.append(dateFrom.year);
		date.append(' ');
		date.append('-');
		date.append(' ');
		date.append(dateTo.day);
		date.append('.');
		date.append(dateTo.month);
		date.append('.');
		date.append(dateTo.year);
		entry.setDate(date.toString());
		entry.setPrice(result.get(index)[IntervalAnketa.PAYMENT_PRICE]);
		entry.setCost(result.get(index)[IntervalAnketa.PAYMENT_AMOUNT]);
		entry.setType(result.get(index)[IntervalAnketa.TYPE]);
		entry.setSettlement(result.get(index)[IntervalAnketa.SETTLEMENT]);
		entry.setEviction(result.get(index)[IntervalAnketa.EVICTION]);
		entry.setMaidenBackground(Utils.Int(result.get(index)[IntervalAnketa.MAIDEN_BACKGROUND]));
		entry.setToday(Utils.Int((result.get(index)[IntervalAnketa.INTERVAL_TODAY]))==1);
		
		if (dateFrom.equals(Config.INST.SYSTEM.TODAY))entry.setEventSettlement();
		if (dateTo.equals(Config.INST.SYSTEM.TODAY))entry.setEventEviction();
		
		entry.check();
		this.add(entry);
	}


}
