package org.unibl.etf.assetmanager.db.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class EmployeeFull implements Serializable {
    @Embedded
    private Employee employee;
    @Relation(parentColumn = "id", entityColumn = "asset_employeeId")
    private List<Asset> liabilities;

    public EmployeeFull(Employee employee, List<Asset> liabilities) {
        this.employee = employee;
        this.liabilities = liabilities;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Asset> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<Asset> liabilities) {
        this.liabilities = liabilities;
    }

    @Override
    public String toString(){
        return employee.getFirstName()+" "+employee.getLastName();
    }
}
