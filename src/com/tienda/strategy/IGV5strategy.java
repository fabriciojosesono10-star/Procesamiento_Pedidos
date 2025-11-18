package com.tienda.strategy;

public class IGV5strategy implements ImpuestoStrategy {
    @Override
    public double calcular(double subtotal) {
        return 0.5;
    }
    
}
