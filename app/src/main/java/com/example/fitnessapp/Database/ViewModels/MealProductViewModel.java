package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnessapp.Database.Models.MealProduct;
import com.example.fitnessapp.Database.Repositories.MealProductRepository;

public class MealProductViewModel extends AndroidViewModel {
    private final MealProductRepository mealProductRepository;

    public MealProductViewModel(@NonNull Application application) {
        super(application);
        mealProductRepository = new MealProductRepository(application);
    }

    public void insert (MealProduct mealProduct){
        mealProductRepository.insert(mealProduct);
    }
    public void deleteByMealId(long mealId){
        mealProductRepository.deleteByMealId(mealId);
    }
    public void deleteByProductId(long productId){
        mealProductRepository.deleteByProductId(productId);
    }
}
