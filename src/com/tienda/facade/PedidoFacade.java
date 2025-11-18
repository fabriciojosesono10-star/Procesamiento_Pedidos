package com.tienda.facade;

import com.tienda.model.Pedido;
import com.tienda.service.ValidadorStock;
import com.tienda.adapter.FacturaService;
import com.tienda.adapter.FacturaAdapter;
import com.tienda.strategy.ImpuestoStrategy;
import com.tienda.repository.PedidoRepository;
import com.tienda.observer.PedidoObserver; // Nuevo
import java.util.List;
import java.util.ArrayList; // Nuevo

public class PedidoFacade {
    private ValidadorStock validador = new ValidadorStock();
    private FacturaService facturaService = new FacturaAdapter();
    private PedidoRepository repository = new PedidoRepository();
    private ImpuestoStrategy impuestoStrategy;
    
    // Lista para almacenar los observadores (Observer Pattern)
    private final List<PedidoObserver> observers = new ArrayList<>(); // Nuevo

    // -- MÉTODOS OBSERVER --
    public void attach(PedidoObserver observer) { // Nuevo
        observers.add(observer);
    }

    public void notifyObservers(String cliente, double total) { // Nuevo
        for (PedidoObserver observer : observers) {
            observer.onPedidoProcesado(cliente, total);
        }
    }
    // ------------------------

    // -- GETTERS para el Hilo --
    public ValidadorStock getValidador() { return validador; }
    public PedidoRepository getRepository() { return repository; }
    public FacturaService getFacturaService() { return facturaService; }
    public ImpuestoStrategy getImpuestoStrategy() { return impuestoStrategy; }
    // --------------------------

    public void setImpuestoStrategy (ImpuestoStrategy strategy) {
        this.impuestoStrategy = strategy;
    }

    // El antiguo 'procesarPedido' ha sido movido a PedidoProcessorTask

    // Método 'obtenerPedidosGuardados' sin cambios.
    public List<Pedido> obtenerPedidosGuardados() {
        return repository.obtenerTodos();
    }

    // Método 'mostrarComprobante' sin cambios, usado por el HILO
    public void mostrarComprobante (String cliente, Pedido pedido) {
        System.out.println("--- Comprobante para " + cliente + " ---");
        System.out.println("Producto: " + pedido.getProducto());
        System.out.println("Subtotal: S/" + String.format("%.2f", pedido.getSubtotal()));
        System.out.println("IGV: S/" + String.format("%.2f", pedido.getIgv()));
        System.out.println("Total: S/" + String.format("%.2f", pedido.getTotal()));
        System.out.println("------------------------------------");
    }
}