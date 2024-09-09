package org.unibl.etf.assetmanager.db.model;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class AssetFull implements Serializable {
    @Embedded
    private Asset asset;
    @Relation(parentColumn = "asset_locationId", entityColumn = "id")
    private Location location;
    @Relation(parentColumn = "asset_employeeId", entityColumn = "id" )
    private Employee employee;

    public AssetFull(Asset asset, Location location, Employee employee) {
        this.asset = asset;
        this.location = location;
        this.employee = employee;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
