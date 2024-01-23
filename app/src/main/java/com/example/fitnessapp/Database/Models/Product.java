package com.example.fitnessapp.Database.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

//@Entity(tableName = "product")
@Entity(tableName = "product"/*, foreignKeys = {
        @ForeignKey(
                entity = Manufacturer.class,
                parentColumns = "manufacturerId",
                childColumns = "manufacturerId",
                onDelete = ForeignKey.CASCADE
        )
}*/)
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String name;
    public Product(String name, long manufacturerId, long measureUnitId){

        this.manufacturerId = manufacturerId;
        this.measureUnitId = measureUnitId;
        this.name = name;
    }
    private long manufacturerId;
    private long measureUnitId;


    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public long getProductId(){
        return productId;
    }
    public void setProductId(long id){
        this.productId=id;
    }
    public long getManufacturerId(){
        return manufacturerId;
    }
    public void setManufacturerId(long id){
        this.manufacturerId=id;
    }

    public long getMeasureUnitId(){
        return measureUnitId;
    }
    public void setMeasureUnitId(long id){
        this.measureUnitId=id;
    }
    // manufacturer
    // unit of measure
    // details
    // category
}
