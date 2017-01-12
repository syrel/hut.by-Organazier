package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.widget.FrameLayout;

public abstract class EntryView extends FrameLayout{
		
	public EntryView(Context context) {
		super(context);
	}
	
	protected abstract void init();
}
