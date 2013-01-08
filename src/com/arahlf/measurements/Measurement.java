package com.arahlf.measurements;

import static com.arahlf.measurements.Unit.MILLIMETER;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Measurement implements Serializable {
    
    public Measurement(int length, Unit unit) {
        this(new BigDecimal(length), unit, unit);
    }
    
    public Measurement(String length, Unit unit) {
        this(new BigDecimal(length), unit, unit);
    }
    
    public Measurement(BigDecimal length, Unit unit) {
        this(length, unit, unit);
    }
    
    public Measurement(BigDecimal length, Unit desiredUnit, Unit inputUnit) {
        _millis = _stripTrailingZeros(length.multiply(inputUnit.getMillimetersPerUnit()));
        _unit = desiredUnit;
    }
    
    public BigDecimal getLength() {
        return _stripTrailingZeros(_millis.divide(_unit.getMillimetersPerUnit(), SCALE, ROUNDING_MODE));
    }
    
    public BigDecimal getLengthInMillimeters() {
        return _millis;
    }
    
    public Unit getUnit() {
        return _unit;
    }
    
    public boolean isZeroLength() {
        return _millis.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public boolean isNegativeLength() {
        return _millis.compareTo(BigDecimal.ZERO) == -1;
    }
    
    public boolean isPositiveLength() {
        return _millis.compareTo(BigDecimal.ZERO) == 1;
    }
    
    public Measurement add(Measurement measurement) {
        BigDecimal lengthInMillis = _millis.add(measurement.getLengthInMillimeters());
        
        return new Measurement(_stripTrailingZeros(lengthInMillis), _unit, MILLIMETER);
    }
    
    public Measurement subtract(Measurement measurement) {
        BigDecimal lengthInMillis = _millis.subtract(measurement.getLengthInMillimeters());
        
        return new Measurement(_stripTrailingZeros(lengthInMillis), _unit, MILLIMETER);
    }
    
    public Measurement multiply(Measurement measurement) {
        BigDecimal length = getLength().multiply(measurement.convert(_unit).getLength());
        
        return new Measurement(_stripTrailingZeros(length), _unit);
    }
    
    public Measurement divide(Measurement measurement) {
        BigDecimal length = getLength().divide(measurement.convert(_unit).getLength(), SCALE, ROUNDING_MODE);
        
        return new Measurement(_stripTrailingZeros(length), _unit);
    }
    
    public Measurement convert(Unit unit) {
        BigDecimal length = _millis.divide(unit.getMillimetersPerUnit(), SCALE, ROUNDING_MODE);
        
        return new Measurement(_stripTrailingZeros(length), unit);
    }
    
    public Measurement scale(int scale) {
        return new Measurement(_stripTrailingZeros(getLength().setScale(scale, ROUNDING_MODE)), _unit);
    }
    
    public Measurement scale(int scale, RoundingMode roundingMode) {
        return new Measurement(_stripTrailingZeros(getLength().setScale(scale, roundingMode)), _unit);
    }
    
    public static Measurement parse(String measurement) {
        Matcher matcher = PARSE_PATTERN.matcher(measurement);
        matcher.find();
        return new Measurement(matcher.group(1), Unit.parse(matcher.group(2)));
    }
    
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
    
    @Override
    public String toString() {
        return getLength().toPlainString() + _unit.getAbbreviation();
    }
    
    private BigDecimal _stripTrailingZeros(BigDecimal length) {
        // stripTrailingZeros doesn't work on 0 :'(
        return length.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : length.stripTrailingZeros();
    }
    
    private final BigDecimal _millis;
    private final Unit _unit;
    
    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final Pattern PARSE_PATTERN = Pattern.compile("^(.+?)\\s?([a-z]+)");
    
    private static final long serialVersionUID = 8902794930924758548L;
}
