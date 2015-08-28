package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryAddView;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;

public class AdvancedFlatAddView extends EntryAddView{

	private EditText address;
	private EditText shortAddress;
	
	public AdvancedFlatAddView(Context context) {
		super(context);
		initView();
	}
	
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_flats_activity_flat_add, this);
        inflater = null;
        address = (EditText) this.findViewById(R.id.address);
        shortAddress = (EditText) this.findViewById(R.id.short_address);
	}

	protected String getAddress(){
		assert address != null;
		return address.getText().toString();
	}
	
	protected String getShortAddress(){
		assert shortAddress != null;
		return shortAddress.getText().toString();
	}
	
	@Override
	protected void clearAllFields() {
		address.setText("");
		shortAddress.setText("");
	}
	
	
	

}
