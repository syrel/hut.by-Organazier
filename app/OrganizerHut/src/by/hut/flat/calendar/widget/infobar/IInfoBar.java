package by.hut.flat.calendar.widget.infobar;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;

public interface IInfoBar {
	public void addInfo(IInfo info);
	public void removeInfo(IInfo info);
	public IInfo getInfo(String tag);
	public boolean contains(IInfo info);
	public int size();
	public boolean onActivityResult(int requestCode, int resultCode, Intent data);
	public void refresh();
	public HashMap<String,IInfo> getInfos();
	public void setActivity(Activity activity);
}
