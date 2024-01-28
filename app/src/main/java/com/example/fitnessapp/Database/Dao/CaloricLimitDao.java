package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnessapp.Database.Models.CaloricLimit;

import java.time.LocalDate;

@Dao
public interface CaloricLimitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CaloricLimit limit);
    @Transaction
    @Query("SELECT * FROM caloric_limit WHERE date=:date")
    LiveData<CaloricLimit> getByDate(LocalDate date);
    @Update
    void update(CaloricLimit limit);
}
