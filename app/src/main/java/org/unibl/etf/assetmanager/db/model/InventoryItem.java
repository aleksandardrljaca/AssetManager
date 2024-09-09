package org.unibl.etf.assetmanager.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.unibl.etf.assetmanager.util.Constants;

import java.io.Serializable;
@Entity(tableName = Constants.TABLE_NAME_INVENTORY_ITEM)
public class InventoryItem implements Serializable {
    @PrimaryKey
    private Long id;
    private Long assetId;
    private Long newLocationId;
    private Long newEmployeeId;
    private Long listId;

    public InventoryItem(Long id, Long assetId, Long newLocationId, Long newEmployeeId, Long listId) {
        this.id = id;
        this.assetId = assetId;
        this.newLocationId = newLocationId;
        this.newEmployeeId = newEmployeeId;
        this.listId = listId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getNewLocationId() {
        return newLocationId;
    }

    public void setNewLocationId(Long newLocationId) {
        this.newLocationId = newLocationId;
    }

    public Long getNewEmployeeId() {
        return newEmployeeId;
    }

    public void setNewEmployeeId(Long newEmployeeId) {
        this.newEmployeeId = newEmployeeId;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }
}
