package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.MealCategory;
import com.example.fitnessapp.Database.Models.MeasureUnit;

import java.util.List;

@Dao
public interface MealCategoryDao {
    @Query("SELECT * FROM meal_category")
    LiveData<List<MealCategory>> getAll();
}
