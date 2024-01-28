package com.example.fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.ViewModels.CategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ManufacturerViewModel;
import com.example.fitnessapp.Database.ViewModels.MeasureUnitViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> categoryNames;
    private List<Category> selectedCategoriesList;
    private boolean[] selectedCategoriesBool;
    private List<String> actualProductCategoryNames;
    private Button addCategoryButton;
    private Button addManufacturerButton;
    public static final String ADD_MAN_CAT_ACTIVITY_REQUEST_CODE = "ADD_MAN_CAT_ACTIVITY_REQUEST_CODE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        selectedCategoriesList = new ArrayList<Category>();
        actualProductCategoryNames = new ArrayList<String>();

        // Pobranie elementów widoku
        headerTextView = findViewById(R.id.header_edit_add);
        addCategoryButton = findViewById(R.id.add_category_button);
        selectManufacturers = findViewById(R.id.select_manufacturer);
        addManufacturerButton = findViewById(R.id.add_manufacturer_button);
        selectCategory = findViewById(R.id.select_category);
        productName = findViewById(R.id.edit_text_product_name);
        selectUnit = findViewById(R.id.select_unit);
        caloriesAmount = findViewById(R.id.edit_text_product_calorificValue);
        proteinAmount = findViewById(R.id.edit_text_product_proteinValue);
        carbohydratesAmount = findViewById(R.id.edit_text_product_carbohydratesValue);
        fatAmount = findViewById(R.id.edit_text_product_fatValue);
        saveButton = findViewById(R.id.save_product_button);

        // Ustawienie onClickListenera dla przycisku "Zapisz"
        saveButton.setOnClickListener(this::onSaveButtonClick);

        // Ustawienie onClickListenera dla przycisku "Dodaj producenta"
        addManufacturerButton.setOnClickListener(this::onClickAddManufacturer);

        // Ustawienie onClickListenera dla przycisku "Dodaj kategorie"
        addCategoryButton.setOnClickListener(this::onClickAddCategory);

        // Pobranie listy producentów z bazy
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

        // Ustawienie onClickListenera dla selecta - wybranie producenta
        selectManufacturers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedManufacturer = parent.getItemAtPosition(position).toString();
            }
        });

        // Pobranie listy kategorii z bazy
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAll().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoriesList = categories;
                categoryNames = new ArrayList<>();
                selectedCategoriesBool = new boolean[categoriesList.size()];
                // tutaj ustawić wartośic true jest jest juz na liscie
                for (Category category : categoriesList) {
                    categoryNames.add(category.getName());
                }
                for(int i=0;i<selectedCategoriesBool.length;i++){
                    if(actualProductCategoryNames.contains(categoryNames.get(i))){
                        selectedCategoriesList.add(categoriesList.get(i));
                        selectedCategoriesBool[i]=true;
                    }
                }
            }
        });

        // Kliknięcie na select Kategorii powoduje otwarcie okna dialogowego
        selectCategory.setOnClickListener(v->{
            showCategoryDialog();
        });

        selectCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }
        });

        // Pobranie listy jednostek z bazy
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

        // Ustawienie onclickListenera dla wyboru jednostki
        selectUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = parent.getItemAtPosition(position).toString();
            }
        });

        headerTextView.setText(R.string.header_add);

        // Pobranie wartości przekazanych do activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            headerTextView.setText(R.string.header_edit);
            selectManufacturers.setText(extras.getString(EXTRA_EDIT_MANUFACTURER_NAME));
            selectedManufacturer = extras.getString(EXTRA_EDIT_MANUFACTURER_NAME);

            actualProductCategoryNames = extras.getStringArrayList(EXTRA_EDIT_CATEGORY_NAME);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i<actualProductCategoryNames.size(); i++){
                stringBuilder.append(actualProductCategoryNames .get(i));
                if(i != actualProductCategoryNames.size() - 1){
                    stringBuilder.append(", ");
                }
            }

            // Przypisanie wartości do widoku
            selectCategory.setText(stringBuilder.toString());
            productName.setText(extras.getString(EXTRA_EDIT_PRODUCT_NAME));
            selectUnit.setText(extras.getString(EXTRA_EDIT_UNIT_NAME));
            selectedUnit = extras.getString(EXTRA_EDIT_UNIT_NAME);
            caloriesAmount.setText(extras.getString(EXTRA_EDIT_CALORIES_AMOUNT));
            proteinAmount.setText(extras.getString(EXTRA_EDIT_PROTEIN_AMOUNT));
            carbohydratesAmount.setText(extras.getString(EXTRA_EDIT_CARBOHYDRATES_AMOUNT));
            fatAmount.setText(extras.getString(EXTRA_EDIT_FAT_AMOUNT));
        }
    }

    // Metoda wywoływana w momencie kliknięcia przycisku "Dodaj producenta"
    private void onClickAddManufacturer(View view) {
        Intent intent = new Intent(this, AddCategoryManufacturerActivity.class);
        intent.putExtra(ADD_MAN_CAT_ACTIVITY_REQUEST_CODE, "NEW_MANUFACTURER");
        startActivity(intent);
    }

    // Metoda wywoływana w momencie kliknięcia przycisku "Dodaj kategorię"
    private void onClickAddCategory(View view) {
        Intent intent = new Intent(this, AddCategoryManufacturerActivity.class);
        intent.putExtra(ADD_MAN_CAT_ACTIVITY_REQUEST_CODE, "NEW_CATEGORY");
        startActivity(intent);
    }

    // Metoda wywoływana w momencie kliknięcia selecta kategorii - otwiera się okno dialogowe z wyborem kategori
    private void showCategoryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditProductActivity.this);

        builder.setTitle(R.string.category_dialog_header);
        builder.setCancelable(false);
        CharSequence[] categoryNamesArray = categoryNames.toArray(new CharSequence[categoryNames.size()]);
        builder.setMultiChoiceItems(categoryNamesArray, selectedCategoriesBool, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    selectedCategoriesList.add(categoriesList.get(which));
                }
                else{
                    selectedCategoriesList.remove(categoriesList.get(which));
                }
            }
        }).setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<selectedCategoriesList.size(); i++){
                    stringBuilder.append(selectedCategoriesList.get(i).getName());
                    if(i != selectedCategoriesList.size() - 1){
                        stringBuilder.append(", ");
                    }
                }
                selectCategory.setText(stringBuilder.toString());
            }
        }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton(R.string.clear_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategoriesList.clear();
                for(int i=0;i<selectedCategoriesBool.length;i++){
                    selectedCategoriesBool[i]=false;
                }
                selectCategory.setText("");
            }
        });
        builder.show();
    }

    // Metoda wywoływana w momencie kliknięcia przycisku "Zapisz"
    private void onSaveButtonClick(View v) {
        Manufacturer manufacturer = manufacturerList.stream().filter(man -> man.getName().equals(selectedManufacturer))
                .findFirst().orElse(null);
        MeasureUnit unit = unitsList.stream().filter(un -> un.getName().equals(selectedUnit))
                .findFirst().orElse(null);
        long[] categoryIds = new long [selectedCategoriesList.size()];

        for(int i=0;i<selectedCategoriesList.size();i++){
            categoryIds[i] = selectedCategoriesList.get(i).getCategoryId();
        }

        Intent replyIntent = new Intent();

        // Walidacja formularza
        if(IsFormValid(categoryIds, manufacturer, unit)){
            // Przesłanie danych do activity
            replyIntent.putExtra(EXTRA_EDIT_MANUFACTURER_ID, String.valueOf(manufacturer.getManufacturerId()));
            replyIntent.putExtra(EXTRA_EDIT_CATEGORY_ID, categoryIds);
            replyIntent.putExtra(EXTRA_EDIT_PRODUCT_NAME, productName.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_UNIT_ID, String.valueOf(unit.getMeasureUnitId()));
            replyIntent.putExtra(EXTRA_EDIT_CALORIES_AMOUNT, caloriesAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_PROTEIN_AMOUNT, proteinAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_CARBOHYDRATES_AMOUNT, carbohydratesAmount.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FAT_AMOUNT, fatAmount.getText().toString());
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    // Metoda sprawdzająca czy dane w formularzu są poprawne
    private boolean IsFormValid(long[] catIds, Manufacturer man, MeasureUnit unit){
        if(catIds.length == 0 ){
            FocusListener(R.id.text_input_product_category, selectCategory);
            return false;
        }
        if(man==null){
            FocusListener(R.id.text_input_product_manufacturer, selectManufacturers);
            return false;
        }
        if(unit==null){
            FocusListener(R.id.text_input_product_unit, selectUnit);
            return false;
        }
        if(TextUtils.isEmpty(productName.getText())){
            FocusListener(R.id.text_input_product_name, productName);
            return false;
        }
        if(TextUtils.isEmpty(caloriesAmount.getText())){
            FocusListener(R.id.text_input_product_calorificValue, caloriesAmount);
            return false;
        }
        if(TextUtils.isEmpty(proteinAmount.getText())){
            FocusListener(R.id.text_input_product_proteinValue, proteinAmount);
            return false;
        }
        if(TextUtils.isEmpty(carbohydratesAmount.getText())){
            FocusListener(R.id.text_input_product_carbohydratesValue, carbohydratesAmount);
            return false;
        }
        if(TextUtils.isEmpty(fatAmount.getText())){
            FocusListener(R.id.text_input_product_fatValue, fatAmount);
            return false;
        }
        return true;
    }

    // Metoda dodająca FocusListener na elemencie formularza
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
}