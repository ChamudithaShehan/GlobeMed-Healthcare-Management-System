package com.globemed.healthcare.patterns.security;

public class BasicAuthenticationService implements AuthenticationService {
    private AuthHandler authChain;

    public BasicAuthenticationService() {
        this.authChain = new UsernamePasswordHandler();
        AuthHandler roleCheckHandler = new RoleCheckHandler();
        this.authChain.setNext(roleCheckHandler);
    }

    @Override
    public boolean login(String username, String password) {
        System.out.println("Executing basic authentication...");
        return authChain.handleRequest(username, password);
    }
}
