package by.hut.flat.calendar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;

public abstract class DialogFloating extends Dialog{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogFloating";

	public static final String DIALOG_FLOAT_VIEW_POSITION = "dialog_float_view_position";

	private LinearLayout container;
	
	protected static void show(Activity context,int requestCode,View view, Bundle extras, Class<?> cls){
		Intent newIntent = new Intent(context, cls);
		newIntent.putExtras(extras);
		newIntent.putExtra(DIALOG_FLOAT_VIEW_POSITION, calcLocation(view));
		context.startActivityForResult(newIntent,requestCode);
	}

	private static int[] calcLocation(View view){
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		int width = view.getWidth();
		int height = view.getHeight();
		return new int[]{x,y,width,height};
	}

	@Override
	public void onInitContentView() {
		final int[] viewMeasure = extras.getIntArray(DIALOG_FLOAT_VIEW_POSITION);
		this.setFinishOnTouchOutside(true);
		setContentView(R.layout.dialog_float_activity);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_float_layout);
		container = (LinearLayout) findViewById(R.id.dialog_float_container);
		ViewTreeObserver vto = dialogLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				int dialogHeight = dialogLayout.getHeight();
				if (dialogHeight > Config.INST.DISPLAY.HEIGHT/2){
					dialogHeight = Config.INST.DISPLAY.HEIGHT/2;
				}
				int[] dialogLocation = calcDialogLocation(viewMeasure,new int[]{dialogLayout.getWidth(),dialogHeight});
				setDialogLocation(dialogLocation,dialogHeight);
				ViewTreeObserver obs = dialogLayout.getViewTreeObserver();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					obs.removeOnGlobalLayoutListener(this);
				} else {
					obs.removeGlobalOnLayoutListener(this);
				}
			}
		});
	}

	private int[] calcDialogLocation(int[] viewMeasure, int[] dialogMeasure){
		int screenWidth = Config.INST.DISPLAY.WIDTH;
		int screenHeight = Config.INST.DISPLAY.HEIGHT;
		int statusBarHeight = Config.INST.DISPLAY.STATUS_BAR_HEIGHT;
		int dialogWidth = dialogMeasure[0];
		int dialogHeight = dialogMeasure[1];
		int viewWidth = viewMeasure[2];
		int viewHeight = viewMeasure[3];
		int viewX = viewMeasure[0];
		int viewY = viewMeasure[1];

		boolean underView = (viewY+viewHeight+dialogHeight <= screenHeight);
		boolean leftToView = (viewX+dialogWidth <= screenWidth);

		int x,y = 0;
		if (underView)y = viewY + viewHeight-statusBarHeight;
		else y = viewY - dialogHeight-statusBarHeight;
		if (leftToView)	x = viewX;
		else x = viewX + viewWidth - dialogWidth;
		return new int[]{x,y};
	}

	private void setDialogLocation(int[] dialogLocation,int dialogHeight){
		WindowManager.LayoutParams params = DialogFloating.this.getWindow().getAttributes();
		DialogFloating.this.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
		params.x = dialogLocation[0];
		params.y = dialogLocation[1];
		params.height = dialogHeight;
		getWindow().setAttributes(params);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}
	
	@Override
	public LinearLayout getLayout(){
		return container;
		
	}
}
