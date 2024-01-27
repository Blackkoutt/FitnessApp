package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MealCategory;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ViewModels.ManufacturerViewModel;
import com.example.fitnessapp.Database.ViewModels.MealCategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductCategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductDetailsViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;
import com.example.fitnessapp.ui.products.ProductsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddEditMealActivity extends AppCompatActivity {
    private AutoCompleteTextView selectMealCategory;
    private TextInputEditText searchEditText;
    public static final String EXTRA_MEAL = "EXTRA_MEAL";
    public static final String EXTRA_EDIT_CATEGORY_ID = "EXTRA_EDIT_CATEGORY_ID";
    public static final String EXTRA_EDIT_PRODUCTS_IDS = "EXTRA_EDIT_PRODUCTS_IDS";
    public static final String EXTRA_EDIT_TOTAL_CALORIES = "EXTRA_EDIT_TOTAL_CALORIES ";
    public static final String EXTRA_EDIT_TOTAL_PROTEINS = "EXTRA_EDIT_TOTAL_PROTEINS";
    public static final String EXTRA_EDIT_TOTAL_CARBOHYDRATES = "EXTRA_EDIT_TOTAL_CARBOHYDRATES";
    public static final String EXTRA_EDIT_TOTAL_FATS = "EXTRA_EDIT_TOTAL_FATS";

    private TextView headerTextView;
    private MealCategoryViewModel mealCategoryViewModel;
    private List <MealCategory> mealCategoryList;
    private RecyclerView productRecyclerView;
    private ProductAdapter adapter;
    private String selectedCategory;
    private ProductViewModel productViewModel;
    private List<ProductWithRelations> selectedProducts;
    private FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_meal);

        selectedProducts = new ArrayList<>();

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::saveMeal);

        selectMealCategory = findViewById(R.id.select_meal_category);
        searchEditText = findViewById(R.id.edit_text_search_product);
        headerTextView = findViewById(R.id.header_edit_add);

        // Ustawienie produktów dla listy recycler View
        productRecyclerView = findViewById(R.id.recycler_view_search_results);
        adapter = new ProductAdapter();
        productRecyclerView.setAdapter(adapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAll().observe(this, adapter::setProducts);



        // Pobranie kategori i dodanie do listy select
        mealCategoryViewModel = new ViewModelProvider(this).get(MealCategoryViewModel.class);
        mealCategoryViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                mealCategoryList = categories;
                List<String> categoriesNames = new ArrayList<>();
                for (MealCategory category : categories) {
                    categoriesNames.add(category.getName());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.select_item, categoriesNames);
                selectMealCategory.setAdapter(categoryAdapter);
            }
        });
        selectMealCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter your data based on the entered text
                adapter.filterProducts(charSequence);
                //adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed in this example
            }
        });
        headerTextView.setText(R.string.add_meal);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            headerTextView.setText(R.string.edit_meal);
            MealWithRelations givenMeal = (MealWithRelations) extras.getSerializable(EXTRA_MEAL);
            selectMealCategory.setText(givenMeal.mealCategory.getName());
            selectedCategory = givenMeal.mealCategory.getName();

            productViewModel.getAll().observe(this, new Observer<List<ProductWithRelations>>() {
                @Override
                public void onChanged(List<ProductWithRelations> allProducts) {
                    for(ProductWithRelations productWR : allProducts){
                        for(Product product : givenMeal.products){
                            if(productWR.product.getProductId() ==  product.getProductId()){
                                selectedProducts.add(productWR);
                            }
                        }

                    }
                }
            });

        }

    }

    private void saveMeal(View view) {
        MealCategory category = mealCategoryList.stream().filter(cat -> cat.getName().equals(selectedCategory))
                .findFirst().orElse(null);
        if(IsFormValid(category)) {
            float totalCalories = calculateNutritionalValues("calories");
            float totalProteins = calculateNutritionalValues("proteins");
            float totalCarbohydrates = calculateNutritionalValues("carbohydrates");
            float totalFat = calculateNutritionalValues("fat");

            long[] productsIds = new long [selectedProducts.size()];

            for(int i=0;i<selectedProducts.size();i++){
                productsIds[i] = selectedProducts.get(i).product.getProductId();
            }
            Intent replyIntent = new Intent();

            replyIntent.putExtra(EXTRA_EDIT_CATEGORY_ID, String.valueOf(category.getMealCategoryId()));
            replyIntent.putExtra(EXTRA_EDIT_PRODUCTS_IDS, productsIds);
            replyIntent.putExtra(EXTRA_EDIT_TOTAL_CALORIES, String.valueOf(totalCalories));
            replyIntent.putExtra(EXTRA_EDIT_TOTAL_PROTEINS, String.valueOf(totalProteins));
            replyIntent.putExtra(EXTRA_EDIT_TOTAL_CARBOHYDRATES, String.valueOf(totalCarbohydrates));
            replyIntent.putExtra(EXTRA_EDIT_TOTAL_FATS, String.valueOf(totalFat));
            setResult(RESULT_OK, replyIntent);
            finish();

        }
    }
    private boolean IsFormValid(MealCategory category){
        if(category==null){
            FocusListener(R.id.text_input_meal_category, selectMealCategory);
            return false;
        }
        if(selectedProducts.size() == 0){
            Snackbar.make(findViewById(R.id.container), getString(R.string.product_list_empty),
                    BaseTransientBottomBar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void FocusListener(int id, TextView textView){
        TextInputLayout textInputLayout = findViewById(id);
        textInputLayout.setHelperText(getString(R.string.required_error));
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(TextUtils.isEmpty(textView.getText())){
                        textInputLayout.setHelperText(getString(R.string.required_error));
                    }
                    else{
                        textInputLayout.setHelperText("");
                    }
                }
            }
        });
    }

    private float calculateNutritionalValues(String wichValue) {
        float value = 0;
        for(ProductWithRelations product : selectedProducts){
            switch(wichValue){
                case "calories":{
                    value += product.details.getCalorificValue();
                    break;
                }
                case "proteins":{
                    value += product.details.getProtein();
                    break;
                }
                case "carbohydrates":{
                    value += product.details.getCarbohydrates();
                    break;
                }
                case "fat":{
                    value += product.details.getFat();
                    break;
                }
            }
        }
        return value;
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private ProductWithRelations product;
        private CardView cardView;
        private TextView categoryTextView;
        //private TextView unitTextView;
        private TextView calorificValueTextView;
        private TextView productManufacturerTextView;
        private TextView productNameTextView;
        public ProductHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.product_list_item, parent, false));

            cardView = itemView.findViewById(R.id.card_view);
            productManufacturerTextView = itemView.findViewById(R.id.product_manufacturer);
            productNameTextView = itemView.findViewById(R.id.product_name);
            categoryTextView = itemView.findViewById(R.id.product_category);
            calorificValueTextView = itemView.findViewById(R.id.product_calorificValue);

            itemView.setOnClickListener(this::AddToList);
            // moze lepiej bedzie onlong click
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        intent.putExtra("EXTRA_PRODUCT_DETAILS", product);
                        startActivity(intent);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Bez tego nie obsługiwany jest event click i long click
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    return false;
                }
            });
        }

        private void AddToList(View view) {
            if(!selectedProducts.contains(product)){
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                selectedProducts.add(product);
            }
            else{
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                selectedProducts.remove(product);
            }
        }

        public void bind(ProductWithRelations product){
            this.product = product;
            cardView.setVisibility(View.VISIBLE);
            if(selectedProducts.contains(product)){
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
            else{
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }

            productNameTextView.setText(product.product.getName());
            String categoryNameText ="";
            for(int i=0;i<product.categories.size();i++){
                categoryNameText+=product.categories.get(i).getName();
                if(i!=product.categories.size()-1){
                    categoryNameText+=", ";
                }
            }
            categoryTextView.setText(getResources().getString(R.string.product_category_label, categoryNameText));
            productManufacturerTextView.setText(getResources().getString(R.string.product_manufacturer_label, product.manufacturer.getName()));
            calorificValueTextView.setText(getResources().getString(R.string.product_calorific_label, String.valueOf(product.details.getCalorificValue())));
        }
        public void clear(){
            cardView.setVisibility(View.GONE);
            productNameTextView.setText("");
            categoryTextView.setText("");
            productManufacturerTextView.setText("");
            calorificValueTextView.setText("");
        }
    }
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder>{
        private List<ProductWithRelations> originalProducts;
        private List<ProductWithRelations> filteredProducts;
        private List<Manufacturer> mans;

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            if(filteredProducts != null && filteredProducts.size()!=0){
                holder.clear();
                if(position<filteredProducts.size()) {
                    ProductWithRelations product = filteredProducts.get(position);
                    holder.bind(product);
                }
            }
            else {
                holder.clear();
                Log.d("MainActivity", "No products");
            }
        }

        @Override
        public int getItemCount() {
            if(originalProducts != null){
                return originalProducts.size();
            }
            else{
                return 0;
            }
        }
        void filterProducts(CharSequence sequence){
            filteredProducts.clear();
            for(ProductWithRelations product: originalProducts){
                CharSequence chara = sequence.toString().toLowerCase();
                int a=1;
                if(product.product.getName().toLowerCase().contains(sequence.toString().toLowerCase())){
                    filteredProducts.add(product);
                }
            }
            notifyDataSetChanged();
        }
        void setProducts(List<ProductWithRelations> products){
            this.originalProducts = new ArrayList<>(products);
            this.filteredProducts = new ArrayList<>(products);
            notifyDataSetChanged();
        }
    }
}