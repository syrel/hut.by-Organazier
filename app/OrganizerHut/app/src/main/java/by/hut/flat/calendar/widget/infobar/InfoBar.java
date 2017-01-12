package by.hut.flat.calendar.widget.infobar;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.layout.ObservableScrollView;

public abstract class InfoBar extends ObservableScrollView implements IInfoBar{
	
	private HashMap<String,IInfo> infos;
	private int size;
	private int height;
	
	private LinearLayout container;
		
	public InfoBar(Context context) {
		this(context,null);
	}
	
	public InfoBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public InfoBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		infos = new HashMap<String,IInfo>();
		initLayout();
	}
	
	private void initLayout(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.info_bar_layout, this);
		container = (LinearLayout) findViewById(R.id.container);
	}
	
	@Override
	public void addInfo(IInfo info){
		String tag = info.getTag();
		if (tag == null) {
			tag = ""+info.hashCode();
			info.setTag(tag);
		}
		IInfo oldInfo = infos.put(tag, info);
		if (oldInfo != null){
			int index = container.indexOfChild(oldInfo.getView());
			container.addView(info.getView(), index);
			this.removeInfo(oldInfo);
		}
		else {
			container.addView(info.getView());
		}
		info.bind(this);
		size++;
	}
	
	@Override
	public void removeInfo(IInfo info){
		if (this.contains(info)){
			container.removeView(info.getView());
			info.unbind(this);
			infos.remove(info.getTag());
			size--;
		}
	}
	
	@Override
	public int size(){
		return size;
	}
	
	public void setHeight(int height){
		this.height = height;
		ViewGroup.LayoutParams lp = this.getLayoutParams();
		lp.height = this.height;
		this.setLayoutParams(lp);
		
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) container.getLayoutParams();
		layoutParams.height = this.height;
		container.setLayoutParams(layoutParams);
	}
	
	@Override
	public boolean contains(IInfo info){
		return infos.containsKey(info.getTag());
	}

	@Override
	public IInfo getInfo(String tag) {
		return infos.get(tag);
	}
	
	@Override
	public void refresh(){
		for (IInfo info : infos.values()){
			info.refresh();
		}
	}
	
	@Override
	public void setActivity(Activity activity){
		for (IInfo info : infos.values()){
			info.setActivity(activity);
		}
	}
	
	@Override
	public HashMap<String,IInfo> getInfos(){
		return this.infos;
	}
}
