package com.tienda.observer;

public class LogObserver implements PedidoObserver {
    @Override
    public void onPedidoProcesado(String cliente, double total) {
        System.out.println(">>> [OBSERVER - LOG] Pedido de " + cliente + " por S/" + String.format("%.2f", total) + " registrado en el sistema de eventos.");
    }
}