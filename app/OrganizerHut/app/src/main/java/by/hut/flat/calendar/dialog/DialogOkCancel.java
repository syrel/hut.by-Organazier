package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DialogOkCancel extends Dialog implements OnClickListener {

	public static final String TAG = DialogOkCancel.class.getName();
	
	public static void showForResult(Activity activity,int requestCode,String question){	
		Intent newIntent = new Intent(activity, DialogSelectInterval.class);
		newIntent.putExtra(ACTION_DO,ACTION_OK_CANCEL);
		newIntent.putExtra(ACTION_QUESTION, question);
		activity.startActivityForResult(newIntent,requestCode);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()){
		case R.id.ok:{
			Intent intent = new Intent();
	    	setResult(RESULT_OK, intent);
			finish();
			break;
		}
		case R.id.cancel:{
			Intent intent = new Intent();
	    	setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
	}
	}

	@Override
	public void onInitContentView() {}

	@Override
	public void onInitElements() {}

	@Override
	public void onInitButtons() {
		Resources resources = this.context.getResources();
		
		ok = (Button)findViewById(R.id.ok);
		ok.setText(resources.getString(R.string.dialog_button_ok_text));
		
		cancel = (Button)findViewById(R.id.cancel);
		cancel.setText(resources.getString(R.string.dialog_button_cancel_text));
		
		// set onclick listener to launch or cancel task
		ok.setOnClickListener(this);
		ok.setClickable(false);
		cancel.setOnClickListener(this);
	}

	@Override
	public String getActivityTag() {
		return TAG;
	}

}
