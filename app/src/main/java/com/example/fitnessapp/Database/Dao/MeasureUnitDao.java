package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;

import java.util.List;

@Dao
public interface MeasureUnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MeasureUnit measureUnit);
    @Query("SELECT * FROM measureUnit")
    LiveData<List<MeasureUnit>> getAll();
    @Query("DELETE FROM measureUnit")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='measureUnit'")
    void resetTableId();
}
