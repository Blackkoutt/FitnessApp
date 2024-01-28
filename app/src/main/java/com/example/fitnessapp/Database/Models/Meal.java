package com.example.fitnessapp.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(tableName = "meal")
public class Meal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long mealId;

    public Meal(long mealCategoryId, LocalDate date, float totalCalorific, float totalProtein, float totalCarbohydrates, float totalFat){
        this.mealCategoryId = mealCategoryId;
        this.date = date;
        this.totalCalorific = totalCalorific;
        this.totalCarbohydrates = totalCarbohydrates;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
    }
    @ColumnInfo(name = "totalCalorific", defaultValue = "0.0")
    private float totalCalorific;
    @ColumnInfo(name = "totalProtein", defaultValue = "0.0")
    private float totalProtein;
    @ColumnInfo(name = "totalCarbohydrates", defaultValue = "0.0")
    private float totalCarbohydrates;
    @ColumnInfo(name = "totalFat", defaultValue = "0.0")
    private float totalFat;
    private long mealCategoryId;
    private LocalDate date;
    public float getTotalCalorific(){return this.totalCalorific; }
    public void setTotalCalorific(float totalCalorific){this.totalCalorific = totalCalorific; }
    public float getTotalProtein(){return this.totalProtein; }
    public void setTotalProtein(float totalProtein){this.totalProtein = totalProtein; }
    public float getTotalCarbohydrates(){return this.totalCarbohydrates; }
    public void setTotalCarbohydrates(float totalCarbohydrates){this.totalCarbohydrates = totalCarbohydrates; }
    public float getTotalFat(){return this.totalFat; }
    public void setTotalFat(float totalFat){ this.totalFat = totalFat; }
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
