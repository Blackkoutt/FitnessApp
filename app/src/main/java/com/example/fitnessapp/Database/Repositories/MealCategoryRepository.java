package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.MealCategoryDao;
import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Models.MealCategory;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class MealCategoryRepository {
    private final MealCategoryDao mealCategoryDao;
    private final LiveData<List<MealCategory>> mealCategory;

    public MealCategoryRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        mealCategoryDao = database.mealCategoryDao();
        mealCategory = mealCategoryDao.getAll();
    }
    public LiveData<List<MealCategory>> getAllCategories(){return mealCategory; }
}
