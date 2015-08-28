package by.hut.flat.calendar.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utils {
	
	/** Calculates signum-function of numbers */
	public static final int signum(long value) { 
		if (value > 0) return 1; 
		if (value < 0) return -1 ; 
		else return 0; 
	}
	
	public static final int Int(String str){
		int n = 0;
		int j = 0;
		boolean negative = false;
		if (str.charAt(0)=='-'){
			j = 1;
			negative = true;
		}
		for (int i = j; i < str.length();i++){
			n = n * 10 +(str.charAt(i)-'0');
		}
		return n*((negative)?-1:1);
	}
	
	public static final int[] split(String str,char del,int num){
		int[] array = new int[num];
		int k = 0;
		for (int i = 0; i < str.length();i++){
			int ch = str.charAt(i);
			if (ch == del) k++;
			else array[k] = array[k]*10+(str.charAt(i)-'0');
		}
		return array;
	}
	
	public static final byte[] intToByteArray(int value) {
		return new byte[] {
			(byte)(value >>> 24),
			(byte)(value >>> 16),
			(byte)(value >>> 8),
			(byte)value
		};
	}
	
	public static final int[][] transpose(int[][] array){
		if (array.length == 0) return new int[0][0];
		if (array[0].length == 0) return new int[0][array.length];
		int[][] result = new int[array[0].length][array.length];
		for (int i = 0; i < array.length; i++){
			for (int j = 0; j < array[0].length; j++){
				result[j][i] = array[i][j];
			}
		}
		return result;
	}
	
	public static final String[] getRow(String[][] array, int row){
		assert row >= 0 && (row < array.length || row == 0);
		if (array == null) return null;
		if (array.length == 0) return new String[0];
		String[] result = new String[array[row].length];
		System.arraycopy(array[row], 0, result, 0, array[row].length);
		return result;
	}
	
	public static final int[] getRow(int[][] array, int row){
		assert row >= 0 && (row < array.length || row == 0);
		if (array == null) return null;
		if (array.length == 0) return new int[0];
		int[] result = new int[array[row].length];
		System.arraycopy(array[row], 0, result, 0, array[row].length);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static final <E> E[] arrayListToArray(ArrayList<E> list){
		if (list == null) return null;
		if (list.size() == 0) return null;
		E[] array = (E[]) Array.newInstance(list.get(0).getClass(), list.size());
		for (int i = 0, length = list.size(); i < length; i++){
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static final int[] toInt(Integer[] array){
		if (array == null) return new int[0];
		int[] intArray = new int[array.length];
		for (int i = 0,length = array.length; i < length; i++){
			intArray[i] = array[i];
		}
		return intArray;
	}
	
	/**
	 * This method convets dp unit to equivalent device specific value in pixels. 
	 * 
	 * @param dp A value in dp(Device independent pixels) unit. Which we need to convert into pixels
	 * @param dpi A value of display density dpi
	 * @return A float value to represent Pixels equivalent to dp according to device
	 */
	public static float convertDpToPx(float dp,int dpi){
		return dp * (dpi / 160f);
	}
	/**
	 * This method converts device specific pixels to device independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param dpi A value of display density dpi
	 * @return A float value to represent db equivalent to px value
	 */
	public static float converPxToDp(int px, int dpi){
		return  px / (dpi / 160f);
	}
	
	/**
	 * Returns string associated with resource id
	 * @param context
	 * @param resID
	 * @return
	 */
	public static String getString(Context context,int resID){
		return context.getResources().getString(resID);
	}
	
	/**
	 * Hides keyboard if any view is focused
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity){
		if (activity.getCurrentFocus() != null){
	    	InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    	inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	/**
	 * Shows keyboard and focused edit text
	 * @param activity
	 * @param editText
	 */
	public static void showKeyboardOnCreate(final EditText editText) {
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		ViewTreeObserver vto = editText.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				ViewTreeObserver obs = editText.getViewTreeObserver();
				editText.postDelayed(new Runnable() {
					@Override
					public void run() {
						InputMethodManager keyboard = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.showSoftInput(editText, 0);
						Print.log("Show!");
					}
				}, 50);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					obs.removeOnGlobalLayoutListener(this);
				} else {
					obs.removeGlobalOnLayoutListener(this);
				}
			}
			
		});
	}
}
