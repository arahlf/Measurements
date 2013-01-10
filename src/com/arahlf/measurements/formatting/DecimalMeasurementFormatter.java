package com.arahlf.measurements.formatting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public class DecimalMeasurementFormatter extends AbstractMeasurementFormatter {
    
    public DecimalMeasurementFormatter(int scale, Unit... units) {
        super(Arrays.asList(units));
        
        _scale = scale;
    }
    
    @Override
    public String format(Measurement measurement) {
        LinkedList<Measurement> pieces = getPieces(measurement);
        
        if (measurement.isZeroLength()) {
            return pieces.getLast().toString();
        }
        
        boolean piecesRoundedUp = adjustPiecesForRounding(pieces);
        Measurement last = pieces.getLast();
        Measurement rounded = last.scale(_scale);
        
        pieces.set(pieces.size() - 1, rounded);
        
        StringBuilder builder = new StringBuilder();
        
        boolean approximation = piecesRoundedUp || !last.equals(rounded);
        if (approximation) {
            builder.append("~");
        }
        
        adjustPiecesForNegatives(pieces);
        
        int zeroLengthPieces = 0;
        
        for (Measurement piece : pieces) {
            if (piece.isZeroLength()) {
                zeroLengthPieces++;
                continue;
            }
            
            builder.append(piece.toString() + " ");
        }
        
        if (zeroLengthPieces == pieces.size()) {
            builder.append(pieces.getLast().toString());
        }
        
        return builder.toString().trim();
    }
    
    @Override
    protected boolean shouldRoundUp(Measurement piece) {
        BigDecimal length = piece.getLength();
        BigDecimal rounded = length.setScale(_scale, RoundingMode.HALF_UP);
        BigDecimal roundedUp = length.setScale(0, RoundingMode.UP);
        
        return length.compareTo(rounded) != 0 && rounded.compareTo(roundedUp) == 0;
    }
    
    private final int _scale;
    
    private static final long serialVersionUID = -501904634416494211L;
}
