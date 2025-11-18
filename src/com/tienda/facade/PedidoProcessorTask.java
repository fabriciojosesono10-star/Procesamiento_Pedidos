package com.tienda.facade;

import com.tienda.model.Pedido;

public class PedidoProcessorTask implements Runnable {
    private final PedidoFacade facade;
    private final String producto, cliente;
    private final int cantidad;
    private final double precioUnitario;

    public PedidoProcessorTask(PedidoFacade facade, String producto, int cantidad, double precioUnitario, String cliente) {
        this.facade = facade;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("\n[HILO-PROCESADOR] Iniciando procesamiento de pedido para " + cliente + "...");

        if (facade.getImpuestoStrategy() == null) {
            System.out.println("[HILO-PROCESADOR] Error: No se ha seleccionado una estrategia de impuestos.");
            return;
        }

        // 1. Validación de Stock
        if (!facade.getValidador().validar(producto, cantidad)) {
            System.out.println("[HILO-PROCESADOR] Falló la validación para " + cliente + ".");
            return;
        }

        // 2. Creación del Pedido (Strategy Pattern)
        Pedido pedido = new Pedido(producto, cantidad, precioUnitario, facade.getImpuestoStrategy());

        // 3. Persistencia (Repository Pattern)
        facade.getRepository().guardar(pedido);

        // 4. Facturación (Adapter Pattern)
        facade.getFacturaService().generarFactura(cliente, pedido.getTotal());

        // 5. Mostrar Comprobante
        facade.mostrarComprobante(cliente, pedido);
        
        // 6. Notificación a Observadores (Observer Pattern)
        facade.notifyObservers(cliente, pedido.getTotal());

        System.out.println("[HILO-PROCESADOR] Procesamiento completado para " + cliente + ".");
    }
}