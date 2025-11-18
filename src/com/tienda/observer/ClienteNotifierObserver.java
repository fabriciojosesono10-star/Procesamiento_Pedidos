package com.tienda.observer;

public class ClienteNotifierObserver implements PedidoObserver {
    @Override
    public void onPedidoProcesado(String cliente, double total) {
        System.out.println(">>> [OBSERVER - NOTIFICACION] Enviando confirmación a " + cliente + " por un total de S/" + String.format("%.2f", total) + ".");
    }
}