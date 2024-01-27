package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"productId", "categoryId"},  indices = {@Index("categoryId")})
public class ProductCategory {
    private long productId;
    private long categoryId;
    public ProductCategory(long productId, long categoryId){
        this.productId = productId;
        this.categoryId = categoryId;
    }
    public long getProductId(){
        return productId;
    }
    public void setProductId(long id){
        this.productId=id;
    }
    public long getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(long id){
        this.categoryId=id;
    }
}
