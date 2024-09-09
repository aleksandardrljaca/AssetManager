package org.unibl.etf.assetmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;

import java.util.List;
@Dao
public interface LocationDAO {
    @Transaction
    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Transaction
    @Query("DELETE FROM location WHERE id=:id")
    void delete(Long id);
    @Transaction
    @Query("INSERT INTO location(name) values(:location)")
    void insert(String location);

    @Transaction
    @Query("UPDATE location SET name=:location WHERE id=:id")
    void update(Long id,String location);
    @Transaction
    @Query("SELECT id from location WHERE name=:name")
    Long getIdByName(String name);
    @Transaction
    @Query("SELECT * from location WHERE id=:id")
    Location getLocationById(Long id);
}
