package edu.jpa.TABLE_PER_CLASS.entity;

import jakarta.persistence.Entity;

@Entity
public class Employee extends Person {
    private double salary;

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
