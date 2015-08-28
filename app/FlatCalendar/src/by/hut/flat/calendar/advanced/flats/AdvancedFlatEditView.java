package by.hut.flat.calendar.advanced.flats;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryEditView;

public class AdvancedFlatEditView extends EntryEditView{
	
	private AdvancedFlatEdit flatEdit;
	
	private EditText address;
	private EditText shortAddress;
	private CheckBox hidden;
	private CheckBox useShortAddressSausage;
	
	public AdvancedFlatEditView(Context context) {
		super(context);
	}
	
	protected void initView(AdvancedFlatEdit flatEdit){
		this.flatEdit = flatEdit;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_flats_activity_flat_edit, this);
        inflater = null;
        initAddressView();
        initShortAddressView();
        initHiddenView();
        initUseShortAddressSausageView();
	}

	private void initAddressView(){
        address = (EditText) this.findViewById(R.id.address);
        address.setText(flatEdit.getFlat().getAddress());
	}
	
	private void initShortAddressView(){
        shortAddress = (EditText) this.findViewById(R.id.shortAddress);
        shortAddress.setText(flatEdit.getFlat().getShortAddress());
	}
	
	private void initHiddenView(){
		hidden = (CheckBox) this.findViewById(R.id.hidden);
		hidden.setChecked(!flatEdit.getFlat().getActive());
	}
	
	private void initUseShortAddressSausageView(){
		useShortAddressSausage = (CheckBox) this.findViewById(R.id.use_short_address_sausage);
		useShortAddressSausage.setChecked(flatEdit.getFlat().getUseShortAddressSausage());
	}
	
	protected String getAddress(){
		assert address != null;
		return address.getText().toString();
	}
	
	protected String getShortAddress(){
		assert shortAddress != null;
		return shortAddress.getText().toString();
	}
	
	protected boolean getActive(){
		assert hidden != null;
		return !hidden.isChecked();
	}

	public boolean getUseShortAddressSausage() {
		assert useShortAddressSausage != null;
		return useShortAddressSausage.isChecked();
	}

}
