package by.hut.flat.calendar.widget.layout;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class TitledTableLayout extends TableLayout{
	private static final int BACKGROUND_COLOR = 0xffffbf3f;
	
	private Context context;	
	private TextTableRow titleRow;
	private ArrayList<TextTableRow> rows;

	public TitledTableLayout(Context context) {
		this(context,null);
	}

	private TitledTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.rows = new ArrayList<TextTableRow>();
		initView();
	}
	
	private void initView(){
		this.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT));
		initTitle();
		setTitleColor(Color.WHITE);
		setTitleTextSize(14);
		setTitlePadding(3);
		this.setBackgroundColor(BACKGROUND_COLOR);
	}
	
	private void initTitle(){
		titleRow = new TextTableRow(context);
		this.addView(titleRow);
	}

	private TextTableRow initRow(String[] values){
		TextTableRow row = new TextTableRow(context);
		row.setBorderWidth(1);
		row.setTextSize(12);
		row.setPadding(2);
		row.setTextColor(Color.BLACK);
		row.setBackgroundColor(Color.WHITE);
		row.setBold(false);
		row.setParams(values);
		rows.add(row);
		this.addView(row);
		return row;
	}
	
	public void setTitleText(String title){
		titleRow.setParams(new String[]{title});
	}
	
	public void setTitleColor(int color){
		titleRow.setTextColor(color);
	}
	
	public void setTitleTextSize(float sp){
		titleRow.setTextSize(sp);
	}
	
	public void setTitlePadding(float dp){
		titleRow.setPadding(dp);
	}
	
	public TextTableRow addRow(String... values){
		return initRow(values);
	}
}
