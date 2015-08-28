package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Employee;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

public class DialogSelectMaiden extends DialogSelect implements OnCheckedChangeListener {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogSelectMaiden";
	private DBAdapter db;
	private RadioGroup radioGroup;
	private int radioButtonChoice;
	private ScrollView scroll;
	private Date date;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public static void showForResult(Activity activity,int requestCode,String[] keys,String[] params){	
		Intent newIntent = new Intent(activity, DialogSelectMaiden.class);
		newIntent.putExtra(ACTION_DO,ACTION_SELECT_MAIDEN);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
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
		date = new Date(getParam(PARAM_DATE));
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
		Maiden dbMaiden = new Maiden(db);
		Employee dbEmployee = new Employee(db);
		
		int dayOfWeek = new CalendarHelper(date).getDayOfWeek();
		int[] maidens = dbMaiden.getMaidenAtDay(dayOfWeek);
		
		for (int i = 0; i < maidens.length;i++){
			RadioButton radioButton = new RadioButton(context);
			
			String[] employeeData = dbEmployee.getData(maidens[i]);
			
			String text = " "+employeeData[Employee.FIO];
			radioButton.setText(text);
			
			radioButton.setTag(""+maidens[i]);
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
		int maidenID = Utils.Int((String) radioGroup.findViewById(radioButtonChoice).getTag());
		if (maidenID >= 0){
			intent.putExtra(PARAM_MAIDEN_ID, maidenID);
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
