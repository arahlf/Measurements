package com.arahlf.measurements;

import junit.framework.TestCase;

import org.junit.Test;

import com.arahlf.measurements.formatting.Fractions;

public class TU_Fractions extends TestCase {
    @Test
    public void testEdgeCases() {
        assertEquals("0", Fractions.getFractionString(0, 4));
        
        assertEquals("1/8", Fractions.getFractionString(.125, 8));
        assertEquals("~1/8", Fractions.getFractionString(.120, 8));
        assertEquals("~1/8", Fractions.getFractionString(.130, 8));
        assertEquals("1-1/8", Fractions.getFractionString(1.125, 8));
        assertEquals("~1-1/8", Fractions.getFractionString(1.1, 8));
        assertEquals("~1-1/8", Fractions.getFractionString(1.13, 8));
        assertEquals("1", Fractions.getFractionString(1, 4));
        assertEquals("~2", Fractions.getFractionString(1.88, 4));
        assertEquals("~2", Fractions.getFractionString(2.12, 4));
        assertEquals("12", Fractions.getFractionString(12, 4));
        assertEquals("~13", Fractions.getFractionString(12.97, 16));
        assertEquals("~13", Fractions.getFractionString(13.02, 16));
        assertEquals("12-3/16", Fractions.getFractionString(12.1875, 16));
        assertEquals("~13-5/8", Fractions.getFractionString(13.630, 8));
        assertEquals("~13-5/8", Fractions.getFractionString(13.640, 8));
        assertEquals("143", Fractions.getFractionString(143, 4));
        assertEquals("~187", Fractions.getFractionString(187.1, 4));
        assertEquals("~187", Fractions.getFractionString(186.9, 4));
        assertEquals("139-7/64", Fractions.getFractionString(139.109375, 64));
        assertEquals("~139-9/64", Fractions.getFractionString(139.140629, 64));
        assertEquals("~139-9/64", Fractions.getFractionString(139.140621, 64));

        assertEquals("-1/8", Fractions.getFractionString(-.125, 8));
        assertEquals("~-1/8", Fractions.getFractionString(-.120, 8));
        assertEquals("~-1/8", Fractions.getFractionString(-.130, 8));
        assertEquals("-1-1/8", Fractions.getFractionString(-1.125, 8));
        assertEquals("~-1-1/8", Fractions.getFractionString(-1.1, 8));
        assertEquals("~-1-1/8", Fractions.getFractionString(-1.13, 8));
        assertEquals("-1", Fractions.getFractionString(-1, 4));
        assertEquals("~-2", Fractions.getFractionString(-1.88, 4));
        assertEquals("~-2", Fractions.getFractionString(-2.12, 4));
        assertEquals("-12", Fractions.getFractionString(-12, 4));
        assertEquals("~-13", Fractions.getFractionString(-12.97, 16));
        assertEquals("~-13", Fractions.getFractionString(-13.02, 16));
        assertEquals("-12-3/16", Fractions.getFractionString(-12.1875, 16));
        assertEquals("~-13-5/8", Fractions.getFractionString(-13.630, 8));
        assertEquals("~-13-5/8", Fractions.getFractionString(-13.640, 8));
        assertEquals("-143", Fractions.getFractionString(-143, 4));
        assertEquals("~-187", Fractions.getFractionString(-187.1, 4));
        assertEquals("~-187", Fractions.getFractionString(-186.9, 4));
        assertEquals("-139-7/64", Fractions.getFractionString(-139.109375, 64));
        assertEquals("~-139-9/64", Fractions.getFractionString(-139.140629, 64));
        assertEquals("~-139-9/64", Fractions.getFractionString(-139.140621, 64));
    }
}
