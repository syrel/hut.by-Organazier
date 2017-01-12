package by.hut.flat.calendar.dialog;

import java.util.LinkedList;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Employee;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DialogSelectRemoveCleanings extends DialogSelect implements OnCheckedChangeListener{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogSelectRemoveCleanings";
	
	private DBAdapter db;
	private ScrollView scroll;
	private int[] cleaningIDs;
	private LinkedList<Integer> selectedCleaningIDs;
	private CheckBox[] checkBoxes;
	private LinearLayout checkBoxContainer;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public static void showForResult(Activity activity,int requestCode,String[] keys,String[] params, int[] cleaning_ids){	
		Intent newIntent = new Intent(activity, DialogSelectRemoveCleanings.class);
		newIntent.putExtra(ACTION_DO,ACTION_SELECT_REMOVE_CLEANINGS);
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
		initDefaultView();
		initCheckBoxes();
	}
	private void initDialog(){
		db = new DBAdapter(context);
		cleaningIDs = extras.getIntArray(PARAM_CLEANING_IDS);
		selectedCleaningIDs = new LinkedList<Integer>();
	}
	private void initDefaultView(){
		ok.setClickable(true);
		ok.setTextColor(Color.WHITE);
		scroll = new ScrollView(context);
		
		checkBoxContainer = new LinearLayout(context);
		checkBoxContainer.setOrientation(LinearLayout.VERTICAL);
		scroll.addView(checkBoxContainer);
		
		checkBoxes = new CheckBox[cleaningIDs.length];
		container.addView(scroll);
	}
	private void initCheckBoxes(){
		db.open();
		Cleanings cleaningsDB = new Cleanings(db);
		Employee employeeDB = new Employee(db);
		for (int i = 0 ; i < cleaningIDs.length; i++){
			checkBoxes[i] = new CheckBox(context);
			checkBoxes[i].setOnCheckedChangeListener(this);
			
			int[] cData = cleaningsDB.getData(cleaningIDs[i]);
			String[] eData = employeeDB.getData(cData[Cleanings.EID]);
			Date date = new Date(cData[Cleanings.DATE]);
			
			String text = " "+eData[Employee.FIO]+" - "+date.day+"."+date.month+"."+date.year;
			checkBoxes[i].setText(text);
			checkBoxes[i].setTag(""+cleaningIDs[i]);
			
			addCheckBox(checkBoxes[i]);
			
			checkBoxes[i].setChecked(true);
		}
		db.close();
	}
	private void addCheckBox(CheckBox checkBox){
		checkBox.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		checkBoxContainer.addView(checkBox);
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onCheckedChanged(CompoundButton view, boolean check) {
		int cleaning_id = Utils.Int(view.getTag().toString());
		if (check){
			selectedCleaningIDs.add(cleaning_id);
		}
		else {
			selectedCleaningIDs.remove((Integer)cleaning_id);
		}
	}
	@Override
	protected void okClick(){
		Intent intent = new Intent();
		int[] selectedIds = new int[selectedCleaningIDs.size()];
		for (int i = 0; i < selectedIds.length; i++){
			selectedIds[i] = selectedCleaningIDs.get(i);
		}
		intent.putExtra(PARAM_CLEANING_IDS, selectedIds);
		setResult(RESULT_OK, intent);
		finish();
	}
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}
	
}
