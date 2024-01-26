package com.example.fitnessapp.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnessapp.Database.Dao.MealDao;
import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ProductDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepository {
    private final MealDao mealDao;
    private final LiveData<List<MealWithRelations>> meals;
    public MealRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        mealDao = database.mealDao();
        meals = mealDao.getAll();
    }
    public LiveData<List<MealWithRelations>> getAllMeals(){return meals; }
    public LiveData<List<MealWithRelations>> getAllMealsByDate(LocalDate date){return mealDao.getAllByDate(date); }

    public long insert(Meal meal)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        AtomicLong insertedId = new AtomicLong();
        ProductDatabase.databaseWriteExecutor.execute(()->{
            long id = mealDao.insert(meal);
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

    public void update(Meal meal){
        ProductDatabase.databaseWriteExecutor.execute(()->mealDao.update(meal));
    }
    public void delete(Meal meal){
        ProductDatabase.databaseWriteExecutor.execute(()->mealDao.delete(meal));
    }
}
