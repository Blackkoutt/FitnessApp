package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "meal")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    private long mealId;

    public Meal(long mealCategoryId, LocalDate date){
        this.mealCategoryId = mealCategoryId;
        this.date = date;
    }
    private long mealCategoryId;
    private LocalDate date;

    public long getMealId(){
        return this.mealId;
    }
    public void setMealId(long mealId){
        this.mealId = mealId;
    }

    public long getMealCategoryId(){
        return mealCategoryId;
    }
    public void setMealCategoryId(long mealCategoryId){
        this.mealCategoryId=mealCategoryId;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }
}
