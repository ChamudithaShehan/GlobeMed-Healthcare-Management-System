package com.globemed.healthcare.patterns.bridge;


public class DirectBilling implements Billing {
    @Override
    public String processPayment(double amount) {
        String message = String.format("Processing direct payment of $%.2f. Please collect cash or card.", amount);
        System.out.println(message);
        return message;
    }
}
