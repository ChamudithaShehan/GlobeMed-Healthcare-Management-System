package com.globemed.healthcare.patterns.security;

import com.globemed.healthcare.model.User;

public abstract class AuthHandler {
    protected AuthHandler next;

    public void setNext(AuthHandler next) {
        this.next = next;
    }

    public abstract boolean handleRequest(String username, String password);
}
