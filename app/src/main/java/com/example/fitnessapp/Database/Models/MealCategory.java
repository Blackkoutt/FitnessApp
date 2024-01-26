package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "meal_category")
public class MealCategory {
    @PrimaryKey(autoGenerate = true)
    private long mealCategoryId;
    private String name;
    public MealCategory (String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }
    public void setName(){
        this.name = name;
    }
    public long getMealCategoryId(){
        return mealCategoryId;
    }
    public void setMealCategoryId(long id){
        this.mealCategoryId=id;
    }
}


