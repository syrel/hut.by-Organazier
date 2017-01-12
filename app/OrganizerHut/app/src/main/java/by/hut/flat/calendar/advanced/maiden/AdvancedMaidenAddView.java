package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryAddView;

public class AdvancedMaidenAddView extends EntryAddView {

	private EditText fio;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;
	private CheckBox sunday;
	
	public AdvancedMaidenAddView(Context context) {
		super(context);
		initView();
	}

	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_maiden_activity_maiden_add, this);
        inflater = null;
        fio = (EditText) this.findViewById(R.id.fio);
        monday = (CheckBox) this.findViewById(R.id.monday);
    	tuesday = (CheckBox) this.findViewById(R.id.tuesday);
    	wednesday = (CheckBox) this.findViewById(R.id.wednesday);
    	thursday = (CheckBox) this.findViewById(R.id.thursday);
    	friday = (CheckBox) this.findViewById(R.id.friday);
    	saturday = (CheckBox) this.findViewById(R.id.saturday);
    	sunday = (CheckBox) this.findViewById(R.id.sunday);
	}

	protected String getFIO(){
		assert fio != null;
		return fio.getText().toString();
	}
	
	protected boolean getMonday(){
		return this.monday.isChecked();
	}
	
	protected boolean getTuesday(){
		return this.tuesday.isChecked();
	}

	protected boolean getWednesday(){
		return this.wednesday.isChecked();
	}

	protected boolean getThursday(){
		return this.thursday.isChecked();
	}

	protected boolean getFriday(){
		return this.friday.isChecked();
	}

	protected boolean getSaturday(){
		return this.saturday.isChecked();
	}
	
	protected boolean getSunday(){
		return this.sunday.isChecked();
	}
	
	@Override
	protected void clearAllFields() {
		monday.setChecked(false);
		tuesday.setChecked(false);
		wednesday.setChecked(false);
		thursday.setChecked(false);
		friday.setChecked(false);
		saturday.setChecked(false);
		sunday.setChecked(false);
		fio.setText("");
	}

}
