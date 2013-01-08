package com.arahlf.measurements.formatting;

import static com.arahlf.measurements.Unit.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public class TU_FractionMeasurementFormatter extends TestCase {
    @Test
    public void testZeroLengthMeasurement() {
        _measurement = new Measurement("0", INCH);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("0in");
    }
    
    @Test
    public void testOnlyNecessaryUnitsAreUsed() {
        _measurement = new Measurement("48", INCH);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("4ft");
    }
    
    @Test
    public void testThreeUnits() {
        _measurement = new Measurement("89", INCH);
        _formatter = new FractionMeasurementFormatter(4, YARD_FEET_INCHES);
        
        _verify("2yd 1ft 5in");
    }
    
    @Test
    public void testThreeUnitsWithExactFraction() {
        _measurement = new Measurement("89.25", INCH);
        _formatter = new FractionMeasurementFormatter(4, YARD_FEET_INCHES);
        
        _verify("2yd 1ft 5-1/4in");
    }
    
    @Test
    public void testThreeUnitsWithEstimateFraction() {
        _measurement = new Measurement("1.89", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~1-7/8in");
    }
    
    @Test
    public void testFloatingPointNumbersAreAccurate() {
        _measurement = new Measurement(".875", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("7/8in");
    }
    
    @Test
    public void testNegativeEstimatedFraction() {
        _measurement = new Measurement("-.876", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~-7/8in");
    }
    
    @Test
    public void testFractionRoundUp() {
        _measurement = new Measurement("6552", MILLIMETER);
        _formatter = new FractionMeasurementFormatter(4, FEET_INCHES);
        
        _verify("~21ft 6in");
    }
    
    @Test
    public void testFractionRoundDown() {
        _measurement = new Measurement("36.001", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~3ft");
    }
    
    @Test
    public void testFractionRoundUpNextGroupIfApplicable() {
        _measurement = new Measurement("35.99", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~3ft");
    }
    
    @Test
    public void testFractionRoundUpMultipleGroups() {
        _measurement = new Measurement("71.99", INCH);
        _formatter = new FractionMeasurementFormatter(8, YARD_FEET_INCHES);
        
        _verify("~2yd");
    }
    
    @Test
    public void testEstimateFractionWithNegativeNumber() {
        _measurement = new Measurement("-13.876", INCH);
        _formatter = new FractionMeasurementFormatter(8, INCHES);
        
        _verify("~-13-7/8in");
    }
    
    @Test
    public void testNegativeFractionWithWholeNumber() {
        _measurement = new Measurement("-25.126", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~-2ft 1-1/8in");
    }
    
    @Test
    public void testExactMeasurementDoesNotDisplayAsEstimate() {
        _measurement = new Measurement(10, FOOT).add(new Measurement(2, INCH));
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("10ft 2in");
    }
    
    @Test
    public void testPositiveMeasurementRoundedDownToZero() {
        _measurement = new Measurement(".0009", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~0in");
    }
    
    @Test
    public void testNegativeMeasurementRoundedUpToZero() {
        _measurement = new Measurement(".0001", INCH);
        _formatter = new FractionMeasurementFormatter(8, FEET_INCHES);
        
        _verify("~0in");
    }
    
    @Test
    public void testFractionIsNotUnnecessarilyRoundedUp() {
        _measurement = new Measurement(".125", INCH);
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
