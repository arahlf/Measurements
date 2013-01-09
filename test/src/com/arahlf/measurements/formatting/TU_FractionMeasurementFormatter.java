package com.arahlf.measurements.formatting;

import static com.arahlf.measurements.Unit.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public class TU_FractionMeasurementFormatter extends TestCase {
    @Test
    public void testZeroLengthMeasurement() {
        _measurement = Measurement.create("0", INCH);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("0in");
    }
    
    @Test
    public void testOnlyNecessaryUnitsAreUsed() {
        _measurement = Measurement.create("48", INCH);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("4ft");
    }
    
    @Test
    public void testThreeUnits() {
        _measurement = Measurement.create("89", INCH);
        _formatter = new FractionMeasurementFormatter(4, YARD_FEET_INCHES);
        
        _verify("2yd 1ft 5in");
    }
    
    @Test
    public void testThreeUnitsWithExactFraction() {
        _measurement = Measurement.create("89.25", INCH);
        _formatter = new FractionMeasurementFormatter(4, YARD_FEET_INCHES);
        
        _verify("2yd 1ft 5-1/4in");
    }
    
    @Test
    public void testThreeUnitsWithEstimateFraction() {
        _measurement = Measurement.create("1.89", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~1-7/8in");
    }
    
    @Test
    public void testFloatingPointNumbersAreAccurate() {
        _measurement = Measurement.create(".875", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("7/8in");
    }
    
    @Test
    public void testNegativeEstimatedFraction() {
        _measurement = Measurement.create("-.876", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~-7/8in");
    }
    
    @Test
    public void testFractionRoundUp() {
        _measurement = Measurement.create("6552", MILLIMETER);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("~21ft 6in");
    }
    
    @Test
    public void testFractionRoundDown() {
        _measurement = Measurement.create("36.001", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~3ft");
    }
    
    @Test
    public void testFractionRoundUpNextGroupIfApplicable() {
        _measurement = Measurement.create("35.99", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~3ft");
    }
    
    @Test
    public void testFractionRoundUpMultipleGroups() {
        _measurement = Measurement.create("71.99", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~2yd");
    }
    
    @Test
    public void testEstimateFractionWithNegativeNumber() {
        _measurement = Measurement.create("-13.876", INCH);
        _formatter = new FractionMeasurementFormatter(8, INCHES);
        
        _verify("~-13-7/8in");
    }
    
    @Test
    public void testNegativeFractionWithWholeNumber() {
        _measurement = Measurement.create("-25.126", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~-2ft 1-1/8in");
    }
    
    @Test
    public void testExactMeasurementDoesNotDisplayAsEstimate() {
        _measurement = Measurement.create(10, FOOT).add(Measurement.create(2, INCH));
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("10ft 2in");
    }
    
    @Test
    public void testPositiveMeasurementRoundedDownToZero() {
        _measurement = Measurement.create(".0009", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~0in");
    }
    
    @Test
    public void testNegativeMeasurementRoundedUpToZero() {
        _measurement = Measurement.create(".0001", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~0in");
    }
    
    @Test
    public void testFractionIsNotUnnecessarilyRoundedUp() {
        _measurement = Measurement.create(".125", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("1/8in");
    }
    
    private void _verify(String expected) {
        String actual = _formatter.format(_measurement);
        assertEquals("expected <" + expected + "> actual <" + actual + ">", expected, actual);
    }
    
    private Measurement _measurement;
    private MeasurementFormatter _formatter;
    
    private static final Unit[] INCHES = new Unit[] { INCH };
    private static final Unit[] FEET_INCHES = new Unit[] { FOOT, INCH };
    private static final Unit[] YARD_FEET_INCHES = new Unit[] { YARD, FOOT, INCH };
}
