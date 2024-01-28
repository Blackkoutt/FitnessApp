package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.MealCategory;

import java.util.List;

@Dao
public interface MealCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MealCategory category);
    @Query("SELECT * FROM meal_category")
    LiveData<List<MealCategory>> getAll();
}
