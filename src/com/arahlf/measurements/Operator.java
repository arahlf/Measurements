package com.arahlf.measurements;

public enum Operator {
    
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷");
    
    public String getSymbol() {
        return _symbol;
    }
    
    private Operator(String symbol) {
        _symbol = symbol;
    }
    
    private final String _symbol;
}
