package by.hut.flat.calendar.widget.infobar;

import android.app.Activity;
import android.view.View;

public interface IInfo {
	public void setBackgroundColor(int color);
	public String getTag();
	public void setTag(String tag);
	public View getView();
	public void bind(IInfoBar infoBar);
	public void unbind(IInfoBar infoBar);
	public void show();
	public void hide();
	public void refresh();
	public void setActivity(Activity activity);
	public Activity getActivity();
}
