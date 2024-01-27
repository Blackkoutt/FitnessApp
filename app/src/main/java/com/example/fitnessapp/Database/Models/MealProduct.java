package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "MealProduct")
public class MealProduct implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long mealProductId;
    private long mealId;
    private long productId;
    public MealProduct(long mealId, long productId){
        this.mealId = mealId;
        this.productId = productId;
    }
    public long getMealProductId(){
        return this.mealProductId;
    }
    public void setMealProductId(long mealProductId){
        this.mealProductId = mealProductId;
    }
    public long getMealId(){
        return mealId;
    }
    public void setMealId(long mealId){
        this.mealId=mealId;
    }
    public long getProductId(){
        return productId;
    }
    public void setProductId(long productId){
        this.productId=productId;
    }
}
