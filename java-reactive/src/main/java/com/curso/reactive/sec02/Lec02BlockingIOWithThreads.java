package com.curso.reactive.sec02;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.curso.reactive.common.BlockingHttpServiceClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec02BlockingIOWithThreads {

	public static void main(String[] args) {
		// Crear un pool de hilos para manejar las solicitudes
		ExecutorService executor = Executors.newFixedThreadPool(10);

		var client = new BlockingHttpServiceClient();

		log.info("Starting blocking IO with threads...");

		for (int i = 0; i < 1000; i++) {
			final int productId = i;
			executor.submit(() -> {
				try {
					String productName = client.getProductName(productId);
					log.info("Product ID: {}, Name: {}", productId, productName);
				} catch (IOException e) {
					log.error("Error fetching product name for ID {}: {}", productId, e.getMessage());
				} catch (InterruptedException e) {
					log.error("Thread interrupted", e);
				}
			});
		}

		// Dar tiempo a las tareas para completarse
		try {
			executor.shutdown();
			boolean terminated = executor.awaitTermination(10, TimeUnit.SECONDS);
			if (!terminated) {
				log.warn("Algunas tareas no terminaron en el tiempo esperado");
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			log.error("Interrumpido mientras se esperaba la terminación: {}", e.getMessage());
			executor.shutdownNow();
		}

		log.info("Finalizado");

	}

}
