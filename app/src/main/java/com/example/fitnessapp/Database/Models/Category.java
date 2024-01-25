package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "category")
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long categoryId;
    private String name;
    public Category (String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public long getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(long id){
        this.categoryId=id;
    }
}
