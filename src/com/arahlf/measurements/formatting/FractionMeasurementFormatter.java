package com.arahlf.measurements.formatting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public class FractionMeasurementFormatter extends AbstractMeasurementFormatter {
    
    public FractionMeasurementFormatter(int denominator, Unit... units) {
        super(Arrays.asList(units));
        
        _denominator = denominator;
    }
    
    @Override
    public String format(Measurement measurement) {
        LinkedList<Measurement> pieces = getPieces(measurement);
        
        if (measurement.isZeroLength()) {
            return pieces.getLast().toString();
        }
        
        adjustPiecesForNegatives(pieces);
        
        boolean estimatedFraction = false;
        boolean piecesRoundedUp = adjustPiecesForRounding(pieces);
        String fractionString = Fractions.getFractionString(pieces.getLast().getLength().doubleValue(), _denominator);
        StringBuilder builder = new StringBuilder();
        
        int zeroLengthPieces = 0;
        
        for (int i = 0; i < pieces.size() - 1; i++) {
            Measurement piece = pieces.get(i);
            
            if (piece.isZeroLength()) {
                zeroLengthPieces++;
                continue;
            }
            
            builder.append(piece.toString() + " ");
        }
        
        if (fractionString.startsWith("~")) {
            fractionString = fractionString.substring(1);
            estimatedFraction = true;
        }
        
        if (!fractionString.equals("0") || (fractionString.equals("0") && (pieces.size() == 1) || zeroLengthPieces == pieces.size() - 1)) {
            builder.append(fractionString + pieces.getLast().getUnit().getAbbreviation());
        }
        
        boolean approximation = estimatedFraction || piecesRoundedUp;
        if (approximation) {
            builder.insert(0, "~");
        }
        
        return builder.toString().trim();
    }
    
    @Override
    protected boolean shouldRoundUp(Measurement piece) {
        String fraction = Fractions.getFractionString(piece.getLength().doubleValue(), _denominator);
        Matcher matcher = NUMBER_REGEX.matcher(fraction);
        
        if (!matcher.find()) {
            return false;
        }
        
        BigDecimal length = piece.getLength();
        BigDecimal rounded = new BigDecimal(Integer.parseInt(matcher.group(1)));
        BigDecimal roundedUp = length.setScale(0, RoundingMode.UP);
        
        return length.compareTo(rounded) != 0 && rounded.compareTo(roundedUp) == 0;
    }
    
    private final int _denominator;
    
    private static final Pattern NUMBER_REGEX = Pattern.compile("~^?(\\d+)$");
    
    private static final long serialVersionUID = -5394598666718193859L;
}
