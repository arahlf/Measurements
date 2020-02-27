package com.arahlf.measurements.formatting;

import java.io.Serializable;

import com.arahlf.measurements.Measurement;

public interface MeasurementFormatter extends Serializable {
    
    public String format(Measurement measurement);
    
}
