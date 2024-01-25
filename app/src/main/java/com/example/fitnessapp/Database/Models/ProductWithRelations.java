package com.example.fitnessapp.Database.Models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ProductWithRelations implements Serializable {
    @Embedded
    public Product product;


    // Relacja N:N
   @Relation(
            parentColumn = "productId",
            entityColumn = "categoryId",
            associateBy = @Junction(ProductCategory.class)
    )
    public List<Category> categories;

    // Relacja 1:1
    @Relation(
            parentColumn = "productId",
            entityColumn = "productDetailsId"
    )
    public ProductDetails details;

    // Relacja 1:N
    @Relation(
            parentColumn = "measureUnitId",
            entityColumn = "measureUnitId"
    )
    public MeasureUnit unit;

    // Relacja 1:N
    @Relation(
            parentColumn = "manufacturerId",
            entityColumn = "manufacturerId"
    )
    public Manufacturer manufacturer;
}
