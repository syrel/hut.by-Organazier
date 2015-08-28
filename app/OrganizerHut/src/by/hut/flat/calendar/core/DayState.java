package by.hut.flat.calendar.core;

public class DayState {
	int state[][] = {
			{1,2,2,5,6,7,8,7,8},
			{3,5,7,4,4,6,6,8,8}
	};
	int rState[][][] = 	{
			{
				{-1,0,-1,-1,-1,3,4,-1,-1},
				{-1,-1,1,-1,-1,-1,-1,5,6},
				{-1,-1,2,-1,-1,-1,-1,7,8}
			},
			{
				{-1,-1,-1,0,-1,1,-1,2,-1},
				{-1,-1,-1,-1,3,-1,5,-1,7},
				{-1,-1,-1,-1,4,-1,6,-1,8}
			}
	};
	
	public int getAmPmAdd(int state_old, int type){
		if (type != 2 || type != 3) {
			alertL("ERROR! type != 2 or 3.");
			return -1;
		}
		return state[type-2][state_old];
	}
	public int getAmPmRemove(int state_am_old, int type,int layer){
		if (type != 2 || type != 3) {
			alertL("ERROR! type != 2 or 3.");
			return -1;
		}
		int array = type - 2;
		int in_array = -1;
		if (layer == 1) in_array = 0;
		else if (layer == 2) in_array = 1;
		else if (layer > 2) in_array = 2;
		if (in_array == -1 || state_am_old == -1){
			alertL("ERROR! in_array = -1 | state_am_old = -1");
			return -1;
		}
		return rState[array][in_array][state_am_old];
	}
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
