package by.hut.flat.calendar.sausage;

import android.os.AsyncTask;

public class SausageTask extends AsyncTask<Void, Void, Void> {
	private ISausage sausage;
	
	private OnTaskCompletedListener mOnTaskCompleted;
	
	public interface OnTaskCompletedListener{
		public void onTaskCompleted();
	}
	
	public SausageTask(ISausage sausage){
		this.sausage = sausage;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		sausage.initCells();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		sausage.addCells();
		if (mOnTaskCompleted != null) mOnTaskCompleted.onTaskCompleted();
	}
	
	public void setOnTaskCompletedListener(OnTaskCompletedListener l){
		this.mOnTaskCompleted = l;
	}
}
