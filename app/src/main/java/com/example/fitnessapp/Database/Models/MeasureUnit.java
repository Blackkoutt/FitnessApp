package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "measureUnit")
public class MeasureUnit {
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
