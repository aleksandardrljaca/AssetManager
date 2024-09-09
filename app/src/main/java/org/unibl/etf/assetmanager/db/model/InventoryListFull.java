package org.unibl.etf.assetmanager.db.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class InventoryListFull implements Serializable {
    @Embedded
    private InventoryList inventoryList;
    @Relation(parentColumn = "id",entityColumn = "id")
    private List<InventoryItem> items;

    public InventoryListFull(InventoryList inventoryList, List<InventoryItem> items) {
        this.inventoryList = inventoryList;
        this.items = items;
    }

    public InventoryList getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(InventoryList inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}
