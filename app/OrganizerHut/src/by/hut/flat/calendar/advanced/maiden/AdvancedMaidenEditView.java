package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryEditView;

public class AdvancedMaidenEditView extends EntryEditView{

	private AdvancedMaidenEdit maidenEdit;
	
	private EditText fio;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;
	private CheckBox sunday;
	private CheckBox hidden;
	
	public AdvancedMaidenEditView(Context context) {
		super(context);
	}
	
	protected void initView(AdvancedMaidenEdit maidenEdit){
		assert maidenEdit != null;
		this.maidenEdit = maidenEdit;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_maiden_activity_maiden_edit, this);
        inflater = null;
        initFioView();
        initWorkView();
        initHiddenView();
	}

	private void initFioView(){
        fio = (EditText) this.findViewById(R.id.fio);
        fio.setText(maidenEdit.getMaiden().getFIO());
	}
	
	private void initWorkView(){
		monday = (CheckBox) this.findViewById(R.id.monday);
		monday.setChecked(maidenEdit.getMaiden().isMonday());
    	tuesday = (CheckBox) this.findViewById(R.id.tuesday);
    	tuesday.setChecked(maidenEdit.getMaiden().isTuesday());
    	wednesday = (CheckBox) this.findViewById(R.id.wednesday);
    	wednesday.setChecked(maidenEdit.getMaiden().isWednesday());
    	thursday = (CheckBox) this.findViewById(R.id.thursday);
    	thursday.setChecked(maidenEdit.getMaiden().isThursday());
    	friday = (CheckBox) this.findViewById(R.id.friday);
    	friday.setChecked(maidenEdit.getMaiden().isFriday());
    	saturday = (CheckBox) this.findViewById(R.id.saturday);
    	saturday.setChecked(maidenEdit.getMaiden().isSaturday());
    	sunday = (CheckBox) this.findViewById(R.id.sunday);
    	sunday.setChecked(maidenEdit.getMaiden().isSunday());
	}
	
	private void initHiddenView(){
		hidden = (CheckBox) this.findViewById(R.id.hidden);
		hidden.setChecked(!maidenEdit.getMaiden().getActive());
	}
	
	protected String getFIO(){
		assert fio != null;
		return fio.getText().toString();
	}
		
	protected boolean getActive(){
		assert hidden != null;
		return !hidden.isChecked();
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

}
