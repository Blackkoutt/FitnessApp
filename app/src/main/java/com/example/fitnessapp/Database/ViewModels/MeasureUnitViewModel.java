package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Repositories.ManufacturerRepository;
import com.example.fitnessapp.Database.Repositories.MeasureUnitRepository;

import java.util.List;

public class MeasureUnitViewModel extends AndroidViewModel {
    private final MeasureUnitRepository unitRepository;
    private final LiveData<List<MeasureUnit>> units;

    public MeasureUnitViewModel(@NonNull Application application) {
        super(application);
        unitRepository = new MeasureUnitRepository(application);
        units = unitRepository.getAllUnits();
    }
    public LiveData<List<MeasureUnit>> getAll() {return units;}
}
