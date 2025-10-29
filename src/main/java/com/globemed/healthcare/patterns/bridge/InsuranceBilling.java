package com.globemed.healthcare.patterns.bridge;


public class InsuranceBilling implements Billing {
    @Override
    public String processPayment(double amount) {
        String message = String.format("Processing insurance claim for $%.2f. Submitting to insurance provider.", amount);
        System.out.println(message);
        return message;
    }
}
