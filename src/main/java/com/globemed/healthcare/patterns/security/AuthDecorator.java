package com.globemed.healthcare.patterns.security;

public abstract class AuthDecorator implements AuthenticationService {
    protected AuthenticationService decoratedService;

    public AuthDecorator(AuthenticationService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override
    public boolean login(String username, String password) {
        return decoratedService.login(username, password);
    }
}
