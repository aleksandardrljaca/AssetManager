package org.unibl.etf.assetmanager.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.unibl.etf.assetmanager.util.Constants;

@Entity(tableName = Constants.TABLE_NAME_INVENTORY_LIST)
public class InventoryList {
    @PrimaryKey
    private Long id;
    private String name;
    public InventoryList(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
