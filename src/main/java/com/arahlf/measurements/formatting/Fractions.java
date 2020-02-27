package com.arahlf.measurements.formatting;

public class Fractions {
    /**
     * Builds a fractional string representation of a number.
     * @param number The number to turn into a fraction string.
     * @param precision The precision of the fractional denominator to use, must be a multiple of 2.
     * @return
     */
    public static String getFractionString(final double number, final int precision) {
        if (precision % 2 != 0) {
            throw new IllegalArgumentException("Precision must be divisible by 2.");
        }
        
        int numerator = 1, denominator = precision, whole = (int) number;
        double decimal = Math.abs(number) - Math.floor(Math.abs(number));
        double previous = 1;
        String prefix = "";
        
        if (decimal == 0) {
            return Integer.toString(whole);
        }
        
        for (int i = 0; i <= precision; i++) {
            double testDecimal = (double) i / precision;
            
            // first check for an exact match
            if (decimal == testDecimal) {
                numerator = i;
                denominator = precision;
                break;
            }
            
            double difference = Math.abs(decimal - testDecimal);
            
            // check if trending closer to the number
            if (difference < previous) {
                previous = difference; // nope, but we're getting closer
                
                if (i == precision) {
                    return "~" + (whole >= 0 ? ++whole : --whole);
                }
            }
            else if (i == 1) {
                return "~" + whole;
            } else {
                // no longer trending closer, so the previous decimal was the closest
                numerator = i - 1;
                prefix = "~";
                break;
            }
        }
        
        // simplify applicable fractions (2/4 -> 1/2)
        while (numerator % 2 == 0 && denominator % 2 == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        
        String fraction = numerator + "/" + denominator;
        
        // preserve the whole number
        if (whole != 0) {
            return prefix + whole + "-" + fraction;
        }
        
        return prefix + (number < 0 ? "-" : "") + fraction;
    }
}
