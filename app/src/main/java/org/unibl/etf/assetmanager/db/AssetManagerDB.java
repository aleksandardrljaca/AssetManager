package org.unibl.etf.assetmanager.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.unibl.etf.assetmanager.db.dao.AssetDAO;
import org.unibl.etf.assetmanager.db.dao.EmployeeDAO;
import org.unibl.etf.assetmanager.db.dao.InventoryItemDAO;
import org.unibl.etf.assetmanager.db.dao.InventoryListDAO;
import org.unibl.etf.assetmanager.db.dao.LocationDAO;
import org.unibl.etf.assetmanager.db.model.Asset;
import org.unibl.etf.assetmanager.db.model.Employee;
import org.unibl.etf.assetmanager.db.model.InventoryItem;
import org.unibl.etf.assetmanager.db.model.InventoryList;
import org.unibl.etf.assetmanager.db.model.Location;
import org.unibl.etf.assetmanager.util.Constants;
import org.unibl.etf.assetmanager.util.DateRoomConverter;


@Database(entities = { Asset.class, Employee.class, Location.class, InventoryItem.class, InventoryList.class }, version = 1)
@TypeConverters({ DateRoomConverter.class})
public abstract class AssetManagerDB extends RoomDatabase {
    public abstract AssetDAO getAssetDAO();
    public abstract EmployeeDAO getEmployeeDAO();
    public abstract LocationDAO getLocationDAO();
    public abstract InventoryItemDAO getInventoryItemDAO();
    public abstract InventoryListDAO getInventoryListDAO();
    private static AssetManagerDB assetTrackerDB;

    // synchronized is use to avoid concurrent access in multi thread environment
    public static /*synchronized*/ AssetManagerDB getInstance(Context context) {
        if (null == assetTrackerDB) {
            assetTrackerDB = buildDatabaseInstance(context);
        }
        return assetTrackerDB;
    }

    @NonNull
    private static AssetManagerDB buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AssetManagerDB.class,
                Constants.DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public  void cleanUp(){
        assetTrackerDB = null;
    }
}
