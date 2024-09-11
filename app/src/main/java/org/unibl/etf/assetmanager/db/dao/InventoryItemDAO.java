package org.unibl.etf.assetmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.unibl.etf.assetmanager.db.model.InventoryItemFull;

import java.util.List;
@Dao
public interface InventoryItemDAO {
    @Transaction
    @Query("SELECT * FROM inventory_item")
    List<InventoryItemFull> getAllInventoryItems();
    @Transaction
    @Query("SELECT * FROM inventory_item WHERE listId=:id")
    List<InventoryItemFull> getAllByInventoryListId(Long id);
    @Transaction
    @Query("DELETE FROM inventory_item WHERE listId=:listId")
    void deleteAllFromInventoryList(Long listId);

    @Transaction
    @Query("INSERT INTO inventory_item(assetId,newLocationId,newEmployeeId,listId) values(:assetId,:locationId,:employeeId,:listId)")
    void insert(Long assetId,Long locationId,Long employeeId,Long listId);

    @Transaction
    @Query("SELECT * FROM inventory_item WHERE assetId=:id")
    List<InventoryItemFull> findAllByAssetId(Long id);
}
