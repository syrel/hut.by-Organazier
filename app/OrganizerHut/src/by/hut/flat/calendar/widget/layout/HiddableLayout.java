package by.hut.flat.calendar.widget.layout;

import java.util.ArrayList;


import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public abstract class HiddableLayout extends LinearLayout implements Hiddable{
	
	private Context context;
	
	private boolean visible = false;
	private boolean lock = false;
	private boolean initialized = false;
	private boolean background = true;
	private boolean progressbar = true;
	
	private ArrayList<View> views;
	
	public HiddableLayout(Context context) {
		super(context);
		this.context = context;
		initLayout();
	}
	
	private HiddableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initLayout();
	}
	
	private HiddableLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initLayout();
	}

	private void initLayout(){
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.setOrientation(VERTICAL);
		this.setVisibility(GONE);
	}
	
	private void initProgressBar(){
		ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyle);
		super.addView(progress);
	}
	
	@Override
	public void show(){
		if (isLocked()) return;
		if (!this.initialized){
			if (progressbar)initProgressBar();
			views = new ArrayList<View>();
			if (background)new HiddableTask().execute();
			else {
				setLock(true);
				removeAllViews();
				onPreInitialize();
				doInInitialization();
				onPostInitialize();
				addViews();
				initialized = true;
				setLock(false);
			}
		}
		this.setVisibility(VISIBLE);
		visible = true;
	}
	
	@Override
	public void hide(){
		if (isLocked()) return;
		this.setVisibility(GONE);
		visible = false;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public boolean isLocked() {
		return lock;
	}

	@Override
	public View getView() {
		return this;
	}

	protected void setLock(boolean lock) {
		this.lock = lock;
	}
	
	protected abstract void onPreInitialize();
	protected abstract void doInInitialization();
	protected abstract void onPostInitialize();
	
	private class HiddableTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected void onPreExecute() {
			setLock(true);
			onPreInitialize();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			doInInitialization();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			removeAllViews();
			onPostInitialize();
			addViews();
			initialized = true;
			setLock(false);
		}	
	}
	
	private void addViews(){
		for (View view : views){
			super.addView(view);
		}
	}
	
	@Override
	public void addView(View view){
		this.views.add(view);
	}
	
	@Override
	public void setInitInBackground(boolean background){
		this.background = background;
	}
	
	@Override
	public void setProgressBarShown(boolean shown){
		this.progressbar = shown;
	}

}
