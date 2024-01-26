package com.example.fitnessapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.MealProduct;
import com.example.fitnessapp.Database.Models.ProductCategory;

@Dao
public interface MealProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MealProduct meal_product);

    @Query("DELETE FROM MealProduct WHERE mealId = :mealId")
    void deleteByMealId(long mealId);
    @Query("DELETE FROM MealProduct WHERE productId = :productId")
    void deleteByProductId(long productId);
}
