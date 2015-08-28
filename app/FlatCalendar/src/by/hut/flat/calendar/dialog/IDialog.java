package by.hut.flat.calendar.dialog;

import android.widget.LinearLayout;

public interface IDialog {
	public void onInitContentView();
	public void onInitElements();
	public void onInitButtons();
	public String getActivityTag();
	public LinearLayout getLayout();
}
