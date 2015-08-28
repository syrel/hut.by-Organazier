package by.hut.flat.calendar.widget.layout;

import java.util.ArrayList;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TextedTableRow extends TableRow{

	private TableRow.LayoutParams textBorderParams;
	private TableRow.LayoutParams textNoBorderParams;
	
	private Context context;
	
	private int textPadding;
	private int borderWidth;
	private float textSize = 12;
	private int textColor = Color.BLACK;
	private int backgroundColor = 0x00000000;
	private boolean bold = false;
	
	private ArrayList<TextView> columns;
	
	public TextedTableRow(Context context) {
		this(context,null);
	}
	
	public TextedTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.columns = new ArrayList<TextView>();
		initView();
	}
	
	public void initView(){
		initBorderLayoutParams();
		initNoBorderLayoutParams();
		
		this.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT));
	}
	
	private void initBorderLayoutParams(){
		textBorderParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
		textBorderParams.weight = 1;
	}
	
	private void initNoBorderLayoutParams(){
		textNoBorderParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
		textNoBorderParams.weight = 1;
	}
	
	public void setParams(String[] values){
		initParams(values);
	}
	
	private void initParams(String[] values){
		this.removeAllViews();
		this.columns = new ArrayList<TextView>();
		int columnNum = values.length;
		for (int i = 0; i < columnNum; i++){
			this.columns.add(initColumn(values, i));
		}
	}
	
	private TextView initColumn(String[] values, int index) {
		TextView textView = new TextView(context);
		if (index == 0)	textView.setLayoutParams(textNoBorderParams);
		else textView.setLayoutParams(textBorderParams);
		textView.setText(values[index]);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		textView.setTextColor(textColor);
		textView.setBackgroundColor(backgroundColor);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(0, textPadding, 0, textPadding);
		if (bold)textView.setTypeface(null,Typeface.BOLD);
		
		this.addView(textView);
		return textView;
	}
	
	public TextedTableRow setPadding(float dp){
		this.textPadding = (int) Utils.convertDpToPx(dp, Config.INST.DISPLAY.DPI);
		for (TextView textView : columns){
			textView.setPadding(0, textPadding, 0, textPadding);
		}
		return this;
	}
	
	public TextedTableRow setBorderWidth(float dp){
		this.borderWidth = (int) Utils.convertDpToPx(dp, Config.INST.DISPLAY.DPI);
		textBorderParams.bottomMargin = borderWidth;
		textBorderParams.leftMargin = borderWidth;
		textNoBorderParams.bottomMargin = borderWidth;
		return this;
	}
	
	public TextedTableRow setTextSize(float sp){
		this.textSize = sp;
		for (TextView textView : columns){
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		}
		return this;
	}
	
	public TextedTableRow setTextColor(int color){
		this.textColor = color;
		for (TextView textView : columns){
			textView.setTextColor(textColor);
		}
		return this;
	}
	
	@Override
	public void setBackgroundColor(int color){
		backgroundColor = color;
		for (TextView textView : columns){
			textView.setBackgroundColor(backgroundColor);
		}
	}
	
	public TextedTableRow setBold(boolean bold){
		this.bold = bold;
		for (TextView textView : columns){
			textView.setTypeface(null,(bold)?Typeface.BOLD:Typeface.NORMAL);
		}
		return this;
	}

	

}
