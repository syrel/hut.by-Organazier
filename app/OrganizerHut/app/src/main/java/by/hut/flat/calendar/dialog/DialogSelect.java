package by.hut.flat.calendar.dialog;


import by.hut.flat.calendar.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
public abstract class DialogSelect extends Dialog implements OnClickListener {
	
	public static final int INACTIVE_COLOR = 0xff8C8C8C;
	protected LinearLayout container;
	
	
	public static void show(Context context, String[] keys,String[] params){
		Intent newIntent = new Intent(context, DialogSelect.class);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		context.startActivity(newIntent);
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/

	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	/**
	 * Initializes initial view of dialog
	 */
	@Override
	public void onInitContentView(){
		setContentView(R.layout.dialog_select_activity);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_select_layout);
	}
	@Override
	public void onInitElements(){
		container = (LinearLayout) this.findViewById(R.id.dialog_select_container);
		onInitButtons();
		initSelectLayout();
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
		
		// set onclick listener to launch or cancel task
		ok.setOnClickListener(this);
		ok.setClickable(false);
		cancel.setOnClickListener(this);
	}
	/*------------------------------------------------------------
	--------------------- T O   O V E R R I D E ------------------
	------------------------------------------------------------*/
	protected abstract void okClick();
	protected abstract void initSelectLayout();

	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ok:{
				okClick();
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
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/

}
