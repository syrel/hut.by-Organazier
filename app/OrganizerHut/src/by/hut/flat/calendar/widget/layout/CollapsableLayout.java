package by.hut.flat.calendar.widget.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Utils;

public class CollapsableLayout extends LinearLayout implements OnClickListener, OnLongClickListener  {
	private Context context;
	private FrameLayout titleContainer;
	private TextView titleText;
	private Button expandButton;
	private Hiddable hiddable;
	private Hiddable longHiddable;
	
	public CollapsableLayout(Context context) {
		super(context);
		this.context = context;
		initLayout();
	}
	
	private CollapsableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private CollapsableLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void initLayout(){
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		this.setOrientation(VERTICAL);
		initTitleContainer();
	}
	
	private void initTitleContainer(){
		titleContainer = new FrameLayout(context);
		titleContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		titleContainer.setBackgroundResource(R.drawable.statistics_title_bg_normal);
		initTitleText();
		initButton();
		this.addView(titleContainer);
	}
	
	private void initTitleText(){
		titleText = new TextView(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.LEFT | Gravity.CENTER_VERTICAL);
		params.setMargins((int) Utils.convertDpToPx(10, Config.INST.DISPLAY.DPI), 0, 0, 0);
		titleText.setTextColor(Color.WHITE);
		titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		titleContainer.addView(titleText,params);
	}
	
	private void initButton(){
		expandButton = new Button(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		expandButton.setBackgroundResource(R.drawable.ic_menu_expand);
		titleContainer.addView(expandButton,params);
		expandButton.setGravity(Gravity.RIGHT);
		titleContainer.setOnClickListener(this);
		expandButton.setOnClickListener(this);
		titleContainer.setOnLongClickListener(this);
		expandButton.setOnLongClickListener(this);
	}
	
	public void setTitleText(String title){
		titleText.setText(title);
	}
	
	public void setHiddable(Hiddable hiddable){
		if (this.hiddable != null){
			this.removeView(this.hiddable.getView());
		}
		this.hiddable = hiddable;
		this.addView(this.hiddable.getView());
	}
	
	public void setLongHiddable(Hiddable longHiddable){
		if (this.longHiddable != null){
			this.removeView(this.longHiddable.getView());
		}
		this.longHiddable = longHiddable;
		this.addView(this.longHiddable.getView());
	}

	@Override
	public void onClick(View v) {
		if (longHiddable != null){
			if (longHiddable.isVisible()){
				longHiddable.hide();
				expandButton.setBackgroundResource(R.drawable.ic_menu_expand);
				return;
			}
		}
		
		if (hiddable != null && !hiddable.isLocked()){
			if (hiddable.isVisible()){
				hiddable.hide();
				expandButton.setBackgroundResource(R.drawable.ic_menu_expand);
			}
			
			else {
				hiddable.show();
				expandButton.setBackgroundResource(R.drawable.ic_menu_collapse);
			}
		}
	}
	
	@Override
	public boolean onLongClick(View v){
		if (longHiddable == null) return false;
		if (hiddable != null) {
			if (hiddable.isVisible()){
				hiddable.hide();
				expandButton.setBackgroundResource(R.drawable.ic_menu_expand);
			}
		}
		
		if (longHiddable != null && !longHiddable.isLocked()){
			if (longHiddable.isVisible()){
				longHiddable.hide();
				expandButton.setBackgroundResource(R.drawable.ic_menu_expand);
			}
			
			else {
				longHiddable.show();
				expandButton.setBackgroundResource(R.drawable.ic_menu_collapse);
			}
		}
		
		return true;
	}
}
