package by.hut.flat.calendar.dialog.floating.select.cleanings;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntry;

public class CleaningEntryView extends EntryView {

	private TextView menuName;
	private LinearLayout line;
	
	public CleaningEntryView(Context context, IEntry entry) {
		super(context, entry, true);
		initView();
	}
	
	protected void initView(){
		this.setLayout(R.layout.dialog_floating_select_cleaning_activity_entry);
		initMenuName();
	}
	
	protected void hideLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(GONE);
	}
	
	protected void showLine(){
		if (line == null) line = (LinearLayout) this.findViewById(R.id.line);
		line.setVisibility(VISIBLE);
	}
	
	private void initMenuName(){
		menuName = (TextView) this.findViewById(R.id.name);
	}
	
	public void setMenuName(String name){
		menuName.setText(name);
	}
}
