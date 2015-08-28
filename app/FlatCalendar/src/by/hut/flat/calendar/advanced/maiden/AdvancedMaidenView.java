package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.widget.list.complex.EntryView;

public class AdvancedMaidenView extends EntryView {

	private final AdvancedMaiden maiden;
	private TextView maidenIDView;
	private TextView fioView;
	private Button upButton;
	private Button downButton;
	
	public AdvancedMaidenView(Context context, AdvancedMaiden maiden) {
		super(context);
		assert maiden != null;
		this.maiden = maiden;
	}
	
	@Override
	protected void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advanced_maiden_activity_maiden_layout, this);
        inflater = null;
        setActive(maiden.getActive());
        initIDView();
        initFIOView();
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
	
	private void initIDView(){
		maidenIDView = (TextView) this.findViewById(R.id.id);
		maidenIDView.setText(maiden.getID()+")");
	}
	
	private void initFIOView(){
		fioView = (TextView) this.findViewById(R.id.fio);
		setFIO(maiden.getFIO());
	}

	private void initUpButton(){
		upButton = (Button) this.findViewById(R.id.up);
		upButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				maiden.clickGoUp();
			}
		});
	}
	
	private void initDownButton(){
		downButton = (Button) this.findViewById(R.id.down);
		downButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				maiden.clickGoDown();
			}
		});
	}
	
	protected void setFIO(String fio){
		fioView.setText(fio);
	}

}
