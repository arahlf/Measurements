package com.arahlf.measurements;

import static com.arahlf.measurements.Unit.MILLIMETER;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An immutable class representing a measurement, consisting of a length and unit.  Instances
 * of this class can be created by using the static create or parse methods.  All operations 
 * return new instances of this class, using the HALF_UP rounding mode when applicable.
 * 
 * @author arahlf
 */
public final class Measurement {
    
    public static Measurement create(int length, Unit unit) {
        return new Measurement(new BigDecimal(length), unit, unit);
    }
    
    public static Measurement create(String length, Unit unit) {
        return new Measurement(new BigDecimal(length), unit, unit);
    }
    
    public static Measurement create(BigDecimal length, Unit unit) {
        return new Measurement(length, unit, unit);
    }
    
    public static Measurement create(BigDecimal length, Unit desiredUnit, Unit inputUnit) {
        return new Measurement(length, desiredUnit, inputUnit);
    }
    
    private Measurement(BigDecimal length, Unit desiredUnit, Unit inputUnit) {
        _millis = length.multiply(inputUnit.getMillimetersPerUnit()).stripTrailingZeros();
        _unit = desiredUnit;
    }
    
    /**
     * Returns the length of the Measurement.
     * @return the length of the Measurement
     */
    public BigDecimal getLength() {
        return _millis.divide(_unit.getMillimetersPerUnit(), SCALE, ROUNDING_MODE).stripTrailingZeros();
    }
    
    /**
     * Returns the length of the Measurement in millimeters.
     * @return the length of the Measurement in millimeters
     */
    public BigDecimal getLengthInMillimeters() {
        return _millis;
    }
    
    /**
     * Returns the Measurement's Unit.
     * @return the Measurement's Unit
     */
    public Unit getUnit() {
        return _unit;
    }
    
    /**
     * Returns true if the measurement has a length of 0.
     * @return true if the measurement has a length of 0
     */
    public boolean isZeroLength() {
        return _millis.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Returns true if the measurement has a length less than 0.
     * @return true if the measurement has a length less than 0
     */
    public boolean isNegativeLength() {
        return _millis.compareTo(BigDecimal.ZERO) == -1;
    }
    
    /**
     * Returns true if the measurement has a length greater than 0.
     * @return true if the measurement has a length greater than 0
     */
    public boolean isPositiveLength() {
        return _millis.compareTo(BigDecimal.ZERO) == 1;
    }
    
    /**
     * Returns a new Measurement that is the sum of the two.  The original Measurement's Unit is maintained.
     * @param measurement The Measurement to add
     * @return Measurement that is the sum of the two
     */
    public Measurement add(Measurement measurement) {
        BigDecimal lengthInMillis = _millis.add(measurement.getLengthInMillimeters());
        
        return new Measurement(lengthInMillis.stripTrailingZeros(), _unit, MILLIMETER);
    }
    
    /**
     * Returns a new Measurement that is the difference of the two.  The original Measurement's Unit is maintained.
     * @param measurement The measurement to subtract
     * @return Measurement that is the difference of the two
     */
    public Measurement subtract(Measurement measurement) {
        BigDecimal lengthInMillis = _millis.subtract(measurement.getLengthInMillimeters());
        
        return new Measurement(lengthInMillis.stripTrailingZeros(), _unit, MILLIMETER);
    }
    
    /**
     * Returns a new Measurement that is the product of the two.  The original Measurement's Unit is maintained.
     * @param measurement The Measurement to multiply by
     * @return Measurement that is the product of the two
     */
    public Measurement multiply(Measurement measurement) {
        BigDecimal length = getLength().multiply(measurement.convert(_unit).getLength());
        
        return create(length.stripTrailingZeros(), _unit);
    }
    
    /**
     * Returns a new Measurement that is the quotient of the two.  The original Measurement's Unit is maintained.
     * @param measurement The Measurement to divide by
     * @return Measurement that is the quotient of the two
     */
    public Measurement divide(Measurement measurement) {
        BigDecimal length = getLength().divide(measurement.convert(_unit).getLength(), SCALE, ROUNDING_MODE);
        
        return create(length.stripTrailingZeros(), _unit);
    }
    
    /**
     * Returns a new Measurement that is converted to the specified Unit.
     * @param unit The unit to convert the Measurement to
     * @return Measurement that is converted to the specified Unit
     */
    public Measurement convert(Unit unit) {
        BigDecimal length = _millis.divide(unit.getMillimetersPerUnit(), SCALE, ROUNDING_MODE);
        
        return create(length.stripTrailingZeros(), unit);
    }
    
    /**
     * Returns a new Measurement with the specified scale (number of decimal places).  Trailing
     * zeros are stripped.
     * @param scale The scale for the new Measurement.
     * @return Measurement with the specified scale (number of decimal places).
     */
    public Measurement scale(int scale) {
        return create(getLength().setScale(scale, ROUNDING_MODE).stripTrailingZeros(), _unit);
    }
    
    /**
     * Returns a new Measurement with the specified scale (number of decimal places).  Trailing
     * zeros are stripped.
     * @param scale The scale for the new Measurement.
     * @param roundingMode The RoundingMode to use if necessary.
     * @return Measurement with the specified scale (number of decimal places).
     */
    public Measurement scale(int scale, RoundingMode roundingMode) {
        return create(getLength().setScale(scale, roundingMode).stripTrailingZeros(), _unit);
    }
    
    /**
     * Attempts to parse a Measurement from a String.  Measurement strings can be parsed
     * from the toString'd version of this class (e.g. "6ft").
     * @param measurement The String to parse.
     * @return Measurement that was parsed, otherwise a NumberFormatException or IllegalArgumentException.
     */
    public static Measurement parse(String measurement) {
        Matcher matcher = PARSE_PATTERN.matcher(measurement);
        matcher.find();
        return create(matcher.group(1), Unit.parse(matcher.group(2)));
    }
    
    /**
     * Returns the String representation of the Measurement (e.g. "4ft").
     */
    @Override
    public String toString() {
        return getLength().toPlainString() + _unit.getAbbreviation();
    }
    
    /**
     * Returns true if the given Measurement's total length (regardless of Unit) is equal to
     * this one.  For example, "1ft" would be equal to "12in".
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Measurement)) {
            return false;
        }
        
        Measurement measurement = (Measurement) other;
        
        return _millis.equals(measurement.getLengthInMillimeters());
    }
    
    @Override
    public int hashCode() {
        return getLengthInMillimeters().hashCode();
    }
    
    private final BigDecimal _millis;
    private final Unit _unit;
    
    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final Pattern PARSE_PATTERN = Pattern.compile("^(.+?)\\s?([a-z]+)");
}
