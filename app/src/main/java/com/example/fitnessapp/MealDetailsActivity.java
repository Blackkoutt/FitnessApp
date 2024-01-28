package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsActivity extends AppCompatActivity {
    private MealWithRelations meal;
    private RecyclerView productRecyclerView;
    private TextView mealCategory;
    private TextView mealDate;
    private TextView mealCalories;
    private TextView mealProteins;
    private TextView mealCarbohydrates;
    private TextView mealFats;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        // Pobranie elementów widoku
        mealDate = findViewById(R.id.meal_date);
        mealCategory = findViewById(R.id.meal_category);
        mealCalories = findViewById(R.id.total_calories);
        mealProteins = findViewById(R.id.total_proteins);
        mealCarbohydrates = findViewById(R.id.total_carbohydrates);
        mealFats = findViewById(R.id.total_fats);
        productRecyclerView = findViewById(R.id.product_recycler_view);

        // Przypisanie adaptera produktów
        final ProductAdapter adapter = new ProductAdapter();
        productRecyclerView.setAdapter(adapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pobranie wartości przekazanych do Activity
        Intent intent = getIntent();
        if (intent.hasExtra("EXTRA_MEAL_DETAILS")) {
            meal = (MealWithRelations) intent.getSerializableExtra("EXTRA_MEAL_DETAILS");
        }
        mealDate.setText(getResources().getString(R.string.meal_date, meal.meal.getDate().toString()));
        mealCategory.setText(getResources().getString(R.string.meal_category_name, meal.mealCategory.getName()));
        mealCalories.setText(getResources().getString(R.string.total_calorific, String.valueOf(meal.meal.getTotalCalorific())));
        mealProteins.setText(getResources().getString(R.string.total_protein, String.valueOf(meal.meal.getTotalProtein())));
        mealCarbohydrates.setText(getResources().getString(R.string.total_carbohydrates, String.valueOf(meal.meal.getTotalCarbohydrates())));
        mealFats.setText(getResources().getString(R.string.total_fat, String.valueOf(meal.meal.getTotalFat())));

        // Pobranie listy produktów z bazy
        List<ProductWithRelations> products = new ArrayList<>();
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAll().observe(this, new Observer<List<ProductWithRelations>>() {
            @Override
            public void onChanged(List<ProductWithRelations> allProducts) {
                for(ProductWithRelations productWR : allProducts){
                    for(Product product : meal.products){
                        if(productWR.product.getProductId() ==  product.getProductId()){
                            products.add(productWR);
                        }
                    }

                }
                adapter.setProducts(products);
            }
        });
    }

    // Holder produktu
    private class ProductHolder extends RecyclerView.ViewHolder {

        private ProductWithRelations product;
        private TextView productNameTextView;
        private TextView productManufacturerTextView;
        private TextView calorificValueTextView;
        private TextView proteinValueTextView;
        private TextView carbohydratesValueTextView;
        private TextView fatsValueTextView;

        public ProductHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.meal_details_product_list_item, parent, false));

            // Pobranie elementów widoku
            productNameTextView = itemView.findViewById(R.id.product_name);
            productManufacturerTextView = itemView.findViewById(R.id.product_manufacturer);
            calorificValueTextView = itemView.findViewById(R.id.product_calorificValue);
            proteinValueTextView = itemView.findViewById(R.id.product_proteinValue);
            carbohydratesValueTextView = itemView.findViewById(R.id.product_carbohydratesValue);
            fatsValueTextView = itemView.findViewById(R.id.product_fatsValue);
        }

        // Bindowanie produktu
        public void bind(ProductWithRelations product){
            this.product = product;
            productNameTextView.setText(getResources().getString(R.string.product_name_label, product.product.getName()));
            productManufacturerTextView.setText(getResources().getString(R.string.product_manufacturer_label, product.manufacturer.getName()));
            calorificValueTextView.setText(getResources().getString(R.string.product_calorific_label, String.valueOf(product.details.getCalorificValue())));
            proteinValueTextView.setText(getResources().getString(R.string.product_protein_label, String.valueOf(product.details.getProtein())));
            carbohydratesValueTextView.setText(getResources().getString(R.string.product_carbohydrates_label, String.valueOf(product.details.getCarbohydrates())));
            fatsValueTextView.setText(getResources().getString(R.string.product_fat_label, String.valueOf(product.details.getFat())));
        }
    }

    // Adapter produktu
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder>{
        private List<ProductWithRelations> products;

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            if(products != null){
                ProductWithRelations product = products.get(position);
                holder.bind(product);
            }
            else {
                Log.d("MainActivity", "No products");
            }
        }

        @Override
        public int getItemCount() {
            if(products != null){
                return products.size();
            }
            else{
                return 0;
            }
        }
        void setProducts(List<ProductWithRelations> products){
            this.products = products;
            notifyDataSetChanged();
        }
    }
}