package com.globemed.healthcare.patterns.composite;


public class Pharmacist implements StaffMember {
    private String name;

    public Pharmacist(String name) {
        this.name = name;
    }

    @Override
    public void showDetails() {
        System.out.println("Pharmacist: " + name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRole() {
        return "Pharmacist";
    }
}
