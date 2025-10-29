package com.globemed.healthcare.patterns.security;

import java.time.LocalDateTime;

public class LoggingDecorator extends AuthDecorator {

    public LoggingDecorator(AuthenticationService decoratedService) {
        super(decoratedService);
    }

    @Override
    public boolean login(String username, String password) {
        System.out.println("[LOG] Login attempt for user '" + username + "' at " + LocalDateTime.now());
        boolean result = super.login(username, password);
        System.out.println("[LOG] Login attempt for user '" + username + "' resulted in: " + (result ? "SUCCESS" : "FAILURE"));
        return result;
    }
}
