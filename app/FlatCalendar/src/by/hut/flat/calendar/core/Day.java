package by.hut.flat.calendar.core;

import by.hut.flat.calendar.utils.Date;

public class Day {
	public final int id;				// id
	public final int FlatID; 			// flat id
	public final Date date;  			// date
	public final int dayOfWeek;			// dayOfWeek
	public final int state;				// state
	public final int background;		// background id
	public final int prepay;			// amount of prepay
	public final int debt;				// amount of debt
	public final int casus;				// casus
	public final int textColor;			// text color
	public final int bgColor;			// background color
	public final boolean isLast;		// if day is last in serie
	
	public boolean clearDay = true;
	public int CacheID = -1;

	public Day(int id, int FlatID, Date date, int dayOfWeek,int state,
			int background,int prepay, int debt,int casus,
			int textColor,int bgColor,boolean isLast){
		this.id = id;
		this.FlatID = FlatID;
		this.date = date;
		this.dayOfWeek = dayOfWeek;
		this.state = state;
		this.background = background;
		this.prepay = prepay;
		this.debt = debt;
		this.casus = casus;
		this.textColor = textColor;
		this.bgColor = bgColor;
		this.isLast = isLast;
	}
	
	public boolean isClearDay() {
		return clearDay;
	}

	public Day setClearDay() {
		this.clearDay = true;
		return this;
	}
	
	public Day setCacheDay() {
		this.clearDay = false;
		return this;
	}
	
	public int getCacheID() {
		return CacheID;
	}

	public Day setCacheID(int cacheID) {
		CacheID = cacheID;
		return this;
	}
	
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	public boolean equals(Object obj){
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Date))
		      return false;
		Day day = (Day) obj;
		return (day.id == this.id && day.FlatID == this.FlatID && day.date.equals(this.date));
	}
	public int hashCode() {
		final int prime = 8;
		int hash = 1;
		hash = hash * prime + id;
		hash = hash * prime + FlatID;
		hash = hash * prime + date.hashCode();
		hash = hash * prime + state;
		hash = hash * prime + background;
		hash = hash * prime + prepay;
		hash = hash * prime + debt;
		hash = hash * prime + casus;
		hash = hash * prime + textColor;
		hash = hash * prime + bgColor;
		hash = hash * prime + ((isLast) ? 1 : 0);
		return hash;
	}
}
