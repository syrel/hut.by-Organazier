package by.hut.flat.calendar.internal;

import android.text.Layout;
import android.text.TextPaint;

public interface LayoutProvider {
	Layout getLayout(CharSequence text, int width, TextPaint textPaint);
}
