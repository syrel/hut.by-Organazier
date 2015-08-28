package by.hut.flat.calendar.anketa;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AnketaPagerEditFrame extends FrameLayout implements Visitable,Settable{
	protected final static String PREFIX = "flat_param_edit_";
	private final static String PREFIX_NAME = "flat_param_edit_name_";
	
	private final static int[] INPUT_METHODS = {
		InputType.TYPE_CLASS_PHONE,							// TELEPHONE_LEFT
		InputType.TYPE_CLASS_PHONE,							// TELEPHONE_RIGHT
		InputType.TYPE_TEXT_VARIATION_PERSON_NAME,			// NAME_LEFT
		InputType.TYPE_TEXT_VARIATION_PERSON_NAME,			// NAME_RIGHT
		InputType.TYPE_CLASS_NUMBER,						// PRICE
		InputType.TYPE_CLASS_NUMBER,						// SUMM
		InputType.TYPE_CLASS_TEXT,							// SETTLEMENT
		InputType.TYPE_CLASS_TEXT,							// EVICTION
		InputType.TYPE_CLASS_NUMBER,						// DEBT
		InputType.TYPE_CLASS_NUMBER,						// PREPAY
		InputType.TYPE_CLASS_TEXT,							// NOTE
		-1,													// ELSE_INFO
		-1													// ANKETA
	};
	
	private String[] paramNamesText;
	
	public AnketaPagerEditFrame(Context context) {
        super(context);
        paramNamesText = context.getResources().getStringArray(R.array.anketa_param_names);
        initComponent();
    }
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	
	private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.anketa_activity_page_edit_layout, this);
        initInputMethod();
        initParamNames();
    }
	
	private void initParamNames(){
		for (int i = 0,length = AnketaPagerFrame.PARAM_NAMES.length; i < length; i++){
			setIntervalParamName(paramNamesText[i],AnketaPagerFrame.PARAM_NAMES[i]);
		}
	}
	
	public void initInputMethod(){
		for (int i = 0,length = AnketaPagerFrame.PARAM_NAMES.length; i < length; i++){
			if (INPUT_METHODS[i] != -1) setIntervalParamInputType(INPUT_METHODS[i], AnketaPagerFrame.PARAM_NAMES[i]);
		}
	}
	/*------------------------------------------------------------
	-------------------------- S E T T E R S ---------------------
	------------------------------------------------------------*/
	@Override
	public void setTelephoneLeft(String tel){
		setIntervalParamValue((tel != null) ? tel : "", AnketaPagerFrame.TELEPHONE_LEFT);
	}
	
	@Override
	public void setTelephoneRight(String tel){
		setIntervalParamValue((tel != null) ? tel : "", AnketaPagerFrame.TELEPHONE_RIGHT);
	}

	@Override
	public void setNameLeft(String name){
		setIntervalParamValue((name != null) ? name : "",AnketaPagerFrame.NAME_LEFT);
	}

	@Override
	public void setNameRight(String name){
		setIntervalParamValue((name != null) ? name : "",AnketaPagerFrame.NAME_RIGHT);
	}

	@Override
	public void setPrice(int price){
		setIntervalParamValue((price > 0) ? ""+price : "", AnketaPagerFrame.PRICE);
	}

	@Override
	public void setSumm(int summ){
		setIntervalParamValue((summ > 0) ? ""+summ : "", AnketaPagerFrame.SUMM);
	}

	@Override
	public void setDebt(int debt){
		setIntervalParamValue((debt > 0) ? ""+debt : "", AnketaPagerFrame.DEBT);
	}

	@Override
	public void setPrepay(int prepay){
		setIntervalParamValue((prepay > 0) ? ""+prepay : "", AnketaPagerFrame.PREPAY);
	}

	@Override
	public void setSettlement(String settlement){
		setIntervalParamValue((settlement != null) ? settlement : "",AnketaPagerFrame.SETTLEMENT);
	}

	@Override
	public void setEviction(String eviction){
		setIntervalParamValue((eviction != null) ? eviction : "",AnketaPagerFrame.EVICTION);
	}

	@Override
	public void setNote(String note){
		setIntervalParamValue((note != null) ? note : "",AnketaPagerFrame.NOTE);
	}
	
	public void setElseInfo(String elseInfo){
		setIntervalParamValue((elseInfo != null) ? elseInfo : "",AnketaPagerFrame.ELSE_INFO);
	}

	@Override
	public void setAnketa(String anketa){
		setIntervalParamValue((anketa != null) ? anketa : "",AnketaPagerFrame.ANKETA);
	}
	
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public String getTelephoneLeft(){
		return getIntervalParamValue(AnketaPagerFrame.TELEPHONE_LEFT);
	}
	
	public String getTelephoneRight(){
		return getIntervalParamValue(AnketaPagerFrame.TELEPHONE_RIGHT);
	}
	
	public String getNameLeft(){
		return getIntervalParamValue(AnketaPagerFrame.NAME_LEFT);
	}
	
	public String getNameRight(){
		return getIntervalParamValue(AnketaPagerFrame.NAME_RIGHT);
	}
	
	public int getPrice() throws MoneyParseException {
		String value = getIntervalParamValue(AnketaPagerFrame.PRICE);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {
			amount = Integer.parseInt(value);
		}
		catch (NumberFormatException e){
			throw new MoneyParseException(MoneyParseException.NUMERIC);
		}
		if (amount < 0) throw new MoneyParseException(MoneyParseException.NEGATIVE);
		return amount;
	}
	
	public int getSumm() throws MoneyParseException {
		String value = getIntervalParamValue(AnketaPagerFrame.SUMM);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {
			amount = Integer.parseInt(value);
		}
		catch (NumberFormatException e){
			throw new MoneyParseException(MoneyParseException.NUMERIC);
		}
		if (amount < 0) throw new MoneyParseException(MoneyParseException.NEGATIVE);
		return amount;
	}
	
	public int getDebt() throws MoneyParseException {
		String value = getIntervalParamValue(AnketaPagerFrame.DEBT);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {
			amount = Integer.parseInt(value);
		}
		catch (NumberFormatException e){
			throw new MoneyParseException(MoneyParseException.NUMERIC);
		}
		if (amount < 0) throw new MoneyParseException(MoneyParseException.NEGATIVE);
		return amount;
	}
	
	public int getPrepay() throws MoneyParseException {
		String value = getIntervalParamValue(AnketaPagerFrame.PREPAY);
		if (value.length() == 0) return 0;
		int amount = 0;
		try {
			amount = Integer.parseInt(value);
		}
		catch (NumberFormatException e){
			throw new MoneyParseException(MoneyParseException.NUMERIC);
		}
		if (amount < 0) throw new MoneyParseException(MoneyParseException.NEGATIVE);
		return amount;
	}
	
	public String getSettlement(){
		return getIntervalParamValue(AnketaPagerFrame.SETTLEMENT);
	}
	
	public String getEviction(){
		return getIntervalParamValue(AnketaPagerFrame.EVICTION);
	}
	
	public String getNote(){
		return getIntervalParamValue(AnketaPagerFrame.NOTE);
	}
	
	public String getElseInfo(){
		return getIntervalParamValue(AnketaPagerFrame.ELSE_INFO);
	}
	
	public String getAnketa(){
		return getIntervalParamValue(AnketaPagerFrame.ANKETA);
	}
	
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	private void setIntervalParamValue(String value, String resource) {
		((EditText)findView(PREFIX + resource)).setText(value);
	}
	
	private void setIntervalParamName(String name, String resource) {
		((TextView)findView(PREFIX_NAME + resource)).setText(name);
	}
	
	private void setIntervalParamInputType(int inputType, String resource){
		((EditText)findView(PREFIX + resource)).setInputType(inputType);
	}
	
	private String getIntervalParamValue(String resource){
		return ((EditText)findView(PREFIX + resource)).getText().toString();
	}
	
	protected View findView(String res) {
		int resID = getResources().getIdentifier(res, "id", Config.PACKAGE_NAME);
		View v = findViewById(resID);
		return v;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}