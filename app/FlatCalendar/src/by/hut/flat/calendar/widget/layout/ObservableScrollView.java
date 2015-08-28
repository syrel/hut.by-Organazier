package by.hut.flat.calendar.widget.layout;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Utils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

	private float mOldX = 0;
	private float mOldY = 0;
	private float clickSize = Utils.convertDpToPx(20, Config.INST.DISPLAY.DPI);
	private boolean mStartedScoll = false;
	private boolean reInitScroll = false;
	
	public interface ScrollViewListener {
	    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
	}
	
    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int action = event.getAction();
    	float x = event.getX();
		float y = event.getY();
		if (reInitScroll){
			mOldX = x;
			mOldY = y;
			reInitScroll = false;
		}
		switch(action){
			case MotionEvent.ACTION_DOWN:{
				mOldX = x;
				mOldY = y;
				break;
			}
            case MotionEvent.ACTION_MOVE:{
            	if (!mStartedScoll){
            		mStartedScoll = true;
                	return false;
            	}
            	
            	onScrollChanged((int)x,(int)y,(int)mOldX,(int)mOldY);
            	if (Math.abs(x-mOldX) <= clickSize && Math.abs(y-mOldY) <= clickSize){
        			return false;
        		}
            	return true;
            }
            case MotionEvent.ACTION_UP: {
            	if (mStartedScoll){
            		onScrollChanged((int)x,(int)y,(int)mOldX,(int)mOldY);
            		mStartedScoll = false;
            	}
            	return false;
            }
        }
    	return false;
    }
    
    public void reInitScrollEvent(){
    	reInitScroll = true;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	super.onInterceptTouchEvent(event);
    	return onTouchEvent(event);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
    	return true;
    }
}