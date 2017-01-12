package by.hut.flat.calendar.core;

public class DayState {

	public int getAmPmState(int b, int r){
		if      (b == 0 && r == 0)return 0;
		else if (b == 0 && r == 1)return 1;
		else if (b == 0 && r >  1)return 2;
		else if (b == 1 && r == 0)return 3;
		else if (b >  1 && r == 0)return 4;
		else if (b == 1 && r == 1)return 5;
		else if (b >  1 && r == 1)return 6;
		else if (b == 1 && r >  1)return 7;
		else if (b >  1 && r >  1)return 8;
		else {
			alertL("ERROR! Unknown state! book="+b+" | rent="+r);
			return -1;
		}
	}
	public int getSettEvictState(int num){
		if      (num == 0)return 0;
		else if (num == 1)return 1;
		else if (num >  1)return 2;
		else {
			alertL("ERROR! Unknown layer_num! layer_num="+num);
			return -1;
		}
	}
	
	public void alertL(String str){ 
		android.util.Log.d("alertL", str);	
	}
	public static int Int(String str){
		return Integer.parseInt(str);
	}
	public static String Str(int integer){
		return ""+integer;
	}
}
