package org.unibl.etf.assetmanager.db.dao;

import androidx.room.Dao;
import androidx.room.ForeignKey;
import androidx.room.Query;
import androidx.room.Transaction;

import org.unibl.etf.assetmanager.db.model.InventoryListFull;

import java.util.List;

@Dao
public interface InventoryListDAO {
    @Transaction
    @Query("SELECT * FROM inventory_list")
    List<InventoryListFull> getAllInventoryLists();
    @Transaction
    @Query("DELETE FROM inventory_list WHERE id=:id")
    void delete(Long id);
    @Transaction
    @Query("INSERT INTO inventory_list(name) values(:name)")
    void insert(String name);
    @Transaction
    @Query("UPDATE inventory_list SET name=:name WHERE id=:id")
    void update(Long id,String name);
    @Transaction
    @Query("SELECT MAX(id) FROM inventory_list")
    Long getMaxId();

}
