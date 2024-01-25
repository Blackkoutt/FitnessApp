package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "measureUnit")
public class MeasureUnit implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long measureUnitId;
    private String name;
    public MeasureUnit(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public long getMeasureUnitId(){
        return measureUnitId;
    }
    public void setMeasureUnitId(long id){
        this.measureUnitId=id;
    }
}
