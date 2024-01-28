package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Repositories.MealRepository;

import java.time.LocalDate;
import java.util.List;

public class MealViewModel extends AndroidViewModel {
    private final MealRepository mealRepository;
    private final LiveData<List<MealWithRelations>> meals;
    public MealViewModel(@NonNull Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        meals = mealRepository.getAllMeals();
    }
    public LiveData<List<MealWithRelations>> getAll() {return meals;}
    public LiveData<List<MealWithRelations>> getAllByDate(LocalDate date) {return mealRepository.getAllMealsByDate(date);}

    public long insert (Meal meal){
        return mealRepository.insert(meal);
    }

    public void update(Meal meal){
        mealRepository.update(meal);
    }

    public void delete(Meal meal){
        mealRepository.delete(meal);
    }
}
