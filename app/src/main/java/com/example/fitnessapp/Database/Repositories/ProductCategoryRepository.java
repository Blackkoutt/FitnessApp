package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import com.example.fitnessapp.Database.Dao.ProductCategoryDao;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.ProductDatabase;

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

