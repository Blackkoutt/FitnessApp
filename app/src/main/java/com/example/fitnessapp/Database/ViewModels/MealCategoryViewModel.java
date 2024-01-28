package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.MealCategory;
import com.example.fitnessapp.Database.Repositories.MealCategoryRepository;

import java.util.List;

public class MealCategoryViewModel extends AndroidViewModel {
    private final MealCategoryRepository mealCategoryRepository;
    private final LiveData<List<MealCategory>> mealCategory;
    public MealCategoryViewModel(@NonNull Application application) {
        super(application);
        mealCategoryRepository = new MealCategoryRepository(application);
        mealCategory = mealCategoryRepository.getAllCategories();
    }
    public LiveData<List<MealCategory>> getAllCategories(){return mealCategory; }
}
