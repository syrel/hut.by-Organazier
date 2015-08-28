package by.hut.flat.calendar.utils;

public class DateView {
	
	public static String toDB(Date date){
		StringBuilder str = new StringBuilder().append(date.year).append('-');
		if (date.month < 10) str.append('0');
		str.append(date.month).append('-');
		if (date.day < 10) str.append('0');
		str.append(date.day);
		return str.toString();
	}
	
	public static String toAnketa(Date date){
		StringBuilder str = new StringBuilder();
		if (date.day < 10) str.append('0');
		str.append(date.day).append('.');
		if (date.month < 10) str.append('0');
		str.append(date.month).append('.');
		str.append(date.year);
		return str.toString();
	}
}
