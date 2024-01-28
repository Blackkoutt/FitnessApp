package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.Manufacturer;

import java.util.List;

@Dao
public interface ManufacturerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Manufacturer manufacturer);
    @Query("SELECT * FROM manufacturer")
    LiveData<List<Manufacturer>> getAll();
    @Query("DELETE FROM manufacturer")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='manufacturer'")
    void resetTableId();
}
