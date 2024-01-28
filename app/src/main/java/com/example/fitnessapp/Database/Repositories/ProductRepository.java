package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ProductDatabase;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<ProductWithRelations>> products;

    public ProductRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        productDao = database.productDao();
        products = productDao.getAll();
    }
    public LiveData<List<ProductWithRelations>> getAllProducts(){return products; }

    public long insert(Product product)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        AtomicLong insertedId = new AtomicLong();
        ProductDatabase.databaseWriteExecutor.execute(()->{
            long id = productDao.insert(product);
            insertedId.set(id);
            latch.countDown();
        });
        try {
            latch.await();  // Czekaj, aż operacja się zakończy
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long new_id = insertedId.get();
        return insertedId.get();
    }

    public void update(Product product){
        ProductDatabase.databaseWriteExecutor.execute(()->productDao.update(product));
    }

    public void delete(Product product){
        ProductDatabase.databaseWriteExecutor.execute(()->productDao.delete(product));
    }
}
