package by.hut.flat.calendar.widget.layout;

import android.view.View;

public interface Hiddable {
	public void hide();
	public void show();
	public boolean isLocked();
	public boolean isVisible();
	public View getView();
	public void setInitInBackground(boolean background);
	public void setProgressBarShown(boolean shown);
}
