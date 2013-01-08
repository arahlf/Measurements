package com.arahlf.measurements;

import static com.arahlf.measurements.Unit.*;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.junit.Test;

public class TU_Measurement extends TestCase {
    @Test
    public void testAdd() {
        Measurement sum;
        
        sum = new Measurement("1", INCH).add(new Measurement("3", FOOT));
        _assertMeasurement(sum, "37in", "939.8");
        
        sum = new Measurement("2", FOOT).add(new Measurement("3", YARD));
        _assertMeasurement(sum, "11ft", "3352.8");
        
        sum = new Measurement("5", YARD).add(new Measurement("3", INCH));
        _assertMeasurement(sum, "5.0833333333yd", "4648.2");
        
        sum = new Measurement("1", YARD).add(new Measurement("1", MILLIMETER));
        _assertMeasurement(sum, "1.0010936133yd", "915.4");
        
        sum = new Measurement("2.125", INCH).add(new Measurement(".875", INCH));
        _assertMeasurement(sum, "3in", "76.2");
        
        sum = new Measurement("10", FOOT).add(new Measurement("2", INCH));
        _assertMeasurement(sum, "10.1666666667ft", "3098.8");
    }
    
    @Test
    public void testSubtract() {
        Measurement difference;
        
        difference = new Measurement("100.5", METER).subtract(new Measurement("3", INCH));
        _assertMeasurement(difference, "100.4238m", "100423.8");
        
        difference = new Measurement("1", YARD).subtract(new Measurement("3", FOOT));
        _assertMeasurement(difference, "0yd", "0");
        
        difference = new Measurement("1600", METER).subtract(new Measurement("25", MILLIMETER));
        _assertMeasurement(difference, "1599.975m", "1599975");
        
        difference = new Measurement("11", FOOT).subtract(new Measurement("10", INCH));
        _assertMeasurement(difference, "10.1666666667ft", "3098.8");
    }
    
    @Test
    public void testMultiply() {
        Measurement product;
        
        product = new Measurement("3", INCH).multiply(new Measurement("3", FOOT));
        _assertMeasurement(product, "108in", "2743.2");
        
        product = new Measurement("8.25", CENTIMETER).multiply(new Measurement("400", METER));
        _assertMeasurement(product, "330000cm", "3300000");
        
        product = new Measurement("109.7", MILLIMETER).multiply(new Measurement("3.125", FOOT));
        _assertMeasurement(product, "104489.25mm", "104489.25");
    }
    
    @Test
    public void testDivide() {
        Measurement quotient;
        
        quotient = new Measurement("9", CENTIMETER).divide(new Measurement("30", MILLIMETER));
        _assertMeasurement(quotient, "3cm", "30");
        
        quotient = new Measurement("12", FOOT).divide(new Measurement("2.23", YARD));
        _assertMeasurement(quotient, "1.7937219731ft", "546.72645740088");
        
        quotient = new Measurement("31", FOOT).divide(new Measurement("3.9", METER));
        _assertMeasurement(quotient, "2.4227692308ft", "738.46006154784");
    }
    
    @Test
    public void testConvert() {
        Measurement converted;
        
        converted = new Measurement("3", FOOT).convert(YARD);
        _assertMeasurement(converted, "1yd", "914.4");
        
        converted = new Measurement("1005", MILLIMETER).convert(METER);
        _assertMeasurement(converted, "1.005m", "1005");
        
        converted = new Measurement("40", INCH).convert(FOOT);
        _assertMeasurement(converted, "3.3333333333ft", "1015.99999998984");
    }
    
    @Test
    public void testScale() {
        Measurement measurement;
        
        measurement = new Measurement("103.378913", INCH).scale(4);
        _assertMeasurement(measurement, "103.3789in", "2625.82406");
        
        measurement = new Measurement(".824", CENTIMETER).scale(2);
        _assertMeasurement(measurement, "0.82cm", "8.2");
    }
    
    @Test
    public void testPositiveNegativeZero() {
        Measurement m;
        
        m = new Measurement("1", FOOT);
        assertTrue(m.isPositiveLength());
        assertFalse(m.isNegativeLength());
        assertFalse(m.isZeroLength());
        
        m = new Measurement("-1", FOOT);
        assertFalse(m.isPositiveLength());
        assertTrue(m.isNegativeLength());
        assertFalse(m.isZeroLength());
        
        m = new Measurement("0", FOOT);
        assertFalse(m.isPositiveLength());
        assertFalse(m.isNegativeLength());
        assertTrue(m.isZeroLength());
    }
    
    @Test
    public void testParseByAbbreviation() {
        Measurement measurement;
        
        measurement = Measurement.parse("23 yd");
        _assertMeasurement(measurement, "23yd", "21031.2");
        
        measurement = Measurement.parse("23yd");
        _assertMeasurement(measurement, "23yd", "21031.2");
    }
    
    @Test
    public void testParseByDisplayName() {
        Measurement measurement;
        
        measurement = Measurement.parse("-1005millimeters");
        _assertMeasurement(measurement, "-1005mm", "-1005");
        
        measurement = Measurement.parse("-1005 millimeters");
        _assertMeasurement(measurement, "-1005mm", "-1005");
    }
    
    @Test
    public void testParseBadNumber() {
        try {
            Measurement.parse("6..5ft");
            fail();
        }
        catch (NumberFormatException expected) {
        }
    }
    
    @Test
    public void testParseBadUnit() {
        try {
            Measurement.parse("9.875inn");
            fail();
        }
        catch (IllegalArgumentException expected) {
        }
    }
    
    @Test
    public void testEquals() {
        Measurement x = new Measurement(3, FOOT);
        Measurement y = new Measurement("3", FOOT);
        Measurement z = new Measurement(new BigDecimal("3"), FOOT);
        
        assertFalse(x.equals(null));
        
        // reflexive
        assertTrue(x.equals(x));
        
        // symmetric
        assertTrue(x.equals(y));
        assertTrue(y.equals(x));
        
        // transitive
        assertTrue(z.equals(z));
        assertTrue(x.equals(z));
        
        // consistent
        for (int i = 0; i < 1000; i++) {
            assertTrue(x.equals(y));
        }
        
        Measurement different = new Measurement("4", FOOT);
        
        assertFalse(x.equals(different));
        
        Measurement differentButSame = new Measurement("36", INCH);
        
        assertTrue(x.equals(differentButSame));
    }
    
    @Test
    public void testHashCode() {
        Measurement x = new Measurement("1005", MILLIMETER);
        Measurement y = new Measurement("1005", MILLIMETER);
        
        assertEquals(x.hashCode(), y.hashCode());
    }
    
    private void _assertMeasurement(Measurement measurement, String value, String lengthInMillis) {
        Matcher matcher = UNIT_PATTERN.matcher(value);
        matcher.find();
        Unit unit = Unit.parse(matcher.group(0));
        
        assertEquals(value, measurement.toString());
        assertEquals(unit, measurement.getUnit());
        assertEquals(lengthInMillis, measurement.getLengthInMillimeters().toPlainString());
    }
    
    private static final Pattern UNIT_PATTERN = Pattern.compile("([a-z]+)$");
}
