package com.example.fitnessapp.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.Repositories.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final LiveData<List<ProductWithRelations>> products;
    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        products = productRepository.getAllProducts();
    }
    public LiveData<List<ProductWithRelations>> getAll() {return products;}

    public long insert (Product product){
        return productRepository.insert(product);
    }

    public void update(Product product){
        productRepository.update(product);
    }

    public void delete(Product product){
        productRepository.delete(product);
    }
}
