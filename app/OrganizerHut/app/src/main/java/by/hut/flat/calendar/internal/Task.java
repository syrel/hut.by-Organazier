package by.hut.flat.calendar.internal;

import android.content.Context;
import android.os.AsyncTask;

public abstract class Task extends AsyncTask<Void, String, Boolean> {

	private Boolean mResult;
	private String mProgressMessage;
	private IProgressTracker mProgressTracker;

	/* UI Thread */
	public Task(Context ctx) {
		// Keep reference to resources
		// Initialise initial pre-execute message
		mProgressMessage = "Please wait...";
	}

	/* UI Thread */
	public void setProgressTracker(IProgressTracker progressTracker) {
		// Attach to progress tracker
		mProgressTracker = progressTracker;
		// Initialise progress tracker with current task state
		if (mProgressTracker != null) {
			mProgressTracker.onProgress(mProgressMessage);
			if (mResult != null) {
				mProgressTracker.onComplete();
			}
		}
	}

	/* UI Thread */
	@Override
	protected void onCancelled() {
		// Detach from progress tracker
		mProgressTracker = null;
	}

	/* UI Thread */
	@Override
	protected void onProgressUpdate(String... values) {
		// Update progress message 
		mProgressMessage = values[0];
		// And send it to progress tracker
		if (mProgressTracker != null) {
			mProgressTracker.onProgress(mProgressMessage);
		}
	}

	/* UI Thread */
	@Override
	protected void onPostExecute(Boolean result) {
		// Update result
		mResult = result;
		// And send it to progress tracker
		if (mProgressTracker != null) {
			mProgressTracker.onComplete();
		}
		// Detach from progress tracker
		mProgressTracker = null;
	}
}