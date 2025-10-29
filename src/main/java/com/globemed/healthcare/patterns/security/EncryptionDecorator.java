package com.globemed.healthcare.patterns.security;

public class EncryptionDecorator extends AuthDecorator {

    public EncryptionDecorator(AuthenticationService decoratedService) {
        super(decoratedService);
    }

    @Override
    public boolean login(String username, String password) {
        System.out.println("Applying encryption...");
        String encryptedPassword = new StringBuilder(password).reverse().toString();
        System.out.println("Note: This is a simulated encryption. In a real app, never do this.");
        return super.login(username, password);
    }
}
