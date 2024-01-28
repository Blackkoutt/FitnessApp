package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);
    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAll();
    @Query("DELETE FROM category")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='category'")
    void resetTableId();
}
