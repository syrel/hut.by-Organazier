package by.hut.flat.calendar.internal;

import by.hut.flat.calendar.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("unused")
public final class AsyncTaskManager implements IProgressTracker, OnCancelListener {

	private final OnTaskCompleteListener mTaskCompleteListener;
	private TextView dialog_message;
	private Task mAsyncTask;
	private Timer timer;
	
	private Context ctx;

	public AsyncTaskManager(Context context, LinearLayout dialog, OnTaskCompleteListener taskCompleteListener) {
		this.ctx = context;
		// Save reference to complete listener (activity)
		mTaskCompleteListener = taskCompleteListener;
		// Setup progress dialog
		dialog_message = (TextView)dialog.findViewById(R.id.dialog_thread_text);
	}

	public void setupTask(Task asyncTask) {
		// Keep task
		mAsyncTask = asyncTask;
		// Wire task to tracker (this)
		mAsyncTask.setProgressTracker(this);
		// Start task
		timer = new Timer();
		mAsyncTask.execute();
	}

	@Override
	public void onProgress(String message) {
		// Show current message in progress dialog if field exists
		if (dialog_message != null){
			dialog_message.setText(message);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// Cancel task
		mAsyncTask.cancel(true);
		// Notify activity about completion
		mTaskCompleteListener.onTaskComplete(mAsyncTask);
		// Reset task
		mAsyncTask = null;
	}

	@Override
	public void onComplete() {
		// Close progress dialog
		if (dialog_message != null){
			dialog_message.setText("Completed! Time: "+(timer.timeElapsed()/1000)+"sec");
		}
		// Notify activity about completion
		mTaskCompleteListener.onTaskComplete(mAsyncTask);
		// Reset task
		mAsyncTask = null;
	}

	public Object retainTask() {
		// Detach task from tracker (this) before retain
		if (mAsyncTask != null) {
			mAsyncTask.setProgressTracker(null);
		}
		// Retain task
		return mAsyncTask;
	}

	public void handleRetainedTask(Object instance) {
		// Restore retained task and attach it to tracker (this)
		if (instance instanceof Task) {
			mAsyncTask = (Task) instance;
			mAsyncTask.setProgressTracker(this);
		}
	}

	public boolean isWorking() {
		// Track current status
		return mAsyncTask != null;
	}
}