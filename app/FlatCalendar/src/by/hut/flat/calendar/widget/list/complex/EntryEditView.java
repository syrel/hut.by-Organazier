package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.widget.LinearLayout;

public class EntryEditView  extends LinearLayout implements Visitable{
		
	public EntryEditView(Context context) {
		super(context);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
