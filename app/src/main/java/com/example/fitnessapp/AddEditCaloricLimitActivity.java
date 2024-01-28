package com.example.fitnessapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.ViewModels.CaloricLimitViewModel;
import com.example.fitnessapp.ui.meals.MealsFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddEditCaloricLimitActivity extends AppCompatActivity {
    private TextView headerTextView;
    private TextView dateTextView;
    private TextInputLayout caloricLimitTextInput;
    private EditText caloricLimitEditText;
    private Button saveLimitButton;
    private String requestCode;
    private CaloricLimit limit;
    private String date;
    private CaloricLimitViewModel caloricLimitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_caloric_limit);

        // Pobranie elementów widoku
        headerTextView = findViewById(R.id.header_edit_add);
        dateTextView = findViewById(R.id.date_textview);
        caloricLimitTextInput = findViewById(R.id.text_input_caloric_limit);
        caloricLimitEditText = findViewById(R.id.edit_text_caloric_limit);
        saveLimitButton = findViewById(R.id.save_limit_button);

        // Utworzenie ViewModelu limitu - komunikacja z bazą danych
        caloricLimitViewModel = new ViewModelProvider(this).get(CaloricLimitViewModel.class);

        // Dodanie onCLickListenera przycisku zapisz
        saveLimitButton.setOnClickListener(this::saveLimit);

        // Pobranie danych przekazanych z aktywności
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            requestCode = extras.getString(MealsFragment.ADD_EDIT_LIMIT_REQUEST_CODE);
            if (requestCode.equals("ADD_LIMIT")) {
                headerTextView.setText(getString(R.string.add_limit));
            } else if (requestCode.equals("EDIT_LIMIT")) {
                headerTextView.setText(getString(R.string.edit_limit));
                limit = (CaloricLimit) extras.getSerializable(MealsFragment.EXTRA_ACTUAL_LIMIT);
                caloricLimitEditText.setText(String.valueOf(limit.getCaloricLimit()));
            }
        }
        date = extras.getString(MealsFragment.EXTRA_DATE);
        dateTextView.setText(date);
    }

    // Metoda wywoływana w momencie wciśnięcia przycisku zapisz
    private void saveLimit(View view) {
        if(ValidateForm()){
            float newLimit = Float.parseFloat(caloricLimitEditText.getText().toString());

            if (requestCode.equals("ADD_LIMIT")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date, formatter);
                CaloricLimit limitToAdd = new CaloricLimit(newLimit, localDate);
                caloricLimitViewModel.insert(limitToAdd);
            } else if (requestCode.equals("EDIT_LIMIT")) {
                if(limit!=null){
                    limit.setCaloricLimit(newLimit);
                    caloricLimitViewModel.update(limit);
                }
            }
            finish();
        }
    }

    // Metoda walidująca formularz
    private boolean ValidateForm(){
        if(TextUtils.isEmpty(caloricLimitEditText.getText())){
            caloricLimitTextInput.setHelperText(getString(R.string.required_error));
            caloricLimitEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        if(TextUtils.isEmpty(caloricLimitEditText.getText())){
                            caloricLimitTextInput.setHelperText(getString(R.string.required_error));
                        }
                        else{
                            caloricLimitTextInput.setHelperText("");
                        }
                    }
                }
            });
            return false;
        }
        return true;
    }
}