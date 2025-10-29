package com.globemed.healthcare.patterns.composite;

public class Nurse implements StaffMember {
    private String name;
    private String department;

    public Nurse(String name, String department) {
        this.name = name;
        this.department = department;
    }

    @Override
    public void showDetails() {
        System.out.println("Nurse: " + name + ", Department: " + department);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRole() {
        return "Nurse";
    }
}
