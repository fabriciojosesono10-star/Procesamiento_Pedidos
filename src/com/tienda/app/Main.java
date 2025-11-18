package com.tienda.app;

import com.tienda.facade.PedidoFacade;
import com.tienda.facade.PedidoProcessorTask;
import com.tienda.strategy.IGV18Strategy;
import com.tienda.strategy.ExoneradoStrategy;
import com.tienda.strategy.ImpuestoStrategy;
import com.tienda.observer.LogObserver;
import com.tienda.observer.ClienteNotifierObserver;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PedidoFacade facade = new PedidoFacade();

        // 1. Configuración del Pool de Hilos
        ExecutorService executor = Executors.newFixedThreadPool(2); 

        // 2. Configuración del Patrón Observer
        facade.attach(new LogObserver());
        facade.attach(new ClienteNotifierObserver());
        System.out.println("BIENVENIDO");
 
        // 3. Selección de Estrategia de Impuestos
        System.out.println("--- Proceso de Pedido ---");
        System.out.println("Seleccione la estrategia de impuestos:");
        System.out.println("1. IGV (18%)");
        System.out.println("2. Exonerado (0%)");
        System.out.print("Opcion: ");
        
        ImpuestoStrategy selectedStrategy = null;
        try {
            // Leemos como línea y convertimos, la mejor práctica con Scanner
            int opcionImpuesto = Integer.parseInt(sc.nextLine()); 

            switch (opcionImpuesto) {
                case 1:
                    selectedStrategy = new IGV18Strategy();
                    System.out.println("Estrategia seleccionada: IGV (18%)");
                    break;
                case 2:
                    selectedStrategy = new ExoneradoStrategy();
                    System.out.println("Estrategia seleccionada: Exonerado (0%)");
                    break;
                default:
                    selectedStrategy = new IGV18Strategy();
                    System.out.println("Opción inválida. Usando IGV (18%) por defecto.");
            }
        } catch (NumberFormatException e) {
            selectedStrategy = new IGV18Strategy();
            System.out.println("Error de formato. Usando IGV (18%) por defecto.");
        }
        facade.setImpuestoStrategy (selectedStrategy);

        // 4. Captura de datos del Pedido 1 (Tarea 1 para el HILO)
        System.out.println("\n--- PROCESO DE PEDIDO 1");
        System.out.print("Ingrese nombre del cliente: ");
        String cliente1 = sc.nextLine();
        System.out.print("Ingrese producto (laptop/teclado/mouse): ");
        String producto1 = sc.nextLine().toLowerCase().trim();
        System.out.print("Ingrese cantidad: ");
        
        int cantidad1 = 0;
        try {
            // Leemos como línea y convertimos
            cantidad1 = Integer.parseInt(sc.nextLine()); 
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida. Usando 1 por defecto.");
            cantidad1 = 1; 
        }
        
        double precioUnitario1 = obtenerPrecio(producto1);
        
        if (precioUnitario1 == 0 || cantidad1 <= 0) {
            System.out.println("Datos del pedido 1 inválidos. Terminando.");
            executor.shutdown();
            return;
        }
        
        // 5. Lanzamiento del HILO 1
        System.out.println("LANZANDO HILO 1 para " + cliente1 + "...");
        Runnable processor1 = new PedidoProcessorTask(facade, producto1, cantidad1, precioUnitario1, cliente1);
        executor.execute(processor1);

        try {
            // Espera 100ms para que el primer hilo imprima su salida inicial
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            // Si el hilo es interrumpido, restauramos el estado
            Thread.currentThread().interrupt();
        }
        
        // 6. INGRESO MANUAL DEL SEGUNDO PEDIDO (Tarea 2 para el HILO)
        System.out.println("\n---PROCESO DE PEDIDO 2---");
        System.out.print("Ingrese nombre del cliente 2: ");
        // Leemos la línea sin problemas gracias a la pausa
        String cliente2 = sc.nextLine(); 
        
        System.out.print("Ingrese producto 2 (laptop/teclado/mouse): ");
        String producto2 = sc.nextLine().toLowerCase().trim();
        System.out.print("Ingrese cantidad 2: ");

        int cantidad2 = 0;
        try {
            cantidad2 = Integer.parseInt(sc.nextLine()); 
        } catch (NumberFormatException e) {
            System.out.println("Cantidad 2 inválida. Usando 1 por defecto.");
            cantidad2 = 1;
        }

        double precioUnitario2 = obtenerPrecio(producto2);
        
        if (precioUnitario2 == 0 || cantidad2 <= 0) {
            System.out.println("Datos del pedido 2 inválidos. Terminando.");
            executor.shutdown();
            return;
        }
        
        // 7. Lanzamiento del HILO 2
        System.out.println("LANZANDO HILO 2 para" + cliente2 + "...");
        Runnable processor2 = new PedidoProcessorTask(facade, producto2, cantidad2, precioUnitario2, cliente2);
        executor.execute(processor2);
        
        // 8. Cierre del Executor
        executor.shutdown(); 

        System.out.println("\n*** El Main Thread termina. Los Worker Threads (Hilos) procesan los pedidos concurrentemente... ***");
    }
    
    // Método utilitario (sin cambios)
    private static double obtenerPrecio (String producto) { 
        switch (producto) {
            case "laptop": return 4000;
            case "teclado": return 120;
            case "mouse": return 80;
            default: return 0;
        }
    }
}