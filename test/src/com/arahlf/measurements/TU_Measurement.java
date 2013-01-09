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
        
        sum = Measurement.create("1", INCH).add(Measurement.create("3", FOOT));
        _assertMeasurement(sum, "37in", "939.8");
        
        sum = Measurement.create("2", FOOT).add(Measurement.create("3", YARD));
        _assertMeasurement(sum, "11ft", "3352.8");
        
        sum = Measurement.create("5", YARD).add(Measurement.create("3", INCH));
        _assertMeasurement(sum, "5.0833333333yd", "4648.2");
        
        sum = Measurement.create("1", YARD).add(Measurement.create("1", MILLIMETER));
        _assertMeasurement(sum, "1.0010936133yd", "915.4");
        
        sum = Measurement.create("2.125", INCH).add(Measurement.create(".875", INCH));
        _assertMeasurement(sum, "3in", "76.2");
        
        sum = Measurement.create("10", FOOT).add(Measurement.create("2", INCH));
        _assertMeasurement(sum, "10.1666666667ft", "3098.8");
    }
    
    @Test
    public void testSubtract() {
        Measurement difference;
        
        difference = Measurement.create("100.5", METER).subtract(Measurement.create("3", INCH));
        _assertMeasurement(difference, "100.4238m", "100423.8");
        
        difference = Measurement.create("1", YARD).subtract(Measurement.create("3", FOOT));
        _assertMeasurement(difference, "0yd", "0");
        
        difference = Measurement.create("1600", METER).subtract(Measurement.create("25", MILLIMETER));
        _assertMeasurement(difference, "1599.975m", "1599975");
        
        difference = Measurement.create("11", FOOT).subtract(Measurement.create("10", INCH));
        _assertMeasurement(difference, "10.1666666667ft", "3098.8");
    }
    
    @Test
    public void testMultiply() {
        Measurement product;
        
        product = Measurement.create("3", INCH).multiply(Measurement.create("3", FOOT));
        _assertMeasurement(product, "108in", "2743.2");
        
        product = Measurement.create("8.25", CENTIMETER).multiply(Measurement.create("400", METER));
        _assertMeasurement(product, "330000cm", "3300000");
        
        product = Measurement.create("109.7", MILLIMETER).multiply(Measurement.create("3.125", FOOT));
        _assertMeasurement(product, "104489.25mm", "104489.25");
    }
    
    @Test
    public void testDivide() {
        Measurement quotient;
        
        quotient = Measurement.create("9", CENTIMETER).divide(Measurement.create("30", MILLIMETER));
        _assertMeasurement(quotient, "3cm", "30");
        
        quotient = Measurement.create("12", FOOT).divide(Measurement.create("2.23", YARD));
        _assertMeasurement(quotient, "1.7937219731ft", "546.72645740088");
        
        quotient = Measurement.create("31", FOOT).divide(Measurement.create("3.9", METER));
        _assertMeasurement(quotient, "2.4227692308ft", "738.46006154784");
    }
    
    @Test
    public void testConvert() {
        Measurement converted;
        
        converted = Measurement.create("3", FOOT).convert(YARD);
        _assertMeasurement(converted, "1yd", "914.4");
        
        converted = Measurement.create("1005", MILLIMETER).convert(METER);
        _assertMeasurement(converted, "1.005m", "1005");
        
        converted = Measurement.create("40", INCH).convert(FOOT);
        _assertMeasurement(converted, "3.3333333333ft", "1015.99999998984");
    }
    
    @Test
    public void testScale() {
        Measurement measurement;
        
        measurement = Measurement.create("103.378913", INCH).scale(4);
        _assertMeasurement(measurement, "103.3789in", "2625.82406");
        
        measurement = Measurement.create(".824", CENTIMETER).scale(2);
        _assertMeasurement(measurement, "0.82cm", "8.2");
    }
    
    @Test
    public void testPositiveNegativeZero() {
        Measurement m;
        
        m = Measurement.create("1", FOOT);
        assertTrue(m.isPositiveLength());
        assertFalse(m.isNegativeLength());
        assertFalse(m.isZeroLength());
        
        m = Measurement.create("-1", FOOT);
        assertFalse(m.isPositiveLength());
        assertTrue(m.isNegativeLength());
        assertFalse(m.isZeroLength());
        
        m = Measurement.create("0", FOOT);
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
        Measurement x = Measurement.create(3, FOOT);
        Measurement y = Measurement.create("3", FOOT);
        Measurement z = Measurement.create(new BigDecimal("3"), FOOT);
        
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
        
        Measurement different = Measurement.create("4", FOOT);
        
        assertFalse(x.equals(different));
        
        Measurement differentButSame = Measurement.create("36", INCH);
        
        assertTrue(x.equals(differentButSame));
    }
    
    @Test
    public void testHashCode() {
        Measurement x = Measurement.create("1005", MILLIMETER);
        Measurement y = Measurement.create("1005", MILLIMETER);
        
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
