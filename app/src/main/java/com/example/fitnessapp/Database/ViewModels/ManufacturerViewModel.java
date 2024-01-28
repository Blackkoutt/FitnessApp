package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Repositories.ManufacturerRepository;

import java.util.List;

public class ManufacturerViewModel extends AndroidViewModel {
    private final ManufacturerRepository manufacturerRepository;
    private final LiveData<List<Manufacturer>> manufacturer;

    public ManufacturerViewModel(@NonNull Application application) {
        super(application);
        manufacturerRepository = new ManufacturerRepository(application);
        manufacturer = manufacturerRepository.getAllManufacturers();
    }
    public LiveData<List<Manufacturer>> getAll() {return manufacturer;}

    public void insert (Manufacturer manufacturer){
        manufacturerRepository.insert(manufacturer);
    }
}
