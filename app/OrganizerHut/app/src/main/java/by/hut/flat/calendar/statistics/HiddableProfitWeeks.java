package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.layout.HiddableLayout;

public class HiddableProfitWeeks extends HiddableLayout {

	public DBAdapter db;
	private Payment paymentDB;
	private Flat flatDB;
	private String[][] flatsData;
	
	public HiddableProfitWeeks(Context context) {
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
		currentMonth();
		currentWeek();
		previousWeek();
		previousMonth();
		currentWeekDays();
		previousWeekDays();
	}
	
	private void currentWeekDays() {
		Date today = Config.INST.SYSTEM.TODAY.copy();
		CalendarHelper calendar = new CalendarHelper(today);
		Date date = today.copy().findDate(-calendar.dayOfWeek);
		addView(new ProfitDays(getContext(), date, paymentDB, "Доход за текущую неделю по дням:"));
	}
	
	private void previousWeekDays() {
		Date today = Config.INST.SYSTEM.TODAY.copy();
		CalendarHelper calendar = new CalendarHelper(today);
		Date date = today.copy().findDate(-calendar.dayOfWeek).findDate(-7);
		addView(new ProfitDays(getContext(),date, paymentDB, "Доход за предыдущую неделю по дням:"));
	}
	
	private void currentWeek() {
		Date today = Config.INST.SYSTEM.TODAY.copy();
		CalendarHelper calendar = new CalendarHelper(today);
		Date dateFrom = today.copy().findDate(-calendar.dayOfWeek);
		Date dateTo = today.copy().findDate(6-calendar.dayOfWeek);
		String title = "Доход за текущую неделю:";
		addView(new ProfitInterval(getContext(),dateFrom, dateTo, paymentDB, flatsData, title));
	}	
	
	private void previousWeek() {
		Date today = Config.INST.SYSTEM.TODAY.copy();
		CalendarHelper calendar = new CalendarHelper(today);
		Date dateFrom = today.copy().findDate(-calendar.dayOfWeek).findDate(-7);
		Date dateTo = today.copy().findDate(6-calendar.dayOfWeek).findDate(-7);
		String title = "Доход за предыдущую неделю:";
		addView(new ProfitInterval(getContext(), dateFrom, dateTo, paymentDB, flatsData, title));
	}
	
	private void currentMonth(){
		Date dateFrom = Config.INST.SYSTEM.TODAY.copy().setDay(1);
		Date dateTo = dateFrom.copy().jumpMonth(1).prev();
		String title = "Доход за текущий месяц:";
		addView(new ProfitInterval(getContext(), dateFrom, dateTo, paymentDB, flatsData, title));
	}
	
	private void previousMonth(){
		Date dateFrom = Config.INST.SYSTEM.TODAY.copy().jumpMonth(-1).setDay(1);
		Date dateTo = Config.INST.SYSTEM.TODAY.copy().setDay(1).prev();
		String title = "Доход за предыдущий месяц:";
		addView(new ProfitInterval(getContext(), dateFrom, dateTo, paymentDB, flatsData, title));
	}

	@Override
	protected void onPostInitialize() {
		db.close();
	}
}
