package by.hut.flat.calendar.widget.list.anketa;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntry;

public class AnketaEntryView extends EntryView{

	private AnketaEntry result;
	private LinearLayout anketaView;
	private LinearLayout entryView;
	private TextView flatAddressView;
	private TextView dateView;
	private TextView telephoneLeftView;
	private TextView telephoneRightView;
	private TextView nameLeftView;
	private TextView nameRightView;
	private TextView priceView;
	private TextView costView;
	private TextView settlementView;
	private TextView evictionView;
	
	public AnketaEntryView(Context context, IEntry entry) {
		super(context,entry,true);
		initView();
	}
	
	protected void initView(){
		this.setLayout(R.layout.advanced_anketa_entry);
		result = (AnketaEntry) this.getEntry();
		anketaView = (LinearLayout) this.findViewById(R.id.anketa);
		entryView = (LinearLayout) this.findViewById(R.id.entry);
	}
	
	protected void initTelephoneLeft(){
		if(telephoneLeftView == null)telephoneLeftView = (TextView)this.findViewById(R.id.telephone_left);
		telephoneLeftView.setText(result.getTelephoneLeft());
	}
		
	protected void initTelephoneRight(){
		if(telephoneRightView == null)telephoneRightView = (TextView)this.findViewById(R.id.telephone_right);
		telephoneRightView.setText(result.getTelephoneRight());
	}
	
	protected void initNameLeft(){
		if(nameLeftView == null)nameLeftView = (TextView)this.findViewById(R.id.name_left);
		nameLeftView.setText(result.getNameLeft());
	}
	
	protected void initNameRight(){
		if(nameRightView == null)nameRightView = (TextView)this.findViewById(R.id.name_right);
		nameRightView.setText(result.getNameRight());
	}
	
	protected void initFlatAddress(){
		if(flatAddressView == null)flatAddressView = (TextView)this.findViewById(R.id.flat);
		flatAddressView.setText(result.getFlatAddress());
	}
	
	protected void initDate(){
		if(dateView == null)dateView = (TextView)this.findViewById(R.id.date);
		dateView.setText(result.getDate());
	}
	
	protected void initPrice(){
		if(priceView == null)priceView = (TextView)this.findViewById(R.id.price);
		priceView.setText(result.getPrice());
	}
	
	protected void initCost(){
		if(costView == null)costView = (TextView)this.findViewById(R.id.cost);
		costView.setText(result.getCost());
	}
	
	protected void initSettlement(){
		if(settlementView == null)settlementView = (TextView)this.findViewById(R.id.settlement);
		settlementView.setText(result.getSettlement());
	}
	
	protected void initEviction(){
		if(evictionView == null)evictionView = (TextView)this.findViewById(R.id.eviction);
		evictionView.setText(result.getEviction());
	}
	
	protected void initBackground(){
		anketaView.setBackgroundColor(result.getBackgroundColor());
	}
	
	protected void hideTelephoneNameLeft(){
		FrameLayout tel1 = (FrameLayout) this.findViewById(R.id.telephoneNameLeft);
		tel1.setVisibility(GONE);
	}
	
	protected void hideTelephoneNameRight(){
		FrameLayout tel2 = (FrameLayout) this.findViewById(R.id.telephoneNameRight);
		tel2.setVisibility(GONE);
	}
	
	protected void hideSettlement(){
		TextView settlementNameView = (TextView) this.findViewById(R.id.settlementName);
		settlementNameView.setVisibility(GONE);
		settlementView.setVisibility(GONE);
	}
	
	protected void hideEviction(){
		TextView evictionNameView = (TextView) this.findViewById(R.id.evictionName);
		evictionNameView.setVisibility(GONE);
		evictionView.setVisibility(GONE);
	}
	
	protected void hidePrice(){
		TextView priceNameView = (TextView) this.findViewById(R.id.price_text);
		priceView.setVisibility(GONE);
		priceNameView.setVisibility(GONE);
	}
	
	protected void hideCost(){
		TextView costNameView = (TextView) this.findViewById(R.id.cost_text);
		costView.setVisibility(GONE);
		costNameView.setVisibility(GONE);
	}
	
	protected void setEventEviction(){
		TextView type = (TextView) this.findViewById(R.id.type);
		type.setText(Utils.getString(getContext(), R.string.advanced_today_eviction_type));
	}
	
	protected void setEventSettlement(){
		TextView type = (TextView) this.findViewById(R.id.type);
		type.setText(Utils.getString(getContext(), R.string.advanced_today_settlement_type));
	}
	
	protected void setToday(boolean done){
		if (done)this.setFog();else setClearSky();
	}
	
	protected void setFog(){
		entryView.setBackgroundColor(0x88ffffff);
	}
	
	protected void setClearSky(){
		entryView.setBackgroundColor(0x00ffffff);
	}
	

}
