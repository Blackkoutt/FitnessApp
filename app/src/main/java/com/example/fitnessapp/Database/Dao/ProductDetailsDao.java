package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnessapp.Database.Models.ProductDetails;

import java.util.List;

@Dao
public interface ProductDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProductDetails details);
    @Query("SELECT * FROM productDetails")
    LiveData<List<ProductDetails>> getAll();
    @Query("DELETE FROM productDetails")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='productDetails'")
    void resetTableId();

    @Delete
    void delete(ProductDetails productDetails);
    @Update
    void update(ProductDetails details);
}
