package com.arahlf.measurements;

import java.math.BigDecimal;

public enum Unit {
    
    MILLIMETER("mm", "Millimeters", "1"),
    CENTIMETER("cm", "Centimeters", "10"),
    METER("m", "Meters", "1000"),
    INCH("in", "Inches", "25.4"),
    FOOT("ft", "Feet", "304.8"),
    YARD("yd", "Yards", "914.4");
    
    public String getAbbreviation() {
        return _abbreviation;
    }
    
    public String getDisplayName() {
        return _displayName;
    }
    
    public BigDecimal getMillimetersPerUnit() {
        return _millimetersPerUnit;
    }
    
    public static Unit parse(String name) {
        name = name.toLowerCase();
        
        for (Unit unit : Unit.values()) {
            String abbreviation = unit.getAbbreviation().toLowerCase();
            String displayName = unit.getDisplayName().toLowerCase();
            
            if (name.equals(abbreviation) || name.equals(displayName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unrecognized unit specified: " + name);
    }
    
    private Unit(String abbreviation, String displayName, String millimetersPerUnit) {
        _abbreviation = abbreviation;
        _displayName = displayName;
        _millimetersPerUnit = new BigDecimal(millimetersPerUnit);
    }
    
    private final String _abbreviation;
    private final String _displayName;
    private final BigDecimal _millimetersPerUnit;
}
