package org.unibl.etf.assetmanager.db.model;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class InventoryItemFull implements Serializable {
    @Embedded
    private InventoryItem inventoryItem;
    @Relation(parentColumn = "assetId", entityColumn = "id")
    private Asset asset;
    @Relation(parentColumn = "newEmployeeId",entityColumn = "id")
    private Employee newEmployee;
    @Relation(parentColumn = "newLocationId",entityColumn = "id")
    private Location newLocation;

    public InventoryItemFull(InventoryItem inventoryItem, Asset asset, Employee newEmployee, Location newLocation) {
        this.inventoryItem = inventoryItem;
        this.asset = asset;
        this.newEmployee = newEmployee;
        this.newLocation = newLocation;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }

    public Location getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(Location newLocation) {
        this.newLocation = newLocation;
    }
}
