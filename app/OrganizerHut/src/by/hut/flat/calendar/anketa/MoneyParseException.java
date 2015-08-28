package by.hut.flat.calendar.anketa;

public class MoneyParseException extends Exception {
	private static final long serialVersionUID = -1188778459471213727L;
	
	public static final int NEGATIVE = 1;
	public static final int NUMERIC = 2;
	public static final int OTHER = 0;
	
	public final int TYPE;
	
	public MoneyParseException(int exceptionType){
		super("Error parsing integer");
		switch (exceptionType){
			case NEGATIVE:{
				this.TYPE = NEGATIVE;
				break;
			}
			case NUMERIC:{
				this.TYPE = NUMERIC;
				break;
			}
			default:{
				this.TYPE = OTHER;
			}
		}
	}
	
	public int getType(){
		return TYPE;
	}
}