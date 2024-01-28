package com.example.fitnessapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnessapp.Database.Models.ProductCategory;

@Dao
public interface ProductCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProductCategory product_category);
    @Query("DELETE FROM ProductCategory")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='ProductCategory'")
    void resetTableId();
    @Query("DELETE FROM ProductCategory WHERE productId = :productId")
    void deleteByProductId(long productId);
}
