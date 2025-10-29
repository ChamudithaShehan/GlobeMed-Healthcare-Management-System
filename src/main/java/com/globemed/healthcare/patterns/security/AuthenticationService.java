package com.globemed.healthcare.patterns.security;

public interface AuthenticationService {
    boolean login(String username, String password);
}
