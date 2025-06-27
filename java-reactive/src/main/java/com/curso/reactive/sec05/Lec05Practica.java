package com.curso.reactive.sec05;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

//Un banco procesa transacciones (montos en euros). Algunas transacciones pueden fallar por:
//
//División por cero (error lógico).
//Saldo insuficiente (excepción personalizada).
//Conexión intermitente (error temporal).

//Queremos:
//
//Reintentar conexiones fallidas.
//Ignorar transacciones con saldo insuficiente y continuar.
//Sustituir errores lógicos con un valor por defecto.
//Transformar errores para estandarizar mensajes.

class InsufficientFundsException extends RuntimeException {
	private static final long serialVersionUID = -6947559540283352587L;

	public InsufficientFundsException(String message) {
		super(message);
	}
}

//Clase para procesar transacciones bancarias
class TransactionProcessor {
	// Simula el procesamiento de una transacción con posible fallo de conexión
	public static Double processTransaction(Double amount) {
		if (Math.random() > 0.4) { // 40% de chance de fallo de conexión
			throw new RuntimeException("Conexión intermitente fallida");
		}
		return amount * 0.95; // Aplica 5% de comisión
	}
}

@Slf4j
public class Lec05Practica {

	public static void main(String[] args) {
		log.info("=== Procesamiento de Transacciones Bancarias ===");
		
		Flux.just(100.0, 50.0, 0.0, 30.0, 20.0)
			// Procesar cada transacción individualmente con manejo de errores
			.flatMap(amount -> 
				Flux.just(amount)
					// 1. Validar y manejar errores de negocio
					.map(amt -> {
						if (amt == 0.0) throw new ArithmeticException("División por cero");
						if (amt < 25.0) throw new InsufficientFundsException("Saldo insuficiente: " + amt);
						return amt;
					})
					// 2. Sustituir errores lógicos (división por cero) con valor por defecto
					.onErrorReturn(ArithmeticException.class, 0.0)
					
					// 3. Ignorar transacciones con saldo insuficiente y continuar
					.onErrorResume(InsufficientFundsException.class, error -> {
						log.warn("Transacción ignorada - {}", error.getMessage());
						return Flux.empty(); // Omitir esta transacción
					})
					
					// 4. Procesar transacción (puede fallar por conexión)
					.map(validAmount -> {
						log.info("Procesando transacción: {} euros", validAmount);
						return TransactionProcessor.processTransaction(validAmount);
					})
					
					// 5. Reintentar conexiones fallidas (hasta 3 veces con delay)
					.retryWhen(Retry.backoff(3, Duration.ofMillis(500))
						.filter(throwable -> throwable.getMessage().contains("Conexión intermitente"))
						.doBeforeRetry(retrySignal -> 
							log.warn("Reintentando conexión (intento {})", retrySignal.totalRetries() + 1))
						.onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
							return new RuntimeException("Conexión fallida después de 3 intentos: " + 
								retrySignal.failure().getMessage());
						}))
					
					// 6. Transformar errores para estandarizar mensajes
					.onErrorMap(RuntimeException.class, error -> 
						new RuntimeException("Error procesando transacción: " + error.getMessage()))
					
					// 7. Manejar errores finales que no pudieron ser procesados
					.onErrorResume(error -> {
						log.error("Error final no manejado: {}", error.getMessage());
						return Flux.empty(); // Omitir transacciones con errores irrecuperables
					})
			)
			
			// Procesar resultados
			.doOnNext(result -> log.info("Transacción completada. Monto final: {} euros", result))
			.doOnComplete(() -> log.info("=== Procesamiento de todas las transacciones completado ==="))
			.doOnError(error -> log.error("Error crítico en el flujo: {}", error.getMessage()))
			
			// Bloquear para ver resultados
			.blockLast();
	}

}
