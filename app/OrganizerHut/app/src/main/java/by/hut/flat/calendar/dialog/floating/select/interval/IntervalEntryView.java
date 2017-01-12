package by.hut.flat.calendar.dialog.floating.select.interval;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntry;

public class IntervalEntryView extends EntryView {

	private TextView flat;
	private TextView date;
	private LinearLayout line;
	
	public IntervalEntryView(Context context, IEntry entry) {
		super(context, entry, true);
		initView();
	}
	
	protected void initView(){
		this.setLayout(R.layout.dialog_floating_select_interval_activity_entry);
		initFlat();
		initDate();
	}
	
	protected void hideLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(GONE);
	}
	
	protected void showLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(VISIBLE);
	}
	
	private void initFlat(){
		flat = (TextView) this.findViewById(R.id.flat);

	}
	
	private void initDate(){
		date = (TextView) this.findViewById(R.id.date);
	}
	
	public void setFlatAddress(String flatAddress){
		flat.setText(flatAddress);
	}
	
	public void setDate(String date){
		this.date.setText(date);
	}
}
