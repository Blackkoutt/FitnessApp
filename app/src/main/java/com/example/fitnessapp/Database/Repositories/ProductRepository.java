package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<ProductWithRelations>> products;

    public ProductRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        productDao = database.productDao();
       // database.manufacturerDao().getAll();
       // database.measureUnitDao().getAll();
        products = productDao.getAll();
    }
    public LiveData<List<ProductWithRelations>> getAllProducts(){return products; }
}
