package by.hut.flat.calendar.anketa;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.internal.source.Border;
import by.hut.flat.calendar.internal.source.BorderedTextView;
import by.hut.flat.calendar.utils.Utils;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AnketaPagerFrame extends FrameLayout implements Settable{
		
	public static final String PREFIX = "flat_param_",PREFIX_NAME = "flat_param_name_";
	
	public static final String TELEPHONE_LEFT = "tel_left",TELEPHONE_RIGHT = "tel_right",
			NAME_LEFT = "name_left", NAME_RIGHT = "name_right", SETTLEMENT = "settlement_date",
			EVICTION = "eviction_date",PRICE = "cost_day", SUMM = "cost_full", DEBT = "debt_sum",
			PREPAY = "prepay_sum", NOTE = "note",ELSE_INFO = "else_info",ANKETA = "anketa";
	
	private String[] paramNamesText;
	
	public static final String[] PARAM_NAMES = new String[]{
		TELEPHONE_LEFT,
		TELEPHONE_RIGHT,
		NAME_LEFT,
		NAME_RIGHT,
		PRICE,
		SUMM,
		SETTLEMENT,
		EVICTION,
		DEBT,
		PREPAY,
		NOTE,
		ELSE_INFO,
		ANKETA
	};
	
	public AnketaPagerFrame(Context context) {
        super(context);
        paramNamesText = context.getResources().getStringArray(R.array.anketa_param_names);
        initComponent();
    }
	private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.anketa_activity_page_layout, this);
        initParamNames();
    }
	
	/*------------------------------------------------------------
	-------------------------- S E T T E R S ---------------------
	------------------------------------------------------------*/
	
	@Override
	public void setTelephoneLeft(String tel){
		setIntervalParamValue((tel != null) ? tel : "", TELEPHONE_LEFT);
	}
	
	@Override
	public void setTelephoneRight(String tel){
		setIntervalParamValue((tel != null) ? tel : "", TELEPHONE_RIGHT);
	}
	
	@Override
	public void setNameLeft(String name){
		setIntervalParamValue((name != null) ? name : "",NAME_LEFT);
	}
	
	@Override
	public void setNameRight(String name){
		setIntervalParamValue((name != null) ? name : "",NAME_RIGHT);
	}
		
	@Override
	public void setPrice(int price){
		setIntervalParamValue((price > 0) ? ""+price : "", PRICE);
		if (price <= 0)	initRedBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.PRICE));
		else if (this.getSumm() > 0 && this.getSumm() > price){
			initYellowBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.SUMM));
			initYellowBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.PRICE));
		}
	}
	
	@Override
	public void setSumm(int summ){
		setIntervalParamValue((summ > 0) ? ""+summ : "", SUMM);
		if (summ <= 0)	initRedBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.SUMM));
		else if (this.getPrice() > 0 && this.getPrice() > summ){
			initYellowBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.SUMM));
			initYellowBorder((BorderedTextView) findView(PREFIX+AnketaPagerFrame.PRICE));
		}
	}
		
	@Override
	public void setDebt(int debt){
		setIntervalParamValue((debt > 0) ? ""+debt : "", DEBT);
	}
	
	@Override
	public void setPrepay(int prepay){
		setIntervalParamValue((prepay > 0) ? ""+prepay : "", PREPAY);
	}
	
	@Override
	public void setSettlement(String settlement){
		setIntervalParamValue((settlement != null) ? settlement : "",SETTLEMENT);
	}
	
	@Override
	public void setEviction(String eviction){
		setIntervalParamValue((eviction != null) ? eviction : "",EVICTION);
	}
	
	@Override
	public void setNote(String note){
		setIntervalParamValue((note != null) ? note : "",NOTE);
	}
	
	@Override
	public void setElseInfo(String elseInfo){
		setIntervalParamValue((elseInfo != null) ? elseInfo : "",ELSE_INFO);
	}
	
	@Override
	public void setAnketa(String anketa){
		setIntervalParamValue((anketa != null) ? anketa : "",ANKETA);
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public int getPrice() {
		String value = getIntervalParamValue(PRICE);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {amount = Integer.parseInt(value);}
		catch (NumberFormatException e){}
		if (amount < 0) amount = 0;
		return amount;
	}
	
	public int getSumm() {
		String value = getIntervalParamValue(SUMM);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {amount = Integer.parseInt(value);}
		catch (NumberFormatException e){}
		if (amount < 0) amount = 0;
		return amount;
	}
	
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	
	private void initBorder(BorderedTextView view, int color) {
		view.setBorders(
				new Border[]{
						new Border(BorderedTextView.BORDER_TOP).setColor(color).setWidth((int)Utils.convertDpToPx(1.5f, Config.INST.DISPLAY.DPI)),
						new Border(BorderedTextView.BORDER_RIGHT).setColor(color).setWidth((int)Utils.convertDpToPx(1.5f, Config.INST.DISPLAY.DPI)),
						new Border(BorderedTextView.BORDER_BOTTOM).setColor(color).setWidth((int)Utils.convertDpToPx(1.5f, Config.INST.DISPLAY.DPI)),
						new Border(BorderedTextView.BORDER_LEFT).setColor(color).setWidth((int)Utils.convertDpToPx(1.5f, Config.INST.DISPLAY.DPI))
				});
	}
	
	private void initRedBorder(BorderedTextView view) {
		initBorder(view, Color.RED);
	}
	
	private void initYellowBorder(BorderedTextView view){
		initBorder(view, Color.YELLOW);
	}
	
	private void initParamNames(){
		for (int i = 0; i < PARAM_NAMES.length; i++){
			setIntervalParamName(paramNamesText[i],PARAM_NAMES[i]);
		}
	}
	
	private String getIntervalParamValue(String resource){
		return ((TextView)findView(PREFIX + resource)).getText().toString();
	}
	
	private void setIntervalParamValue(String value, String resource) {
		((TextView)findView(PREFIX + resource)).setText(value);
	}
	
	private void setIntervalParamName(String name, String resource) {
		((TextView)findView(PREFIX_NAME + resource)).setText(name);
	}
	
	private View findView(String res) {
		int resID = getResources().getIdentifier(res, "id", Config.PACKAGE_NAME);
		View v = findViewById(resID);
		return v;
	}
}
