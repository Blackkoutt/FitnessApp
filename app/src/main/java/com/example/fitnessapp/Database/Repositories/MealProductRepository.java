package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import com.example.fitnessapp.Database.Dao.MealProductDao;
import com.example.fitnessapp.Database.Dao.ProductCategoryDao;
import com.example.fitnessapp.Database.Models.MealProduct;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.ProductDatabase;

public class MealProductRepository {
    private final MealProductDao mealProductDao;

    public MealProductRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        mealProductDao = database.mealProductDao();
    }
    public void insert(MealProduct mealProduct) {ProductDatabase.databaseWriteExecutor.execute(()->mealProductDao.insert(mealProduct));}
    public void deleteByMealId(long mealId){
        ProductDatabase.databaseWriteExecutor.execute(()->mealProductDao.deleteByMealId(mealId));
    }
    public void deleteByProductId(long productId){
        ProductDatabase.databaseWriteExecutor.execute(()->mealProductDao.deleteByProductId(productId));
    }
}
