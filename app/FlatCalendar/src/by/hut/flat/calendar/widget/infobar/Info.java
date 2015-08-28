package by.hut.flat.calendar.widget.infobar;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.internal.source.BorderedTextView;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class Info extends LinearLayout implements IInfo{
	private static final int rightMargin = 4; 	// rightMargin in dp
	private static final int topMargin = 2; 	// topMargin in dp
	private static final int bottomMargin = 2; 	// bottomMargin in dp
	private static final int leftPadding = 6;	// leftPadding in dp
	private static final int rightPadding = 6;	// rightPadding in dp
	private static final int borderWidth = 1;	// borderWidth in dp
	
	private int backgroundColor = Color.RED;;
	private int textColor = Color.WHITE;
	private int textSize = 26;		//text size in px
	
	private Activity activity;
	private Context context;
	private BorderedTextView infoView;
	private IInfoBar infoBar;
	
	private String info;
	private String tag;
	
	private boolean visible;

	public Info(Context context) {
		super(context);
		this.context = context;
		initView();
	}
		
	private void initView(){
		initLayout();
		initInfoView();
	}
	
	private void initLayout(){
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.rightMargin = (int) Utils.convertDpToPx(rightMargin, Config.INST.DISPLAY.DPI);
		lp.topMargin = (int) Utils.convertDpToPx(topMargin, Config.INST.DISPLAY.DPI);
		lp.bottomMargin = (int) Utils.convertDpToPx(bottomMargin, Config.INST.DISPLAY.DPI);
		this.setLayoutParams(lp);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(backgroundColor);
		setVisible(false);
	}
	
	private void initInfoView(){
		infoView = new BorderedTextView(context);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		infoView.setLayoutParams(lp);
		infoView.setText(info);
		infoView.setTextColor(textColor);
		infoView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
		infoView.setGravity(Gravity.CENTER_VERTICAL);
		infoView.setPadding((int) Utils.convertDpToPx(leftPadding, Config.INST.DISPLAY.DPI), 0, (int) Utils.convertDpToPx(rightPadding, Config.INST.DISPLAY.DPI), 0);
		infoView.setBorder((int) Utils.convertDpToPx(borderWidth, Config.INST.DISPLAY.DPI), 0x88ffffff);
		infoView.setBackgroundResource(R.drawable.info_bar_notification_bg);
		infoView.setShadowLayer(2, 1, 2, 0x50000000);
		this.addView(infoView);
	}
	
	public void setInfoText(String info){
		this.info = info;
		this.infoView.setText(this.info);
	}
	
	public void setInfoTextColor(int color){
		this.textColor = color;
		this.infoView.setTextColor(textColor);
	}
	
	public void setInfoTextSize(int size){
		this.textSize = size;
		this.infoView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	}
	
	@Override
	public void setBackgroundColor(int color){
		super.setBackgroundColor(color);
		this.backgroundColor = color;
	}
	
	@Override
	public String getTag(){
		return tag;
	}

	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public View getView() {
		return this;
	}
	
	@Override
	public void bind(IInfoBar infoBar){
		if (this.infoBar == null){
			this.infoBar = infoBar;
		}
	}
	
	@Override
	public void unbind(IInfoBar infoBar){
		if (this.infoBar != null && this.infoBar.contains(this)){
			this.infoBar = null;
		}
	}
	
	@Override
	public void show(){
		if (!this.visible){
			setVisible(true);
		}
	}

	@Override
	public void hide() {
		if (this.visible){
			setVisible(false);
		}
	}

	@Override
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Activity getActivity() {
		return activity;
	}
	
	private void setVisible(boolean visible){
		this.visible = visible;
		if (visible)this.setVisibility(View.VISIBLE);
		else this.setVisibility(View.GONE);
	}

}
