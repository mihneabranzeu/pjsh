package edu.jpa.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class DepartmentKey implements Serializable {
    private String companyName;
    private String departmentName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
