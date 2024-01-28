package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(tableName = "caloric_limit")
public class CaloricLimit implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long limitId;
    public CaloricLimit(float caloricLimit, LocalDate date){
        this.caloricLimit = caloricLimit;
        this.date = date;
    }
    private float caloricLimit;
    private LocalDate date;
    public long getLimitId(){
        return this.limitId;
    }
    public void setLimitId(long limitId){
        this.limitId = limitId;
    }
    public float getCaloricLimit(){
        return this.caloricLimit;
    }
    public void setCaloricLimit(float caloricLimit){
        this.caloricLimit = caloricLimit;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }
}
