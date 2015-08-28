package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class EntryAddView extends LinearLayout implements Visitable{
	
	public EntryAddView(Context context) {
		super(context);
	}
	
	protected abstract void clearAllFields();
		
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	
	

}
