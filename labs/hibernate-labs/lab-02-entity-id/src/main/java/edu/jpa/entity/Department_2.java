package edu.jpa.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Department_2 {
    @EmbeddedId
    private DepartmentKey id;
    private String description;

    public DepartmentKey getId() {
        return id;
    }

    public void setId(DepartmentKey id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
