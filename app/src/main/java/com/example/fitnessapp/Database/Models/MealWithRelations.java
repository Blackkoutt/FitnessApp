package com.example.fitnessapp.Database.Models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class MealWithRelations implements Serializable {
    @Embedded
    public Meal meal;

    // Relacja 1:N
    @Relation(
            parentColumn = "mealCategoryId",
            entityColumn = "mealCategoryId"
    )
    public MealCategory mealCategory;
    @Relation(
            parentColumn = "mealId",
            entityColumn = "productId",
            associateBy = @Junction(MealProduct.class)
    )
    public List<Product> products;

}
