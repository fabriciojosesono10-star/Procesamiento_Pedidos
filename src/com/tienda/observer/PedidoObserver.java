package com.tienda.observer;

public interface PedidoObserver {
    void onPedidoProcesado(String cliente, double total);
}