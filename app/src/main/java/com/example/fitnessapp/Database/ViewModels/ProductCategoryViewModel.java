package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.Repositories.ProductCategoryRepository;
import com.example.fitnessapp.Database.Repositories.ProductDetailsRepository;

import java.util.List;

public class ProductCategoryViewModel extends AndroidViewModel {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryViewModel(@NonNull Application application) {
        super(application);
        productCategoryRepository = new ProductCategoryRepository(application);
    }
    public void insert (ProductCategory productCategory){
        productCategoryRepository.insert(productCategory);
    }
    public void delete(long id){
        productCategoryRepository.delete(id);
    }
}
