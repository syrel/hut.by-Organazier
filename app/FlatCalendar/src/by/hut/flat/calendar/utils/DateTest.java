package by.hut.flat.calendar.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import by.hut.flat.calendar.internal.Timer;

public class DateTest {
	private Date date;
	private int day = 20;
	private int month = 3;
	private int year = 2013;
	
	@Before
	public void setUp(){
		date = new Date(day,month,year);
	}
	@Test
	public void testLongCreate20130627(){
		Date date = new Date(20130627);
		assertEquals(2013,date.year);
		assertEquals(6,date.month);
		assertEquals(27,date.day);
	}
	
	@Test
	public void testLongCreate20121230(){
		Date date = new Date(20121230);
		assertEquals(2012,date.year);
		assertEquals(12,date.month);
		assertEquals(30,date.day);
	}
	
	@Test
	public void testPrev(){
		Date date2 = new Date(12,4,2013);
		Date date1 = new Date(25,3,2013);
		Timer timer = new Timer();
		
		for (int i = 0; i < 500000; i++){
			date1.isPrev(date2);
		}
		
		System.out.println("Time: "+timer.timeElapsed()+" ms");
		
		assertEquals(new Date(25,3,2013),date1);
		assertEquals(new Date(12,4,2013),date2);
		
		timer.reset();
		for (int i = 0; i < 500000; i++){
			date1.isPrev(date2);
		}
		System.out.println("Time: "+timer.timeElapsed()+" ms");
		
		assertEquals(new Date(25,3,2013),date1);
		assertEquals(new Date(12,4,2013),date2);
	}
	@Test
	public void testIsPrevDay(){
		Date d = new Date(10,03,2013);
		Date date = new Date(9,03,2013);
		assertTrue(date.isPrev(d));
		assertEquals(new Date(10,03,2013),d);
	}
	@Test
	public void testIsLastDayInMonth(){
		assertTrue(new Date(28,2,2013).isLastDayInMonth());
		assertTrue(new Date(29,2,2012).isLastDayInMonth());
		assertTrue(new Date(31,12,2013).isLastDayInMonth());
		assertTrue(new Date(30,04,2013).isLastDayInMonth());
	}
	@Test
	public void testFindDate(){
		assertEquals(new Date(20,3,2013),(new Date(27,6,1992)).findDate(7571));
		assertEquals(new Date(27,6,1992),(new Date(20,3,2013)).findDate(-7571));
	}
	@Test
	public void testNextDate(){
		assertEquals(new Date(26,03,2013),(new Date(25,03,2013)).next());
		assertEquals(new Date(1,01,2014),(new Date(31,12,2013)).next());
		assertEquals(new Date(29,02,2012),(new Date(28,02,2012)).next());
		assertEquals(new Date(1,02,2013),(new Date(31,01,2013)).next());
	}
	@Test
	public void testPrevDate(){
		assertEquals(new Date(24,03,2013),(new Date(25,03,2013)).prev());
		assertEquals(new Date(31,12,2013),(new Date(1,1,2014)).prev());
		assertEquals(new Date(28,02,2012),(new Date(29,02,2012)).prev());
		assertEquals(new Date(31,01,2013),(new Date(01,02,2013)).prev());
	}
	@Test
	public void testDaysBetweenDates(){
		assertEquals(-1,(new Date(20,3,2013)).daysUntil(new Date(19,3,2013)));
		assertEquals(1,(new Date(19,3,2013)).daysUntil(new Date(20,3,2013)));
		assertEquals(-28,(new Date(20,3,2013)).daysUntil(new Date(20,2,2013)));
		assertEquals(28,(new Date(20,2,2013)).daysUntil(new Date(20,3,2013)));
		assertEquals(-365,(new Date(20,3,2013)).daysUntil(new Date(20,3,2012)));
		assertEquals(365,(new Date(20,3,2012)).daysUntil(new Date(20,3,2013)));
		assertEquals(366,(new Date(20,1,2016)).daysUntil(new Date(20,1,2017)));
		assertEquals(-7571,(new Date(20,3,2013)).daysUntil(new Date(27,6,1992)));
		assertEquals(7571,(new Date(27,6,1992)).daysUntil(new Date(20,3,2013)));
		assertEquals(41,(new Date(24,02,2013)).daysUntil(new Date(6,04,2013)));
	}
	@Test
	public void testIsLeapYear(){
		assertTrue(Date.isLeapYear(2004));
		assertTrue(Date.isLeapYear(2400));
		assertTrue(Date.isLeapYear(2000));
		assertTrue(Date.isLeapYear(1600));
		assertTrue(Date.isLeapYear(1996));
	}
	@Test
	public void testIsNotLeapYear(){
		assertFalse(Date.isLeapYear(2001));
		assertFalse(Date.isLeapYear(2013));
		assertFalse(Date.isLeapYear(1900));
		assertFalse(Date.isLeapYear(2100));
	}
	@Test
	public void testShouldCompareToLess(){
		
		assertEquals(1,date.compareTo(new Date(day-1,month,year)));
		
		assertEquals(1,date.compareTo(new Date(day-1,month-1,year)));
		assertEquals(1,date.compareTo(new Date(day,month-1,year)));
		assertEquals(1,date.compareTo(new Date(day+1,month-1,year)));
		
		assertEquals(1,date.compareTo(new Date(day-1,month,year-1)));
		assertEquals(1,date.compareTo(new Date(day,month,year-1)));
		assertEquals(1,date.compareTo(new Date(day+1,month,year-1)));
		
		assertEquals(1,date.compareTo(new Date(day-1,month-1,year-1)));
		assertEquals(1,date.compareTo(new Date(day,month-1,year-1)));
		assertEquals(1,date.compareTo(new Date(day+1,month-1,year-1)));
		
		assertEquals(1,date.compareTo(new Date(day-1,month+1,year-1)));
		assertEquals(1,date.compareTo(new Date(day,month+1,year-1)));
		assertEquals(1,date.compareTo(new Date(day+1,month+1,year-1)));
	}
	@Test
	public void testShouldCompareToBigger(){
		
		assertEquals(-1,date.compareTo(new Date(day+1,month,year)));
		
		assertEquals(-1,date.compareTo(new Date(day-1,month+1,year)));
		assertEquals(-1,date.compareTo(new Date(day,month+1,year)));
		assertEquals(-1,date.compareTo(new Date(day+1,month+1,year)));
		
		assertEquals(-1,date.compareTo(new Date(day-1,month,year+1)));
		assertEquals(-1,date.compareTo(new Date(day,month,year+1)));
		assertEquals(-1,date.compareTo(new Date(day+1,month,year+1)));
		
		assertEquals(-1,date.compareTo(new Date(day-1,month-1,year+1)));
		assertEquals(-1,date.compareTo(new Date(day,month-1,year+1)));
		assertEquals(-1,date.compareTo(new Date(day+1,month-1,year+1)));
		
		assertEquals(-1,date.compareTo(new Date(day-1,month+1,year+1)));
		assertEquals(-1,date.compareTo(new Date(day,month+1,year+1)));
		assertEquals(-1,date.compareTo(new Date(day+1,month+1,year+1)));
	}
	@Test
	public void testShouldCompareToSame(){
		assertEquals(0,date.compareTo(new Date(day,month,year)));
	}
	@Test
	public void testShouldConstructed(){
		assertEquals(day,date.getDay());
		assertEquals(month,date.getMonth());
		assertEquals(year,date.getYear());
	}
	@Test
	public void testIsNotValidDate() {
		assertFalse(Date.isValidDate(0,0,0));
		assertFalse(Date.isValidDate(32,3,2013));
		assertFalse(Date.isValidDate(29,2,2013));
		assertFalse(Date.isValidDate(5,0,2002));
		assertFalse(Date.isValidDate(7,13,2013));
		assertFalse(Date.isValidDate(29,2,1900));
	}
	@Test
	public void testIsValidDate() {
		assertTrue(Date.isValidDate(27,6,1992));
		assertTrue(Date.isValidDate(29,3,2013));
		assertTrue(Date.isValidDate(29,2,2012));
		assertTrue(Date.isValidDate(29,2,2000));
		assertTrue(Date.isValidDate(30,12,1900));
		assertTrue(Date.isValidDate(1,1,2013));
	}

}
