package org.unibl.etf.assetmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;

import java.util.List;

@Dao
public interface EmployeeDAO {
    @Transaction
    @Query("SELECT * FROM employee")
    List<EmployeeFull> getAllEmployees();
    @Transaction
    @Query("DELETE FROM employee WHERE id=:id")
    void delete(Long id);
    @Transaction
    @Query("SELECT * from asset inner join employee on asset_employeeId=:id")
    List<AssetFull> getAllLiabilities(int id);
    @Transaction
    @Query("UPDATE employee SET firstName=:firstName, lastName=:lastName WHERE id=:id")
    void update(Long id, String firstName,String lastName);
    @Transaction
    @Query("INSERT INTO employee(firstName,lastName) values(:firstName,:lastName)")
    void insert(String firstName,String lastName);
    @Transaction
    @Query("SELECT id from employee WHERE firstName=:firstName and lastName=:lastName")
    Long getIdByName(String firstName,String lastName);

    @Transaction
    @Query("SELECT * from employee WHERE id=:id")
    EmployeeFull getById(Long id);
}
