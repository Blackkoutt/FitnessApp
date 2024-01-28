package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.ProductDetailsDao;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class ProductDetailsRepository {
    private final ProductDetailsDao detailsDao;
    private final LiveData<List<ProductDetails>> details;

    public ProductDetailsRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        detailsDao = database.productDetailsDao();
        details = detailsDao.getAll();
    }
    public LiveData<List<ProductDetails>> getAllDetails(){return details; }

    public void insert(ProductDetails details) {ProductDatabase.databaseWriteExecutor.execute(()->detailsDao.insert(details));}

    public void delete(ProductDetails details){
        ProductDatabase.databaseWriteExecutor.execute(()->detailsDao.delete(details));
    }

    public void update(ProductDetails details){
        ProductDatabase.databaseWriteExecutor.execute(()->detailsDao.update(details));
    }
}
