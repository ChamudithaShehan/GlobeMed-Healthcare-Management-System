package com.globemed.healthcare.patterns.composite;


public class Doctor implements StaffMember {
    private String name;
    private String specialty;

    public Doctor(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    @Override
    public void showDetails() {
        System.out.println("Doctor: " + name + ", Specialty: " + specialty);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRole() {
        return "Doctor (" + specialty + ")";
    }
}
