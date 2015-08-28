package by.hut.flat.calendar.widget.list.simple;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class SubListView extends LinearLayout implements IEntryView,IMenuObserver {

	public SubListView(Context context,IEntry subject) {
		super(context);
		subject.registerObserver(this);
	}
	
	public SubListView(Context context,IEntry subject,boolean custom) {
		super(context);
		subject.registerObserver(this);
		if (!custom){
			useSimpleView();
		}
	}
	
	@Override
	public void useSimpleView() {
		this.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(layoutParams);
	}

	@Override
	public void notifyEntryAdd(IEntry entry) {
		addView(entry.getHeaderView());
	}
	
	@Override
	public View view() {
		return this;
	}

	@Override
	public void setView(IEntryView entryView) {
		assert entryView != null;
		clear();
		addView(entryView.view());
	}

	@Override
	public void notifyEntryHeaderClicked(IEntryView entryView) {
		
	}

	@Override
	public void clear() {
		this.removeAllViews();
	}

	@Override
	public void notifyToCollapse() {}
	
	
	
}
