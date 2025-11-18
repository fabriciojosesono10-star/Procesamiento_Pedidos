package com.tienda.repository;

import com.tienda.model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private final List<Pedido> pedidos = new ArrayList<>();
    
    // AÑADE 'synchronized' AQUÍ
    public synchronized void guardar(Pedido pedido) { 
        pedidos.add(pedido);
        // Ahora el número de pedido será secuencial y correcto
        System.out.println("[Repository] Pedido guardado: #" + pedidos.size()); 
    }

    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos);
    }
}