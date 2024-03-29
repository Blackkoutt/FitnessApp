package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "manufacturer")
public class Manufacturer implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long manufacturerId;
    private String name;
    public Manufacturer(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public long getManufacturerId(){
        return manufacturerId;
    }
    public void setManufacturerId(long id){
        this.manufacturerId=id;
    }
}
