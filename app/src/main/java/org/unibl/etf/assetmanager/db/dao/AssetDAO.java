package org.unibl.etf.assetmanager.db.dao;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.jetbrains.annotations.NotNull;
import org.unibl.etf.assetmanager.db.model.AssetFull;

import java.util.Date;
import java.util.List;

@Dao
public interface AssetDAO {
    @Transaction
    @Query("SELECT * FROM asset")
    List<AssetFull> getAllAssets();

    @Transaction
    @Query("DELETE FROM asset WHERE id=:id")
    void delete(Long id);
    @Transaction
    @Query("INSERT INTO asset(name,description,barcode,price,creationDate,imageUri,asset_locationId,asset_employeeId) " +
            "values(:name,:description,:barcode,:price,:date,:imageURI,:locationId,:employeeId)")
    void insert(String name,String description,Long barcode,Double price,Date date,String imageURI,Long locationId,Long employeeId);
    @Transaction
    @Query("SELECT * from asset where asset_locationId=:id")
    List<AssetFull> getAllAssetsByLocationId(Long id);

    @Transaction
    @Query("SELECT * from asset where asset_employeeId=:id")
    List<AssetFull> getAllAssetsByEmployeeId(Long id);
    @Transaction
    @Query("UPDATE asset SET name=:name,description=:description,barcode=:barcode,price=:price,creationDate=:date," +
            "imageURI=:imageURI,asset_locationId=:locationId,asset_employeeId=:employeeId where id=:id")
    void update(Long id,String name,String description,Long barcode,Double price,Date date,String imageURI,Long locationId,Long employeeId);
    @Transaction
    @Query("SELECT * from asset WHERE barcode=:barcode")
    AssetFull getAllByBarcode(Long barcode);
}
