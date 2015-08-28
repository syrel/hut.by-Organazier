package by.hut.flat.calendar.dialog.floating.select.dates;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntry;

public class DatesEntryView extends EntryView{

	private TextView type;
	private TextView date;
	private LinearLayout line;
	
	public DatesEntryView(Context context, IEntry entry) {
		super(context, entry, true);
		initView();
	}
	
	protected void initView(){
		this.setLayout(R.layout.dialog_floating_select_dates_activity_entry);
		initType();
		initDate();
	}
	
	private void initType(){
		type = (TextView) this.findViewById(R.id.type);
	}
	
	protected void hideLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(GONE);
	}
	
	protected void showLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(VISIBLE);
	}
	
	private void initDate(){
		date = (TextView) this.findViewById(R.id.date);
	}
	
	public void setType(String type){
		this.type.setText(type);
	}
	
	public void setDate(String date){
		this.date.setText(date);
	}
	
}
