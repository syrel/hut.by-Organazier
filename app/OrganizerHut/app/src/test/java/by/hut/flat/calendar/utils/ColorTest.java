package by.hut.flat.calendar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColorTest {
	@Test
	public void testShould255255255255ToHex(){
		Color color = new Color(255,255,255,255);
		assertEquals(0xffffffff,color.hex);
	}
	@Test
	public void testShould0000ToHex(){
		Color color = new Color(0,0,0,0);
		assertEquals(0x00000000,color.hex);
	}
	@Test
	public void testShould0x00ffffffToRgb(){
		Color color = new Color(0x00ffffff);
		assertEquals(255,color.r);
		assertEquals(255,color.g);
		assertEquals(255,color.b);
		assertEquals(0,color.a);
	}
	@Test
	public void testShould0xffffffffToRgb(){
		Color color = new Color(0xffffffff);
		assertEquals(255,color.r);
		assertEquals(255,color.g);
		assertEquals(255,color.b);
		assertEquals(255,color.a);
	}
}
