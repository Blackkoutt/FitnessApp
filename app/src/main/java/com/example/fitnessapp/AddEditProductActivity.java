package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.ViewModels.CategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ManufacturerViewModel;
import com.example.fitnessapp.Database.ViewModels.MeasureUnitViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductDetailsViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;
import com.example.fitnessapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class AddEditProductActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_CATEGORY_ID = "EXTRA_EDIT_CATEGORY_ID";
    public static final String EXTRA_EDIT_CATEGORY_NAME = "EXTRA_EDIT_CATEGORY_NAME";
    public static final String EXTRA_EDIT_MANUFACTURER_ID = "EXTRA_EDIT_MANUFACTURER_ID";
    public static final String EXTRA_EDIT_MANUFACTURER_NAME = "EXTRA_EDIT_MANUFACTURER_NAME";
    public static final String EXTRA_EDIT_UNIT_ID = "EXTRA_EDIT_UNIT_ID ";
    public static final String EXTRA_EDIT_UNIT_NAME = "EXTRA_EDIT_UNIT_NAME";
    public static final String EXTRA_EDIT_PRODUCT_NAME="EXTRA_EDIT_PRODUCT_NAME";
    public static final String EXTRA_EDIT_CALORIES_AMOUNT= "EXTRA_EDIT_CALORIES_AMOUNT";
    public static final String EXTRA_EDIT_PROTEIN_AMOUNT= "EXTRA_EDIT_PROTEIN_AMOUNT";
    public static final String EXTRA_EDIT_CARBOHYDRATES_AMOUNT= "EXTRA_EDIT_CARBOHYDRATES_AMOUNT";
    public static final String EXTRA_EDIT_FAT_AMOUNT= "EXTRA_EDIT_FAT_AMOUNT";

    private AutoCompleteTextView selectManufacturers;
    private AutoCompleteTextView selectCategory;
    private TextInputEditText productName;
    private AutoCompleteTextView selectUnit;
    private TextInputEditText caloriesAmount;
    private TextInputEditText proteinAmount;
    private TextInputEditText carbohydratesAmount;
    private TextInputEditText fatAmount;
    private CategoryViewModel categoryViewModel;
    private ManufacturerViewModel manufacturerViewModel;
    private MeasureUnitViewModel unitViewModel;
    private Button saveButton;
    private List<Manufacturer> manufacturerList;
    private List<Category> categoriesList;
    private List<MeasureUnit> unitsList;
    private String selectedManufacturer;
    private String selectedCategory;
    private String selectedUnit;
    private TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        headerTextView = findViewById(R.id.header_edit_add);

        // Select Producentów
        selectManufacturers = findViewById(R.id.select_manufacturer);
        // Pobranie listy kategorii z bazy
        manufacturerViewModel = new ViewModelProvider(this).get(ManufacturerViewModel.class);
        manufacturerViewModel.getAll().observe(this, manufacturers -> {
            if (manufacturers != null && !manufacturers.isEmpty()) {
                manufacturerList = manufacturers;
                List<String> manufacturerNames = new ArrayList<>();
                for (Manufacturer manufacturer : manufacturers) {
                    manufacturerNames.add(manufacturer.getName());
                }
                ArrayAdapter<String> manufacturerAdapter = new ArrayAdapter<>(this, R.layout.select_item, manufacturerNames);
                selectManufacturers.setAdapter(manufacturerAdapter);
            }
        });
        // Ustawienie onclickListenera
        selectManufacturers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedManufacturer = parent.getItemAtPosition(position).toString();
            }
        });

        // Select Kategorii
        selectCategory = findViewById(R.id.select_category);
        // Pobranie listy kategorii z bazy
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAll().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoriesList = categories;
                List<String> categoryNames = new ArrayList<>();
                for (Category category : categories) {
                    categoryNames.add(category.getName());
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.select_item, categoryNames);
                selectCategory.setAdapter(categoryAdapter);
            }
        });
        // Ustawienie onclickListenera
        selectCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }
        });

        // Input Produktu
        productName = findViewById(R.id.edit_text_product_name);


        // Select Kategorii
        selectUnit = findViewById(R.id.select_unit);
        // Pobranie listy kategorii z bazy
        unitViewModel = new ViewModelProvider(this).get(MeasureUnitViewModel.class);
        unitViewModel.getAll().observe(this, units -> {
            if (units != null && !units.isEmpty()) {
                unitsList = units;
                List<String> unitNames = new ArrayList<>();
                for (MeasureUnit unit : units) {
                    unitNames.add(unit.getName());
                }

                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, R.layout.select_item, unitNames);
                selectUnit.setAdapter(unitAdapter);
            }
        });
        // Ustawienie onclickListenera
        selectUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = parent.getItemAtPosition(position).toString();
            }
        });

        // Input Kalori
        caloriesAmount = findViewById(R.id.edit_text_product_calorificValue);

        // Input Białka
        proteinAmount = findViewById(R.id.edit_text_product_proteinValue);

        // Input Węglowodanów
        carbohydratesAmount = findViewById(R.id.edit_text_product_carbohydratesValue);

        // Input Tłuszczy
        fatAmount = findViewById(R.id.edit_text_product_fatValue);

        headerTextView.setText(R.string.header_add);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            headerTextView.setText(R.string.header_edit);
            selectManufacturers.setText(extras.getString(EXTRA_EDIT_MANUFACTURER_NAME));
            selectCategory.setText(extras.getString(EXTRA_EDIT_CATEGORY_NAME));
            productName.setText(extras.getString(EXTRA_EDIT_PRODUCT_NAME));
            selectUnit.setText(extras.getString(EXTRA_EDIT_UNIT_NAME));
            caloriesAmount.setText(extras.getString(EXTRA_EDIT_CALORIES_AMOUNT));
            proteinAmount.setText(extras.getString(EXTRA_EDIT_PROTEIN_AMOUNT));
            carbohydratesAmount.setText(extras.getString(EXTRA_EDIT_CARBOHYDRATES_AMOUNT));
            fatAmount.setText(extras.getString(EXTRA_EDIT_FAT_AMOUNT));
        }

        // Przycisk do zapisywania
        saveButton = findViewById(R.id.save_product_button);
        saveButton.setOnClickListener(this::onSaveButtonClick);
    }
    private void onSaveButtonClick(View v) {
        Category category = categoriesList.stream().filter(cat -> cat.getName().equals(selectedCategory))
                .findFirst().orElse(null);
        Manufacturer manufacturer = manufacturerList.stream().filter(man -> man.getName().equals(selectedManufacturer))
                .findFirst().orElse(null);
        MeasureUnit unit = unitsList.stream().filter(un -> un.getName().equals(selectedUnit))
                .findFirst().orElse(null);

        Intent replyIntent = new Intent();

        // Wiecej warunków
        if(!IsFormValid(category, manufacturer, unit)){
            setResult(RESULT_CANCELED, replyIntent);
        }
        else{
            replyIntent.putExtra(EXTRA_EDIT_MANUFACTURER_ID, String.valueOf(manufacturer.getManufacturerId()));
            replyIntent.putExtra(EXTRA_EDIT_CATEGORY_ID, String.valueOf(category.getCategoryId()));
            replyIntent.putExtra(EXTRA_EDIT_PRODUCT_NAME, productName.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_UNIT_ID, String.valueOf(unit.getMeasureUnitId()));
            replyIntent.putExtra(EXTRA_EDIT_CALORIES_AMOUNT, caloriesAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_PROTEIN_AMOUNT, proteinAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_CARBOHYDRATES_AMOUNT, carbohydratesAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FAT_AMOUNT, fatAmount.getText().toString());
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
    private boolean IsFormValid(Category cat, Manufacturer man, MeasureUnit unit){
        return cat != null && man!=null && unit!=null && !TextUtils.isEmpty(productName.getText()) &&
                !TextUtils.isEmpty(caloriesAmount.getText()) && !TextUtils.isEmpty(proteinAmount.getText()) &&
                !TextUtils.isEmpty(carbohydratesAmount.getText()) && !TextUtils.isEmpty(fatAmount.getText());
    }
}