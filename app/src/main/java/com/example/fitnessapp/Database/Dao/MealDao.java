package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Meal meal);

    @Transaction
    @Query("SELECT * FROM meal")
    LiveData<List<MealWithRelations>> getAll();

    @Transaction
    @Query("SELECT * FROM meal WHERE date=:date")
    LiveData<List<MealWithRelations>> getAllByDate(LocalDate date);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);
}
