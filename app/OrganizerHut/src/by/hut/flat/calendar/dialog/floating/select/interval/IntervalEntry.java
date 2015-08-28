package by.hut.flat.calendar.dialog.floating.select.interval;

import android.content.Context;
import by.hut.flat.calendar.widget.list.simple.Entry;

public class IntervalEntry extends Entry{

	private Context context;
	
	private IntervalEntryView view;
	private String flatAddress;
	private String date;
	private final int IntervalID;
			
	public IntervalEntry(Context context,int IntervalID) {
		super(context);
		this.context = context;
		this.IntervalID = IntervalID;
		init();
	}
	
	private void init(){
		view = new IntervalEntryView(context,this);
		this.setHeaderView(view);
	}
	
	protected String getFlatAddress(){
		return this.flatAddress;
	}
	
	protected String getDate(){
		return this.date;
	}
	
	public void setFlatAddress(String flatAddress){
		this.flatAddress = flatAddress;
		view.setFlatAddress(flatAddress);
	}
	
	public void setDate(String date){
		this.date = date;
		view.setDate(date);
	}
	
	public void hideLine(){
		view.hideLine();
	}

	public int getIntervalID() {
		return IntervalID;
	}
}