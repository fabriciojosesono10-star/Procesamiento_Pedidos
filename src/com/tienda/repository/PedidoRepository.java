package com.tienda.repository;

import com.tienda.model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private final List<Pedido> pedidos = new ArrayList<>();
    
    // AÑADE 'synchronized' AQUÍ
    public synchronized void guardar(Pedido pedido) { 
        if (pedido == null) {
            System.out.println("[Repository] Advertencia: intento de guardar un pedido nulo");
            return; // mejora sin tocar estructura
        }

        pedidos.add(pedido);
        System.out.println("[Repository] Pedido guardado: #" + pedidos.size()); 
    }

    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos); // copia segura
    }
}
