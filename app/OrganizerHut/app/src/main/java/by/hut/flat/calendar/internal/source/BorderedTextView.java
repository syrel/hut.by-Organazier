package by.hut.flat.calendar.internal.source;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderedTextView extends TextView {
	private Paint paint = new Paint();
	public static final int BORDER_TOP = 0x00000001;
	public static final int BORDER_RIGHT = 0x00000002;
	public static final int BORDER_BOTTOM = 0x00000004;
	public static final int BORDER_LEFT = 0x00000008;

	private Border[] borders;

	public BorderedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BorderedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BorderedTextView(Context context) {
		super(context);
		init();
	}
	private void init(){
		paint.setColor(Color.BLACK);    
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(borders == null) return;

		for(Border border : borders){
			paint.setColor(border.getColor());

			if(border.getStyle() == BORDER_TOP){
				canvas.drawRect(0, 0, getWidth(), border.getWidth(), paint);                
			} else
				if(border.getStyle() == BORDER_RIGHT){
					canvas.drawRect(getWidth()- border.getWidth(), border.getWidth(), getWidth(), getHeight()-border.getWidth(), paint);
				} else
					if(border.getStyle() == BORDER_BOTTOM){
						canvas.drawRect(0, getHeight()-border.getWidth(), getWidth(), getHeight(), paint);
					} else
						if(border.getStyle() == BORDER_LEFT){
							canvas.drawRect(0, border.getWidth(), border.getWidth(), getHeight()-border.getWidth(), paint);
						}
		}
	}

	public Border[] getBorders() {
		return borders;
	}

	public void setBorders(Border[] borders) {
		this.borders = borders;
	}
	
	public void setBorder(int width, int color){
		this.setBorders(
				new Border[]{
						new Border(BorderedTextView.BORDER_TOP).setColor(color).setWidth(width),
						new Border(BorderedTextView.BORDER_RIGHT).setColor(color).setWidth(width),
						new Border(BorderedTextView.BORDER_BOTTOM).setColor(color).setWidth(width),
						new Border(BorderedTextView.BORDER_LEFT).setColor(color).setWidth(width),
				});
	}
}