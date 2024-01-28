package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealWithRelations;

import java.time.LocalDate;
import java.util.List;

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
