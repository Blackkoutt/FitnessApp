package com.example.fitnessapp.Database.Models;

import androidx.room.TypeConverter;
import java.time.LocalDate;

// Klasa konwerterów typów
public class Converters {
    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return (value == null) ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return (date == null) ? null : date.toEpochDay();
    }
}

