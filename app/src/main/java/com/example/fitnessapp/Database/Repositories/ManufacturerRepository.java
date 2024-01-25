package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.ManufacturerDao;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;

public class ManufacturerRepository {
    private final ManufacturerDao manufacturerDao;
    private final LiveData<List<Manufacturer>> manufacturers;

    public ManufacturerRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        manufacturerDao = database.manufacturerDao();
        manufacturers = manufacturerDao.getAll();
    }
    public LiveData<List<Manufacturer>> getAllManufacturers(){return manufacturers; }
    public void insert(Manufacturer manufacturer) {ProductDatabase.databaseWriteExecutor.execute(()->manufacturerDao.insert(manufacturer));}
}
