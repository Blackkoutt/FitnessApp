package com.example.fitnessapp.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;

import java.util.List;

@Dao
public interface ProductDao {
    //@Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);
    @Transaction
    @Query("SELECT * FROM product")
    LiveData<List<ProductWithRelations>> getAll();
    @Update
    void update(Product product);
    @Query("DELETE FROM product")
    void clearTable();
    @Query("DELETE FROM sqlite_sequence WHERE name='product'")
    void resetTableId();
    @Delete
    void delete(Product product);
}
