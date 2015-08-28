package by.hut.flat.calendar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Employee;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class DialogSelectCleaning extends DialogSelect implements OnCheckedChangeListener{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogSelectCleanings";
	
	private static final int BOOK_BGCOLOR = 0x3300FF00;
	private static final int RENT_BGCOLOR = 0x332bc0ff;
	
	private DBAdapter db;
	private int[] cleaningIDs;
	private RadioGroup radioGroup;
	private int radioButtonChoice;
	private ScrollView scroll;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public static void showForResult(Activity activity,int requestCode,String[] keys,String[] params, int[] cleaning_ids){	
		Intent newIntent = new Intent(activity, DialogSelectCleaning.class);
		newIntent.putExtra(ACTION_DO,ACTION_SELECT_CLEANING);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		newIntent.putExtra(PARAM_CLEANING_IDS, cleaning_ids);
		activity.startActivityForResult(newIntent,requestCode);
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	@Override
	protected void initSelectLayout(){
		initDialog();
		initRadioGroup();
		initRadioButtons();
	}
	
	private void initDialog(){
		db = new DBAdapter(context);
		cleaningIDs = extras.getIntArray(PARAM_CLEANING_IDS);
	}
	private void initRadioGroup(){
		ok.setClickable(false);
		ok.setTextColor(INACTIVE_COLOR);
		scroll = new ScrollView(context);
		radioGroup = new RadioGroup(context);
		radioGroup.setOnCheckedChangeListener(this);
		scroll.addView(radioGroup);
		container.addView(scroll);
	}
	private void initRadioButtons(){
		db.open();
		Cleanings cleaningsDB = new Cleanings(db);
		Employee employeeDB = new Employee(db);
		IntervalAnketa intervalAnketaDB = new IntervalAnketa(db);
		for (int i = 0; i < cleaningIDs.length;i++){
			RadioButton radioButton = new RadioButton(context);
			
			int[] cData = cleaningsDB.getData(cleaningIDs[i]);
			String[] eData = employeeDB.getData(cData[Cleanings.EID]);
			Date date = new Date(cData[Cleanings.DATE]);
			String text = " "+eData[Employee.FIO] + " - "+date.day+"."+date.month+"."+date.year;
			
			/* if inteval exists */
			if (cData[Cleanings.IID] > 0){
				String[] iData = intervalAnketaDB.getData(cData[Cleanings.IID]);
				
				if (Utils.Int(iData[IntervalAnketa.TYPE]) == dbStructure.RENT){
					radioButton.setBackgroundColor(RENT_BGCOLOR);
				}
				else if(Utils.Int(iData[IntervalAnketa.TYPE]) == dbStructure.BOOK){
					radioButton.setBackgroundColor(BOOK_BGCOLOR);
				}
			}
			
			radioButton.setText(text);
			
			radioButton.setTag(""+cleaningIDs[i]);
			addButton(radioButton);
		}
		db.close();
	}
	private void addButton(RadioButton radioButton){
		radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		radioButton.setButtonDrawable(R.drawable.btn_radio_holo_dark);
		radioGroup.addView(radioButton);
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	protected void okClick(){
		Intent intent = new Intent();
		int cleaningID = Utils.Int((String) radioGroup.findViewById(radioButtonChoice).getTag());
		if (cleaningID >= 0){
			intent.putExtra(PARAM_CLEANING_ID, cleaningID);
			setResult(RESULT_OK, intent);
			finish();
		}
		else {
			finish();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		if (checkedId != 0){
			radioButtonChoice = checkedId;
			ok.setClickable(true);
			ok.setTextColor(Color.WHITE);
		}
		else {
			ok.setClickable(false);
			ok.setTextColor(INACTIVE_COLOR);
		}
	}
	
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}
}
