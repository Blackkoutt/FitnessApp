package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.MeasureUnitDao;
import com.example.fitnessapp.Database.Dao.ProductCategoryDao;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class ProductCategoryRepository {
    private final ProductCategoryDao productCategoryDao;

    public ProductCategoryRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        productCategoryDao = database.productCategoryDao();
    }
    public void insert(ProductCategory productCategory) {ProductDatabase.databaseWriteExecutor.execute(()->productCategoryDao.insert(productCategory));}
    public void delete(long id){
        ProductDatabase.databaseWriteExecutor.execute(()->productCategoryDao.deleteByProductId(id));
    }
}

