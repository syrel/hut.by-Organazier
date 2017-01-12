package by.hut.flat.calendar.dialog.floating.select.cleanings;

import android.content.Context;
import by.hut.flat.calendar.widget.list.simple.Entry;

public class CleaningEntry extends Entry{

	private Context context;
	
	private CleaningEntryView view;
	private String menuName;
	private final int CleaningID;
			
	public CleaningEntry(Context context,String menuName,int CleaningID) {
		super(context);
		this.context = context;
		this.menuName = menuName;
		this.CleaningID = CleaningID;
		init();
	}
	
	private void init(){
		view = new CleaningEntryView(context,this);
		view.setMenuName(menuName);
		this.setHeaderView(view);
	}
	
	protected String getMenuName(){
		return this.menuName;
	}
		
	public void setMenuName(String menuName){
		this.menuName = menuName;
		view.setMenuName(menuName);
	}
	
	public void hideLine(){
		view.hideLine();
	}

	public int getCleaningID() {
		return CleaningID;
	}
}