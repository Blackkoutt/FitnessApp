package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.CategoryDao;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> categories;

    public CategoryRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        categoryDao = database.categoryDao();
        categories = categoryDao.getAll();
    }
    public LiveData<List<Category>> getAllCategories(){return categories; }
    public void insert(Category category) {ProductDatabase.databaseWriteExecutor.execute(()->categoryDao.insert(category));}
}
