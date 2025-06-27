package com.curso.reactive.sec04;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec04FluxThreadSafetyExamples {

    public static void main(String[] args) {
//        log.info("=== Ejemplo 1: Un productor, múltiples suscriptores ===");
//        demo1UnProductorMultiplesSuscriptores();
//        
//        Util.sleepSeconds(5);
        
//        log.info("\n=== Ejemplo 2: Múltiples productores, un suscriptor ===");
//        demo2MultipleProductoresUnSuscriptor();
//        
//        Util.sleepSeconds(5);
        
        log.info("\n=== Ejemplo 3: Múltiples productores, múltiples suscriptores ===");
        demo3MultipleProductoresMultiplesSuscriptores();
        
        Util.sleepSeconds(5);
    }

    /**
     * Ejemplo 1: Un productor que emite 1000 elementos, múltiples suscriptores (3 threads)
     * Cada suscriptor recibe todos los elementos
     */
    private static void demo1UnProductorMultiplesSuscriptores() {
        // Sink thread-safe para un solo productor
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        Flux<String> flux = sink.asFlux();
        
        // Listas thread-safe para cada suscriptor
        List<String> lista1 = new CopyOnWriteArrayList<>();
        List<String> lista2 = new CopyOnWriteArrayList<>();
        List<String> lista3 = new CopyOnWriteArrayList<>();
        
        CountDownLatch latch = new CountDownLatch(3);
        
        // Suscriptor 1
        flux.subscribe(
            item -> {
                lista1.add("Subs1-" + item);
                log.info("Suscriptor 1 recibió: {}", item);
            },
            error -> log.error("Error en suscriptor 1: {}", error.getMessage()),
            () -> {
                log.info("Suscriptor 1 completado. Total elementos: {}", lista1.size());
                latch.countDown();
            }
        );
        
        // Suscriptor 2
        flux.subscribe(
            item -> {
                lista2.add("Subs2-" + item);
                log.info("Suscriptor 2 recibió: {}", item);
            },
            error -> log.error("Error en suscriptor 2: {}", error.getMessage()),
            () -> {
                log.info("Suscriptor 2 completado. Total elementos: {}", lista2.size());
                latch.countDown();
            }
        );
        
        // Suscriptor 3
        flux.subscribe(
            item -> {
                lista3.add("Subs3-" + item);
                log.info("Suscriptor 3 recibió: {}", item);
            },
            error -> log.error("Error en suscriptor 3: {}", error.getMessage()),
            () -> {
                log.info("Suscriptor 3 completado. Total elementos: {}", lista3.size());
                latch.countDown();
            }
        );
        
        // Un solo productor emitiendo 1000 elementos
        Thread productor = new Thread(() -> {
            log.info("Productor iniciado");
            for (int i = 1; i <= 1000; i++) {
                sink.tryEmitNext("Item-" + i);
                if (i % 100 == 0) {
                    log.info("Productor emitió {} elementos", i);
                }
            }
            sink.tryEmitComplete();
            log.info("Productor completado");
        });
        
        productor.start();
        
        try {
            latch.await();
            log.info("=== RESULTADO EJEMPLO 1 ===");
            log.info("Lista 1 tamaño: {}", lista1.size());
            log.info("Lista 2 tamaño: {}", lista2.size());
            log.info("Lista 3 tamaño: {}", lista3.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Ejemplo 2: Múltiples productores (5 threads) emitiendo elementos, un suscriptor
     * Total de 5000 elementos (1000 por cada productor)
     */
    private static void demo2MultipleProductoresUnSuscriptor() {
        // Lista thread-safe para el único suscriptor
        List<String> listaSuscriptor = Collections.synchronizedList(new java.util.ArrayList<>());
        
        int totalProductores = 5;
        CountDownLatch latch = new CountDownLatch(1);
        
        // Crear múltiples Flux, uno por cada productor
        Flux<String>[] fluxes = new Flux[totalProductores];
        
        for (int producerIndex = 1; producerIndex <= totalProductores; producerIndex++) {
            final int producerId = producerIndex;
            
            // Cada productor tiene su propio Flux
            fluxes[producerIndex - 1] = Flux.create(sink -> {
                new Thread(() -> {
                    log.info("Productor {} iniciado", producerId);
                    for (int i = 1; i <= 1000; i++) {
                        sink.next("Prod" + producerId + "-Item" + i);
                    }
                    log.info("Productor {} completado", producerId);
                    sink.complete();
                }).start();
            });
        }
        
        // Merge todos los Flux en uno solo
        Flux<String> fluxCombinado = Flux.merge(fluxes);
        
        // Un solo suscriptor
        fluxCombinado.subscribe(
            item -> {
                listaSuscriptor.add(item);
                if (listaSuscriptor.size() % 500 == 0) {
                    log.info("Suscriptor ha recibido {} elementos", listaSuscriptor.size());
                }
            },
            error -> log.error("Error en suscriptor: {}", error.getMessage()),
            () -> {
                log.info("Suscriptor completado. Total elementos recibidos: {}", listaSuscriptor.size());
                latch.countDown();
            }
        );
        
        try {
            latch.await();
            log.info("=== RESULTADO EJEMPLO 2 ===");
            log.info("Lista suscriptor tamaño: {}", listaSuscriptor.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Ejemplo 3: Múltiples productores (3 threads) y múltiples suscriptores (4 threads)
     * 3000 elementos total distribuidos entre 4 suscriptores (cada elemento procesado una vez)
     */
    private static void demo3MultipleProductoresMultiplesSuscriptores() {
        // Listas thread-safe para cada suscriptor
        List<String> listaSubs1 = new CopyOnWriteArrayList<>();
        List<String> listaSubs2 = new CopyOnWriteArrayList<>();
        List<String> listaSubs3 = new CopyOnWriteArrayList<>();
        List<String> listaSubs4 = new CopyOnWriteArrayList<>();
        
        int totalProductores = 3;
        int totalSuscriptores = 4;
        CountDownLatch latch = new CountDownLatch(totalSuscriptores);
        
        // Un solo sink que recibe todos los elementos
        Sinks.Many<String> mainSink = Sinks.many().multicast().onBackpressureBuffer();
        
        // Cola thread-safe para distribución
        java.util.concurrent.BlockingQueue<String> distributionQueue = new java.util.concurrent.LinkedBlockingQueue<>();
        
        AtomicInteger completedProducers = new AtomicInteger(0);
        AtomicInteger totalEmitted = new AtomicInteger(0);
        AtomicInteger distributedCounter = new AtomicInteger(0);
        
        // Crear threads de suscriptores que toman elementos de la cola
        List<String>[] listas = new List[]{listaSubs1, listaSubs2, listaSubs3, listaSubs4};
        
        for (int i = 0; i < totalSuscriptores; i++) {
            final int suscriptorId = i + 1;
            final List<String> lista = listas[i];
            
            Thread suscriptor = new Thread(() -> {
                try {
                    while (true) {
                        String elemento = distributionQueue.take(); // Bloquea hasta obtener elemento
                        if ("STOP".equals(elemento)) {
                            break; // Señal de parada
                        }
                        lista.add("S" + suscriptorId + "-" + elemento);
                        if (lista.size() % 100 == 0) {
                            log.info("Suscriptor {}: {} elementos procesados", suscriptorId, lista.size());
                        }
                    }
                    log.info("Suscriptor {} completado. Total procesado: {}", suscriptorId, lista.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("Suscriptor {} interrumpido. Total procesado: {}", suscriptorId, lista.size());
                } finally {
                    latch.countDown();
                }
            });
            suscriptor.start();
        }
        
        // Crear productores que envían elementos a la cola thread-safe
        for (int producerIndex = 1; producerIndex <= totalProductores; producerIndex++) {
            final int producerId = producerIndex;
            
            Thread productor = new Thread(() -> {
                log.info("Productor {} iniciado", producerId);
                for (int i = 1; i <= 1000; i++) {
                    String elemento = "P" + producerId + "-Item" + i;
                    try {
                        distributionQueue.put(elemento); // put() es thread-safe y blocking
                        totalEmitted.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
                int completed = completedProducers.incrementAndGet();
                log.info("Productor {} completado. Elementos emitidos hasta ahora: {}", producerId, totalEmitted.get());
                
                // Cuando todos los productores terminen, enviar señales de parada
                if (completed == totalProductores) {
                    try {
                        // Enviar señal de parada a todos los suscriptores
                        for (int i = 0; i < totalSuscriptores; i++) {
                            distributionQueue.put("STOP");
                        }
                        log.info("Todos los productores completados. Total elementos emitidos: {}", totalEmitted.get());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            productor.start();
        }

        
        try {
            latch.await();
            log.info("=== RESULTADO EJEMPLO 3 - DISTRIBUCIÓN DE TRABAJO ===");
            log.info("Suscriptor 1 procesó: {} elementos", listaSubs1.size());
            log.info("Suscriptor 2 procesó: {} elementos", listaSubs2.size());
            log.info("Suscriptor 3 procesó: {} elementos", listaSubs3.size());
            log.info("Suscriptor 4 procesó: {} elementos", listaSubs4.size());
            int totalProcesado = listaSubs1.size() + listaSubs2.size() + 
                              listaSubs3.size() + listaSubs4.size();
            log.info("Total elementos procesados (debe ser 3000): {}", totalProcesado);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 