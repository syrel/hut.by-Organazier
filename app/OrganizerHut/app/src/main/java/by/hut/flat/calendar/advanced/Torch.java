package by.hut.flat.calendar.advanced;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import by.hut.flat.calendar.root.LinuxShell;
import by.hut.flat.calendar.widget.list.simple.Entry;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntryView;

public class Torch extends Entry implements OnClickListener{
	private Context context;
	private IEntryView headerView;
	
	private boolean on = false;
	private boolean block = false;
	
	public Torch(Context context) {
		super(context, "Фонарик");
		this.context = context;
		buildView();
	}
	
	private void buildView(){
		headerView = new EntryView(context,this,false);
		headerView.setOnClickListener(this);
		this.setHeaderView(this.headerView);
	}

	@Override
	public void onClick(View v) {
		if(!block)new TorchTask().execute(on);
	}
	
	private class TorchTask extends AsyncTask<Boolean, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        block = true;
	    }
		
		@Override
		protected Boolean doInBackground(Boolean... params) {
			if (!params[0]){
				LinuxShell.execute("echo 1 > /sys/class/camera/flash/rear_flash");
				params[0] = true;
			}
			else {
				LinuxShell.execute("echo 0 > /sys/class/camera/flash/rear_flash");
				params[0] = false;
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			on = result;
			block = false;
		}
	}
}
