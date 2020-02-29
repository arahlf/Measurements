package com.arahlf.measurements;

import org.junit.jupiter.api.Test;

import static com.arahlf.measurements.Unit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TU_Measurement {
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
        
        product = Measurement.create("3", INCH).multiply(new BigDecimal("3"));
        _assertMeasurement(product, "9in", "228.6");
        
        product = Measurement.create("8.25", CENTIMETER).multiply(new BigDecimal("400"));
        _assertMeasurement(product, "3300cm", "33000");
        
        product = Measurement.create("109.7", MILLIMETER).multiply(new BigDecimal("3.125"));
        _assertMeasurement(product, "342.8125mm", "342.8125");
    }
    
    @Test
    public void testDivide() {
        Measurement quotient;
        
        quotient = Measurement.create("9", CENTIMETER).divide(new BigDecimal("30"));
        _assertMeasurement(quotient, "0.3cm", "3");
        
        quotient = Measurement.create("12", FOOT).divide(new BigDecimal("2.23"));
        _assertMeasurement(quotient, "5.3811659193ft", "1640.17937220264");
        
        quotient = Measurement.create("31", FOOT).divide(new BigDecimal("3.9"));
        _assertMeasurement(quotient, "7.9487179487ft", "2422.76923076376");
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
        assertThrows(NumberFormatException.class, () -> Measurement.parse("6..5ft"));
    }
    
    @Test
    public void testParseBadUnit() {
        assertThrows(IllegalArgumentException.class, () -> Measurement.parse("9.875inn"));
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
