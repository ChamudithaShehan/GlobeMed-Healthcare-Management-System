package com.globemed.healthcare.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class BillItem {
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty cost;

    public BillItem(String description, double cost) {
        this.description = new SimpleStringProperty(description);
        this.cost = new SimpleDoubleProperty(cost);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }
}
