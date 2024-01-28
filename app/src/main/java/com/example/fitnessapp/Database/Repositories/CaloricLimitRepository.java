package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.CaloricLimitDao;
import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.ProductDatabase;

import java.time.LocalDate;

public class CaloricLimitRepository {
    private final CaloricLimitDao caloricLimitDao;
    public CaloricLimitRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        caloricLimitDao = database.caloricLimitDao();
    }
    public void insert(CaloricLimit limit) {ProductDatabase.databaseWriteExecutor.execute(()->caloricLimitDao.insert(limit));}
    public LiveData<CaloricLimit> getLimitByDate(LocalDate date){return caloricLimitDao.getByDate(date); }

    public void update(CaloricLimit limit){
        ProductDatabase.databaseWriteExecutor.execute(()->caloricLimitDao.update(limit));
    }
}
