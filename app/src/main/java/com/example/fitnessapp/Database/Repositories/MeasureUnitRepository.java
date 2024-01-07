package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.CategoryDao;
import com.example.fitnessapp.Database.Dao.MeasureUnitDao;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class MeasureUnitRepository {
    private final MeasureUnitDao measureUnitDao;
    private final LiveData<List<MeasureUnit>> units;

    public MeasureUnitRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        measureUnitDao = database.measureUnitDao();
        units = measureUnitDao.getAll();
    }
    public LiveData<List<MeasureUnit>> getAllUnits(){return units; }
}
