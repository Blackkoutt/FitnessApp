package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Repositories.ProductCategoryRepository;

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
