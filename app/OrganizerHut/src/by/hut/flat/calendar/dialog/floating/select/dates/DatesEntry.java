package by.hut.flat.calendar.dialog.floating.select.dates;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.list.simple.Entry;

public class DatesEntry extends Entry{


	private final Context context;
	
	private DatesEntryView view;
	private String type;
	private Date date;
	private int id;
	
	public DatesEntry(Context context, int id) {
		super(context);
		this.context = context;
		this.id = id;
		init();
	}
	
	private void init(){
		view = new DatesEntryView(context,this);
		this.setHeaderView(view);
	}
	
	protected String getType(){
		return this.type;
	}
	
	protected Date getDate(){
		return this.date;
	}
	
	public void setType(String type){
		this.type = type;
		view.setType(type);
	}
	
	public void setDate(Date date){
		this.date = date;
		view.setDate(date.day+" "+Config.INST.STRINGS.MONTHS_OF_YEAR[date.month-1]+" "+date.year);
	}
	
	public void hideLine(){
		view.hideLine();
	}
	
	public int getID(){
		return this.id;
	}

}
