package com.arahlf.measurements.formatting;

import static com.arahlf.measurements.Unit.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public class TU_DecimalMeasurementFormatter extends TestCase {
    @Test
    public void testZeroLengthMeasurement() {
        _measurement = Measurement.create("0", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("0in");
    }
    
    @Test
    public void testOnlyNecessaryUnitsAreUsed() {
        _measurement = Measurement.create("36", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("3ft");
    }
    
    @Test
    public void testOnlyUnnecessaryUnitsAreSkipped() {
        _measurement = Measurement.create("11", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("11in");
    }
    
    @Test
    public void testTwoUnitsNoRemainder() {
        _measurement = Measurement.create("38", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("3ft 2in");
    }
    
    @Test
    public void testRemainderWithinScale() {
        _measurement = Measurement.create("17.5", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("1ft 5.5in");
    }
    
    @Test
    public void testRemainderMatchingScale() {
        _measurement = Measurement.create("17.25", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("1ft 5.25in");
    }
    
    @Test
    public void testRemainderExceedingScale() {
        _measurement = Measurement.create("1.05", METER);
        _formatter = new DecimalMeasurementFormatter(3, INCHES);
        
        _verify("~41.339in");
    }
    
    @Test
    public void testRepeatingRemainder() {
        _measurement = Measurement.create("8", FOOT);
        _formatter = new DecimalMeasurementFormatter(3, YARDS);
        
        _verify("~2.667yd");
    }
    
    @Test
    public void testNegativeRepeatingRemainder() {
        _measurement = Measurement.create("-8", FOOT);
        _formatter = new DecimalMeasurementFormatter(3, YARDS);
        
        _verify("~-2.667yd");
    }
    
    @Test
    public void testThreeUnits() {
        _measurement = Measurement.create("12432343.90", MILLIMETER);
        _formatter = new DecimalMeasurementFormatter(3, METERS_CENTIMETERS_MILLIMETERS);
        
        _verify("12432m 34cm 3.9mm");
    }
    
    @Test
    public void testNegativeNumberWithMultipleUnits() {
        _measurement = Measurement.create("-39.123", INCH);
        _formatter = new DecimalMeasurementFormatter(3, FEET_INCHES);
        
        _verify("-3ft 3.123in");
    }
    
    @Test
    public void testExactMeasurementDoesNotDisplayAsEstimate() {
        _measurement = Measurement.create(10, FOOT).add(Measurement.create(2, INCH));
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("10ft 2in");
    }
    
    @Test
    public void testMeasurementRoundUp() {
        _measurement = Measurement.create("3.999", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("~4in");
    }
    
    @Test
    public void testMeasurementRoundUpGroup() {
        _measurement = Measurement.create("11.999", INCH);
        _formatter = new DecimalMeasurementFormatter(2, FEET_INCHES);
        
        _verify("~1ft");
    }
    
    @Test
    public void testMeasurementRoundUpMultipleGroups() {
        _measurement = Measurement.create("71.999", INCH);
        _formatter = new DecimalMeasurementFormatter(2, YARD_FEET_INCHES);
        
        _verify("~2yd");
    }
    
    @Test
    public void testNegativeMeasurementRoundUpMultipleGroups() {
        _measurement = Measurement.create("-71.999", INCH);
        _formatter = new DecimalMeasurementFormatter(2, YARD_FEET_INCHES);
        
        _verify("~-2yd");
    }
    
    @Test
    public void testPositiveMeasurementRoundedDownToZero() {
        _measurement = Measurement.create(".0009", INCH);
        _formatter = new DecimalMeasurementFormatter(2, YARD_FEET_INCHES);
        
        _verify("~0in");
    }
    
    @Test
    public void testNegativeMeasurementRoundedUpToZero() {
        _measurement = Measurement.create("-.0001", INCH);
        _formatter = new DecimalMeasurementFormatter(2, YARD_FEET_INCHES);
        
        _verify("~0in");
    }
    
    private void _verify(String expected) {
        String actual = _formatter.format(_measurement);
        assertEquals("expected <" + expected + "> actual <" + actual + ">", expected, actual);
    }
    
    private Measurement _measurement;
    private MeasurementFormatter _formatter;
    
    private static final Unit[] INCHES = new Unit[] { INCH };
    private static final Unit[] YARDS = new Unit[] { YARD };
    private static final Unit[] FEET_INCHES = new Unit[] { FOOT, INCH };
    private static final Unit[] YARD_FEET_INCHES = new Unit[] { YARD, FOOT, INCH };
    private static final Unit[] METERS_CENTIMETERS_MILLIMETERS = new Unit[] { METER, CENTIMETER, MILLIMETER };
}
