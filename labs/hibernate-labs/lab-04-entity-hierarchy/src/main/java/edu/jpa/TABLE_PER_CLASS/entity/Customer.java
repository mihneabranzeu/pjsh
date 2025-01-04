package edu.jpa.TABLE_PER_CLASS.entity;

import jakarta.persistence.Entity;

@Entity
public class Customer extends Person {
    private double discount;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
