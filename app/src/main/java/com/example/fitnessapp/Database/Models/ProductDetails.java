package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "productDetails")
public class ProductDetails implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private long productDetailsId;
    public ProductDetails(float calorificValue,
                          float carbohydrates,
                          float fat,
                          float protein, long productDetailsId){
        this.calorificValue = calorificValue;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.productDetailsId = productDetailsId;
    }
    public long getProductDetailsId(){
        return productDetailsId;
    }
    public void setProductDetailsId(long id){
        this.productDetailsId=id;
    }
    private float calorificValue;
    public float getCalorificValue(){return calorificValue;}
    public void setCalorificValue(float value){
        this.calorificValue=value;
    }
    private float carbohydrates;
    public float getCarbohydrates(){return carbohydrates;}
    public void setCarbohydrates(float value){
        this.carbohydrates=value;
    }
    private float fat;
    public float getFat(){return fat;}
    public void setFat(float value){
        this.fat=value;
    }
    private float protein;
    public float getProtein(){return protein;}
    public void setProtein(float value){
        this.protein=value;
    }
}
