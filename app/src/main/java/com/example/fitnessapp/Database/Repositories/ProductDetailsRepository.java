package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.MeasureUnitDao;
import com.example.fitnessapp.Database.Dao.ProductDetailsDao;
import com.example.fitnessapp.Database.Models.MeasureUnit;
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
}
