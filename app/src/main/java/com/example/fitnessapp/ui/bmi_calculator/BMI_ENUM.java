package com.example.fitnessapp.ui.bmi_calculator;

public enum BMI_ENUM {
    EMACIATION(16.0),
    UNDERWEIGHT(17.0),
    CORRECT(18.5),
    OVERWEIGHT(25.0),
    FIRST_DEGREE_OBESITY(30.0),
    SECOND_DEGREE_OBESITY(35.0),
    THIRD_DEGREE_OBESITY(40.0);

    private final double BMIValue;

    BMI_ENUM(double BMIValue) {
        this.BMIValue = BMIValue;
    }

    public double value() {
        return BMIValue;
    }
}
