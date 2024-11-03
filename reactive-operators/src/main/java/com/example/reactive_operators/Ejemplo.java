package com.example.reactive_operators;

import io.reactivex.Observable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ejemplo {

   public static void main(String[] args) {
        // Lista de transacciones simuladas
        List<Transaction> transactions = Arrays.asList(
                new Transaction("user123", 100.0, true),
                new Transaction("user456", 50.0, false),
                new Transaction("user123", 75.0, true),
                new Transaction("user123", 200.0, true),
                new Transaction("user123", 120.0, true)
        );
        // Tasa de cambio base
        double baseExchangeRate = 1.2;
        // Tasas de cambio adicionales según el monto (ejemplo)
        List<Double> exchangeRates = Arrays.asList(1.2, 1.15, 1.3);
        // Usuario objetivo
        String targetUser = "user123";

        Observable.fromIterable(transactions)
                // Filtrar solo las transacciones exitosas del usuario específico
                .filter(transaction -> transaction.isSuccess() && transaction.getUserId().equals(targetUser))
                // Aplicar una tasa de cambio diferente según el monto usando zip
                .zipWith(Observable.fromIterable(exchangeRates).repeat(), (transaction, rate) -> 
                        transaction.getAmount() * rate)
                // Dividir las transacciones grandes (mayores a 100) en múltiples transacciones pequeñas
                .flatMap(amount -> amount > 100 
                        ? Observable.fromIterable(divideAmount(amount, 3)) 
                        : Observable.just(amount))
                // Aplicar otra conversión para calcular la cantidad total en la nueva moneda
                .map(amount -> amount * baseExchangeRate)
                // Sumar el total de todas las transacciones
                .reduce(Double::sum)
                // Suscribirse e imprimir el resultado
                .subscribe(
                        total -> System.out.println("Total converted amount for " + targetUser + ": " + total),
                        Throwable::printStackTrace
                );
    }
    // Método auxiliar para dividir un monto en varias partes
    private static List<Double> divideAmount(double amount, int parts) {
        return Stream.generate(() -> amount / parts)
                     .limit(parts)
                     .collect(Collectors.toList());
    }
}
