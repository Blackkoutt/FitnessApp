package com.example.fitnessapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.ViewModels.CategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ManufacturerViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddCategoryManufacturerActivity extends AppCompatActivity {
    private TextView headerTextView;
    private Button addButton;
    private String requestCode;
    private TextInputEditText manufacturerCategoryName;
    private TextInputLayout manufacturerCategoryNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_manufacturer);

        // Pobranie elementów widoku
        headerTextView = findViewById(R.id.header_cat_man_add);
        manufacturerCategoryName = findViewById(R.id.edit_text_man_cat_name);
        manufacturerCategoryNameLayout = findViewById(R.id.text_input_man_cat_name);
        addButton = findViewById(R.id.save_man_cat_button);

        // Ustawienie tekstu przycisku
        addButton.setText(R.string.add_string);

        // Ustawienie onClickListenera przycisku
        addButton.setOnClickListener(this::onClickAddManufacturerOrCategory);

        // Pobranie danych przekazanych z activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            requestCode = extras.getString(AddEditProductActivity.ADD_MAN_CAT_ACTIVITY_REQUEST_CODE);
            if (requestCode.equals("NEW_MANUFACTURER")) {
                headerTextView.setText(getString(R.string.add_manufacturer));
                manufacturerCategoryNameLayout.setHint(getString(R.string.manufacturer_name));
            } else if (requestCode.equals("NEW_CATEGORY")) {
                headerTextView.setText(getString(R.string.add_category));
                manufacturerCategoryNameLayout.setHint(getString(R.string.category_name));
            }
        }

    }

    // Metoda wywoływana w momencie wciśnięcia przycisku Dodaj
    private void onClickAddManufacturerOrCategory(View view) {
        if(ValidateForm()){
            String manufacturerOrCategoryName = String.valueOf(manufacturerCategoryName.getText());
            if (requestCode.equals("NEW_MANUFACTURER")){
                Manufacturer man = new Manufacturer(manufacturerOrCategoryName);
                ManufacturerViewModel manufacturerViewModel = new ViewModelProvider(this).get(ManufacturerViewModel.class);
                manufacturerViewModel.insert(man);
                finish();
            }
            else if (requestCode.equals("NEW_CATEGORY")) {
                Category cat = new Category(manufacturerOrCategoryName);
                CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
                categoryViewModel.insert(cat);
                finish();
            }
        }
    }

    // Metoda walidująca formularz
    private boolean ValidateForm(){
        if(TextUtils.isEmpty(manufacturerCategoryName.getText())){
            manufacturerCategoryNameLayout.setHelperText(getString(R.string.required_error));
            manufacturerCategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        if(TextUtils.isEmpty(manufacturerCategoryName.getText())){
                            manufacturerCategoryNameLayout.setHelperText(getString(R.string.required_error));
                        }
                        else{
                            manufacturerCategoryNameLayout.setHelperText("");
                        }
                    }
                }
            });
            return false;
        }
        return true;
    }
}