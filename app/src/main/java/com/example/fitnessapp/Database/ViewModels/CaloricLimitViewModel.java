package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Repositories.CaloricLimitRepository;
import com.example.fitnessapp.Database.Repositories.MealRepository;

import java.time.LocalDate;
import java.util.List;

public class CaloricLimitViewModel extends AndroidViewModel {
    private final CaloricLimitRepository caloricLimitRepository;
    public CaloricLimitViewModel(@NonNull Application application) {
        super(application);
        caloricLimitRepository = new CaloricLimitRepository(application);
    }
   public void insert (CaloricLimit limit){ caloricLimitRepository.insert(limit); }
    public LiveData<CaloricLimit> getLimitByDate(LocalDate date) {return caloricLimitRepository.getLimitByDate(date);}
    public void update(CaloricLimit limit){
        caloricLimitRepository.update(limit);
    }
}
