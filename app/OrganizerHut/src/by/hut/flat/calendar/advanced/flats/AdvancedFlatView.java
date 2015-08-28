package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryView;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdvancedFlatView extends EntryView{

	private final AdvancedFlat flat;
	private TextView flatIDView;
	private TextView addressView;
	private Button upButton;
	private Button downButton;
		
	public AdvancedFlatView(Context context, AdvancedFlat flat) {
		super(context);
		assert flat != null;
		this.flat = flat;
	}

	@Override
	protected void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_flats_activity_flat_layout, this);
        inflater = null;
        setActive(flat.getActive());
        initFlatIDView();
        initAddressView();
        initUpButton();
        initDownButton();
	}
	
	protected void setActive(boolean active){
		if (!active){
			this.setBackgroundColor(Color.LTGRAY);
		}
		else {
			this.setBackgroundColor(Color.WHITE);
		}
	}
	
	private void initFlatIDView(){
		flatIDView = (TextView) this.findViewById(R.id.id);
		flatIDView.setText(flat.getFlatID()+")");
	}
	
	private void initAddressView(){
		addressView = (TextView) this.findViewById(R.id.address);
		setAddress(flat.getAddress());
	}

	private void initUpButton(){
		upButton = (Button) this.findViewById(R.id.up);
		upButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				flat.clickGoUp();
			}
		});
	}
	
	private void initDownButton(){
		downButton = (Button) this.findViewById(R.id.down);
		downButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				flat.clickGoDown();
			}
		});
	}
	
	protected void setAddress(String address){
		addressView.setText(address);
	}
}
