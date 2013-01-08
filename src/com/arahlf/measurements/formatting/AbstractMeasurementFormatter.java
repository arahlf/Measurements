package com.arahlf.measurements.formatting;

import static com.arahlf.measurements.Unit.MILLIMETER;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import com.arahlf.measurements.Measurement;
import com.arahlf.measurements.Unit;

public abstract class AbstractMeasurementFormatter implements MeasurementFormatter {
    
    protected AbstractMeasurementFormatter(List<Unit> units) {
        if (units == null || units.isEmpty()) {
            throw new IllegalArgumentException("Must specify a list of units.");
        }
        
        BigDecimal previous = units.get(0).getMillimetersPerUnit();
        for (int i = 1; i < units.size(); i++) {
            BigDecimal current = units.get(i).getMillimetersPerUnit();
            
            if (current.compareTo(previous) != -1) {
                throw new IllegalArgumentException("Units not in descending length: " + units);
            }
            
            previous = current;
        }
        
        _units = units;
    }
    
    protected abstract boolean shouldRoundUp(Measurement piece);
    
    protected LinkedList<Measurement> getPieces(Measurement measurement) {
        LinkedList<Measurement> pieces = new LinkedList<Measurement>();
        BigDecimal remainingMillis = measurement.getLengthInMillimeters();
        
        for (int i = 0; i < _units.size(); i++) {
            Unit unit = _units.get(i);
            BigDecimal[] result = remainingMillis.divideAndRemainder(unit.getMillimetersPerUnit());
            
            BigDecimal quotient = result[0].stripTrailingZeros();
            BigDecimal remainder = result[1];
            
            Measurement piece = new Measurement(quotient, unit);
            
            if (i == _units.size() - 1) {
                piece = piece.add(new Measurement(remainder, MILLIMETER)); // add the remainder to the last piece
            }
            else {
                remainingMillis = remainder;
            }
            
            pieces.add(piece);
        }
        
        return pieces;
    }
    
    protected boolean adjustPiecesForRounding(LinkedList<Measurement> pieces) {
        Measurement last = pieces.getLast();
        
        if (!shouldRoundUp(last)) {
            return false;
        }
        
        int size = pieces.size();
        pieces.set(size - 1, last.scale(0, RoundingMode.UP));
        
        for (int i = size - 1; i > 0; i--) {
            Measurement current = pieces.get(i);
            Measurement next = pieces.get(i - 1);
            
            boolean canRoundUp = current.getLengthInMillimeters().abs().compareTo(next.getUnit().getMillimetersPerUnit()) == 0;
            if (canRoundUp) {
                pieces.set(i, new Measurement(0, current.getUnit()));
                pieces.set(i - 1, next.add(new Measurement(next.isNegativeLength() ? -1 : 1, next.getUnit())));
            }
        }
        
        return true;
    }
    
    protected void adjustPiecesForNegatives(LinkedList<Measurement> pieces) {
        boolean containsNegativePiece = false;
        
        for (int i = 0; i < pieces.size(); i++) {
            Measurement piece = pieces.get(i);
            
            if (piece.isNegativeLength()) {
                if (containsNegativePiece) {
                    pieces.set(i, new Measurement(piece.getLength().abs(), piece.getUnit()));
                }
                else {
                    containsNegativePiece = true;
                }
            }
        }
    }
    
    private final List<Unit> _units;
    
    private static final long serialVersionUID = 5350610055133000534L;
}
