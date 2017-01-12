package by.hut.flat.calendar.utils;

import android.content.Context;
import android.widget.Toast;

public class Print {
	public static void p(int[] array){
		StringBuilder str = new StringBuilder();
		str.append('[');
		str.append(toString(array, ','));
		str.append(']');
		log(str);
	}
	
	public static void p(int[][] array){
		if (array.length == 0) log("[empty]");
		for (int i = 0, length = array.length; i < length; i++){
			p(array[i]);
		}
	}
	
	public static void p(String[] array) {
		StringBuilder str = new StringBuilder();
		str.append('[');
		str.append(toString(array, ','));
		str.append(']');
		log(str);
	}

	public static void p(String[][] array) {
		StringBuilder str = new StringBuilder();
		str.append('[');
        for (String[] row : array) {
		    str.append(toString(row, ','));
            str.append(System.lineSeparator());
        }
		str.append(']');
		log(str);
	}
	
	public static String toString(int[] array,Object delimiter){
		if (array == null) return "null";
		if (array.length == 0) return "empty";
		if (array.length == 1) return ""+array[0];
		StringBuilder str = new StringBuilder();
		for (int i = 0,length = array.length;i < length; i++){
			if (i != 0) str.append(delimiter);
			str.append(array[i]);
		}
		return str.toString();
	}
	
	public static String toString(String[] array,Object delimiter){
		if (array == null) return "null";
		if (array.length == 0) return "empty";
		if (array.length == 1) return ""+array[0];
		StringBuilder str = new StringBuilder();
		for (int i = 0,length = array.length;i < length; i++){
			if (i != 0) str.append(delimiter);
			str.append(array[i]);
		}
		return str.toString();
	}
	
	public static void log(Object o){
		System.out.println(o.toString());
	}
	
	public static void err(Object o){
		System.err.println("[error] "+o.toString());
	}
	
	public static void toast(Context context,String msg){
		Toast toast = Toast.makeText(context, msg,Toast.LENGTH_LONG);
		toast.show();
	}
}
