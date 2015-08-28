package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.internal.AsyncTaskManager;
import by.hut.flat.calendar.internal.OnTaskCompleteListener;
import by.hut.flat.calendar.internal.Task;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DialogThread extends Dialog implements OnTaskCompleteListener, OnClickListener{

	private ProgressBar progressBar;
	private TextView dialogMessage;
	protected AsyncTaskManager mAsyncTaskManager;
	
	@Override
	public void onInitContentView(){
		setContentView(R.layout.dialog_thread_activity);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_thread_layout);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onInitElements(){
		logText = (TextView) findViewById(R.id.dialog_thread_log);
		logScroll = (ScrollView) findViewById(R.id.scroll);
		
		progressBar = (ProgressBar) findViewById(R.id.dialog_thread_bar);
		dialogMessage = (TextView) findViewById(R.id.dialog_thread_text);
		mAsyncTaskManager = new AsyncTaskManager(context, dialogLayout,this);
		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
		
		onInitButtons(); // init ok,cancel,close buttons
	}
	
	
	public static void show(Context context, String[] keys,String[] params){
		Intent newIntent = new Intent(context, DialogThread.class);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		context.startActivity(newIntent);
	}
	
	/**
	 * Initializes ok,cancel,close buttons and if {code Aks = false} starts task.
	 */
	
	@Override
	public void onInitButtons(){
		Resources resources = this.context.getResources();
		
		ok = (Button)findViewById(R.id.ok);
		ok.setText(resources.getString(R.string.dialog_button_ok_text));
		
		cancel = (Button)findViewById(R.id.cancel);
		cancel.setText(resources.getString(R.string.dialog_button_cancel_text));

		close = (Button)findViewById(R.id.close);
		close.setText(resources.getString(R.string.dialog_button_close_text));
		close.setOnClickListener(this);
		
		// set onclick listener to launch or cancel task
		if (Ask){
			ok.setOnClickListener(this);			
			cancel.setOnClickListener(this);
		}
		// when no asking, start immediately
		else {
			launchTask();
		}
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	// override to add tasks
	public abstract void implementTask();
	
	private void launchTask(){
				
		if (QuickHide){
			dialogMessage.setVisibility(View.GONE);
			dialogLayout.setVisibility(View.GONE);
		}
		
		// show log window if Log = true.
		if (Log){
			logScroll.setVisibility(View.VISIBLE);
		}
		
		// show progress bar
		if (Progress){
			progressBar.setVisibility(View.VISIBLE);
		}
		
		// deactivate buttons
		ok.setClickable(false);
		ok.setTextColor(Color.rgb(140, 140, 140));
		cancel.setClickable(false);
		cancel.setTextColor(Color.rgb(140, 140, 140));
		
		// actually implementing and launching task
		implementTask();
	}
	
	@Override
	public void onTaskComplete(Task task) {
		if (task.isCancelled()) {
			Toast.makeText(context,"Canceled!", Toast.LENGTH_LONG).show();
		}
		else {
			if (AutoHide){ // close if autohide = true
				finish();
			}
			ok.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
			close.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return mAsyncTaskManager.retainTask();
	}
	
	public void closeDialog() {
		finish();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ok:{
				launchTask();
				break;
			}
			case R.id.cancel:{
				finish();
				break;
			}
			case R.id.close:{
				closeDialog();
				break;
			}
		}
	}
}
