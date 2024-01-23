package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.Repositories.MeasureUnitRepository;
import com.example.fitnessapp.Database.Repositories.ProductDetailsRepository;

import java.util.List;

public class ProductDetailsViewModel extends AndroidViewModel {
    private final ProductDetailsRepository detailsRepository;
    private final LiveData<List<ProductDetails>> details;

    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        detailsRepository = new ProductDetailsRepository(application);
        details = detailsRepository.getAllDetails();
    }
    public LiveData<List<ProductDetails>> getAll() {return details;}
    public void insert (ProductDetails _productDetails){
        detailsRepository.insert(_productDetails);
    }
    public void delete(ProductDetails details){
        detailsRepository.delete(details);
    }

    public void update(ProductDetails details){
        detailsRepository.update(details);
    }

}
