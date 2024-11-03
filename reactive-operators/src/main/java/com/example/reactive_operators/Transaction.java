package com.example.reactive_operators;

// Clase para representar una transacción bancaria
class Transaction {
    private final String userId;
    private final double amount;
    private final boolean isSuccess;

    public Transaction(String userId, double amount, boolean isSuccess) {
        this.userId = userId;
        this.amount = amount;
        this.isSuccess = isSuccess;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}