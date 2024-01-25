package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fitnessapp.Database.Models.ProductWithRelations;

public class ProductDetailsActivity extends AppCompatActivity {

    private ProductWithRelations product;
    private TextView productNameTextView;
    private TextView productManufacturerTextView;
    private TextView productCategoryTextView;
    private TextView productUnitTextView;
    private TextView productCaloriesTextView;
    private TextView productProteinTextView;
    private TextView productCarbohydratesTextView;
    private TextView productFatTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productNameTextView = findViewById(R.id.product_name);
        productManufacturerTextView = findViewById(R.id.product_manufacturer);
        productCategoryTextView = findViewById(R.id.product_category);
        productUnitTextView = findViewById(R.id.product_unit);
        productCaloriesTextView = findViewById(R.id.product_calorificValue);
        productProteinTextView = findViewById(R.id.product_proteinValue);
        productCarbohydratesTextView = findViewById(R.id.product_carbohydratesValue);
        productFatTextView = findViewById(R.id.product_fatValue);

        Intent intent = getIntent();
        if (intent.hasExtra("EXTRA_PRODUCT_DETAILS")) {
            product = (ProductWithRelations) intent.getSerializableExtra("EXTRA_PRODUCT_DETAILS");
        }

        productNameTextView.setText(getResources().getString(R.string.product_name_label, product.product.getName()));
        productManufacturerTextView.setText(getResources().getString(R.string.product_manufacturer_label, product.manufacturer.getName()));

        String categoryNameText ="";
        for(int i=0;i<product.categories.size();i++){
            categoryNameText+=product.categories.get(i).getName();
            if(i!=product.categories.size()-1){
                categoryNameText+=", ";
            }
        }
        productCategoryTextView.setText(getResources().getString(R.string.product_category_label, categoryNameText));
        productUnitTextView.setText(getResources().getString(R.string.product_unit_label, product.unit.getName()));
        productCaloriesTextView.setText(getResources().getString(R.string.product_calorific_label, String.valueOf(product.details.getCalorificValue())));
        productProteinTextView.setText(getResources().getString(R.string.product_protein_label, String.valueOf(product.details.getProtein())));
        productCarbohydratesTextView.setText(getResources().getString(R.string.product_carbohydrates_label, String.valueOf(product.details.getCarbohydrates())));
        productFatTextView.setText(getResources().getString(R.string.product_fat_label, String.valueOf(product.details.getFat())));
    }
}