package com.example.fitnessapp.ui.bmi_calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class BmiCalculator extends Fragment {
    private static final String KEY_RESULTS_VISIBLE = "resultsVisible";
    private static final String KEY_BMI_NAME = "KEY_BMI_NAME";
    private static final String KEY_BMI_VALUE = "KEY_BMI_VALUE";
    private static final String KEY_COLOR_VALUE = "KEY_COLOR_VALUE";
    private boolean resultsVisible = false;
    private Button calculateBMIButton;
    private TextInputLayout heightTextInput;
    private EditText heightEditText;
    private TextInputLayout bodyWeightTextInput;
    private EditText bodyWeightEditText;
    private TextView BMIValueTextView;
    private TextView BMINameTextView;
    private String BMIName;
    private String BMIValue;
    private int color;


    // Metoda do zapisu stanu przy obróceniu ekranu
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_RESULTS_VISIBLE, resultsVisible);
        outState.putString(KEY_BMI_NAME, BMIName);
        outState.putInt(KEY_COLOR_VALUE, color);
        outState.putString(KEY_BMI_VALUE, BMIValue);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi_calculator, container, false);

        // Jeśli zapisano stan przy obrocie ekranu
        if(savedInstanceState != null){
            // Pobierz wszytskie wartości stanu
            resultsVisible = savedInstanceState.getBoolean(KEY_RESULTS_VISIBLE);
            BMIName = savedInstanceState.getString(KEY_BMI_NAME);
            BMIValue = savedInstanceState.getString(KEY_BMI_VALUE);
            color = savedInstanceState.getInt(KEY_COLOR_VALUE);
        }

        // Przypisz wszytskie elementy widoku
        calculateBMIButton = view.findViewById(R.id.calculate_bmi);
        heightTextInput = view.findViewById(R.id.text_input_height);
        heightEditText = view.findViewById(R.id.edit_text_height);
        bodyWeightTextInput = view.findViewById(R.id.text_input_body_weight);
        bodyWeightEditText = view.findViewById(R.id.edit_text_body_weight);
        BMIValueTextView = view.findViewById(R.id.your_bmi_value);
        BMINameTextView = view.findViewById(R.id.your_bmi_name);

        // Jeśli wyniki są widoczne przypisz ich zawartość oraz kolor
        if(resultsVisible){
            BMIValueTextView.setText(BMIValue);
            BMINameTextView.setText(BMIName);
            BMINameTextView.setTextColor(color);
            BMIValueTextView.setVisibility(View.VISIBLE);
            BMINameTextView.setVisibility(View.VISIBLE);
        }
        else{
            BMIValueTextView.setVisibility(View.GONE);
            BMINameTextView.setVisibility(View.GONE);
        }

        // Nasłuchiwanie zdarzenia click na przycisku oblicz kalorie
        calculateBMIButton.setOnClickListener(this::calculateBMI);

        return view;
    }

    // Metoda obliczająca kalorie
    private void calculateBMI(View view) {
        // Sprawdzenie poprawności wprowadzonych danych
        if(ValidateControl(heightTextInput, heightEditText) && ValidateControl(bodyWeightTextInput, bodyWeightEditText)){
            // Obliczenie BMI
            double height = Double.parseDouble(heightEditText.getText().toString());
            double weight = Double.parseDouble(bodyWeightEditText.getText().toString());
            double BMI = weight / (Math.pow(height,2));

            // Ustawienie widoczności pól wyników
            BMIValueTextView.setVisibility(View.VISIBLE);
            BMINameTextView.setVisibility(View.VISIBLE);

            // Ustawienie zmiennej - czy wyniki są widoczne
            resultsVisible = true;

            // Sformatowanie daty i ustawienie wyniku
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedBMI = df.format(BMI);
            BMIValueTextView.setText(getResources().getString(R.string.your_bmi, formattedBMI));

            // Ustawienie tekstu w zależności do wartości BMI
            // Switch na wartości double nie działa ;/
            if(BMI < BMI_ENUM.EMACIATION.value()){
                BMINameTextView.setText(getResources().getString(R.string.starvation));
                color = getResources().getColor(R.color.blue_700);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.EMACIATION.value() && BMI < BMI_ENUM.UNDERWEIGHT.value()){
                BMINameTextView.setText(getResources().getString(R.string.emaciation));
                color = getResources().getColor(R.color.blue_500);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.UNDERWEIGHT.value() && BMI < BMI_ENUM.CORRECT.value()){
                BMINameTextView.setText(getResources().getString(R.string.underweight));
                color = getResources().getColor(R.color.blue_green);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.CORRECT.value() && BMI < BMI_ENUM.OVERWEIGHT.value()){
                BMINameTextView.setText(getResources().getString(R.string.correct));
                color = getResources().getColor(R.color.green);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.OVERWEIGHT.value() && BMI < BMI_ENUM.FIRST_DEGREE_OBESITY.value()){
                BMINameTextView.setText(getResources().getString(R.string.overweight));
                color = getResources().getColor(R.color.yellow);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.FIRST_DEGREE_OBESITY.value() && BMI < BMI_ENUM.SECOND_DEGREE_OBESITY.value()){
                BMINameTextView.setText(getResources().getString(R.string.first_degree_obesity));
                color = getResources().getColor(R.color.orange);
                BMINameTextView.setTextColor(color);
            }
            else if(BMI >= BMI_ENUM.SECOND_DEGREE_OBESITY.value() && BMI < BMI_ENUM.THIRD_DEGREE_OBESITY.value()){
                BMINameTextView.setText(getResources().getString(R.string.second_degree_obesity));
                color = getResources().getColor(R.color.red_base);
                BMINameTextView.setTextColor(color);
            }
            else{
                BMINameTextView.setText(getResources().getString(R.string.third_degree_obesity));
                color = getResources().getColor(R.color.dark_red);
                BMINameTextView.setTextColor(color);
            }
            BMIName = BMINameTextView.getText().toString();
            BMIValue = BMIValueTextView.getText().toString();
        }
    }

    // Metoda walidująca kontrolkę formularza
    private boolean ValidateControl(TextInputLayout textInputLayout, EditText editText){
        if(TextUtils.isEmpty(editText.getText())){
            textInputLayout.setHelperText(getString(R.string.required_error));
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        if(TextUtils.isEmpty(editText.getText())){
                            textInputLayout.setHelperText(getString(R.string.required_error));
                        }
                        else{
                            textInputLayout.setHelperText("");
                        }
                    }
                }
            });
            return false;
        }
        return true;
    }
}
