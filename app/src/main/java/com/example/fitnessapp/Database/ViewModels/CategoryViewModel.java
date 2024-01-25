package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Repositories.CategoryRepository;
import com.example.fitnessapp.Database.Repositories.MeasureUnitRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository categoryRepository;
    private final LiveData<List<Category>> categories;

    public CategoryViewModel (@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        categories = categoryRepository.getAllCategories();
    }
    public LiveData<List<Category>> getAll() {return categories;}
    public void insert (Category category){
        categoryRepository.insert(category);
    }
}
