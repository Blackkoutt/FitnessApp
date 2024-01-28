package com.example.fitnessapp.ui.meals;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.AddEditCaloricLimitActivity;
import com.example.fitnessapp.AddEditMealActivity;
import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealProduct;
import com.example.fitnessapp.Database.Models.MealWithRelations;
import com.example.fitnessapp.Database.ViewModels.CaloricLimitViewModel;
import com.example.fitnessapp.Database.ViewModels.MealProductViewModel;
import com.example.fitnessapp.Database.ViewModels.MealViewModel;
import com.example.fitnessapp.MealDetailsActivity;
import com.example.fitnessapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MealsFragment extends Fragment {
    private Button previousMonthButton;
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_MEAL = "EXTRA_MEAL";
    public static final String EXTRA_ACTUAL_LIMIT = "EXTRA_ACTUAL_LIMIT";
    public static final String EXTRA_MEAL_DETAILS = "EXTRA_MEAL_DETAILS";
    public static final String EXTRA_LIMIT = "EXTRA_LIMIT";
    public static final String REQUEST_CODE_ADD_EDIT_MEAL = "REQUEST_CODE_ADD_EDIT_MEAL";
    public static final String EXTRA_TOTAL_CALORIFIC = "EXTRA_TOTAL_CALORIFIC";
    public static final String ADD_EDIT_LIMIT_REQUEST_CODE = "ADD_EDIT_LIMIT_REQUEST_CODE";

    public static final int NEW_MEAL_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_MEAL_ACTIVITY_REQUEST_CODE = 2;
    private Button nextMonthButton;
    private TextView monthViewTextView;
    private LocalDate selectedDate;
    private RecyclerView recyclerView;
    private MealsFragment.CalendarAdapter adapter;
    private View lastClickedParentView = null;
    private RecyclerView mealRecyclerView;
    private LocalDate selectedDay;
    private MealAdapter mealAdapter;
    private MealViewModel mealViewModel;
    private MealProductViewModel mealProductViewModel;
    private CaloricLimitViewModel caloricLimitViewModel;
    private Button newMealButton;
    private Meal editedMeal;
    private View fragmentView;
    private TextView limitTextView;
    private TextView limitExceededTextView;
    private TextView caloriesTextView;
    private float totalCalorificOfSelectedDay;
    private float limitOfSelectedDay;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            long categoryId = Long.parseLong(data.getStringExtra(AddEditMealActivity.EXTRA_EDIT_CATEGORY_ID));
            long[] productsIds = data.getLongArrayExtra(AddEditMealActivity.EXTRA_EDIT_PRODUCTS_IDS);
            float totalCalories = Float.parseFloat(data.getStringExtra(AddEditMealActivity.EXTRA_EDIT_TOTAL_CALORIES));
            float totalProteins = Float.parseFloat(data.getStringExtra(AddEditMealActivity.EXTRA_EDIT_TOTAL_PROTEINS));
            float totalCarbohydrates = Float.parseFloat(data.getStringExtra(AddEditMealActivity.EXTRA_EDIT_TOTAL_CARBOHYDRATES));
            float totalFat = Float.parseFloat(data.getStringExtra(AddEditMealActivity.EXTRA_EDIT_TOTAL_FATS));

            // Dodawanie posiłku
            if(requestCode == NEW_MEAL_ACTIVITY_REQUEST_CODE){
                // trzeba sprawdzić czy selectedDay jest takie samo jak wybrane wczesniej
                Meal mealToAdd = new Meal(categoryId, selectedDay, totalCalories, totalProteins, totalCarbohydrates, totalFat);
                long mealId = mealViewModel.insert(mealToAdd);

                for(long productId : productsIds){
                    MealProduct mealProduct = new MealProduct(mealId, productId);
                    mealProductViewModel.insert(mealProduct);
                }


                Snackbar.make(fragmentView.findViewById(R.id.main_fragment_layout), getString(R.string.meal_added_succesful),
                        Snackbar.LENGTH_LONG).show();
            }
            // Edycja produktu
            else{
                editedMeal.setMealCategoryId(categoryId);
                editedMeal.setTotalCalorific(totalCalories);
                editedMeal.setTotalProtein(totalProteins);
                editedMeal.setTotalCarbohydrates(totalCarbohydrates);
                editedMeal.setTotalFat(totalFat);
                mealViewModel.update(editedMeal);

                mealProductViewModel.deleteByMealId(editedMeal.getMealId());
                for(long productId : productsIds){
                    MealProduct mealProduct = new MealProduct(editedMeal.getMealId(), productId);
                    mealProductViewModel.insert(mealProduct);
                }
                Snackbar.make(fragmentView.findViewById(R.id.main_fragment_layout), getString(R.string.meal_modified_succesful),
                        Snackbar.LENGTH_LONG).show();
            }
        }
        else{
            Snackbar.make(fragmentView.findViewById(R.id.main_fragment_layout), getString(R.string.empty_not_saved_meal),
                    Snackbar.LENGTH_LONG).show();
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meals, container, false);
        fragmentView = view;

        previousMonthButton = view.findViewById(R.id.previous_month);
        nextMonthButton = view.findViewById(R.id.next_month);
        monthViewTextView = view.findViewById(R.id.moth_view_text_view);
        newMealButton = view.findViewById(R.id.new_meal);
        limitTextView = view.findViewById(R.id.limit_text);
        limitExceededTextView = view.findViewById(R.id.limit_exceeded);
        caloriesTextView = view.findViewById(R.id.calories_text);
        limitExceededTextView.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.calendar_recycler_view);
        adapter = new MealsFragment.CalendarAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 7));
        selectedDate = LocalDate.now();
        selectedDay = LocalDate.now();

        mealRecyclerView = view.findViewById(R.id.meal_recycler_view);
        mealAdapter = new MealAdapter();
        mealRecyclerView.setAdapter(mealAdapter);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mealViewModel = new ViewModelProvider(getActivity()).get(MealViewModel.class);
        mealViewModel.getAllByDate(selectedDate).observe(getActivity(), mealAdapter::setMeals);

        mealProductViewModel = new ViewModelProvider(getActivity()).get(MealProductViewModel.class);

        caloricLimitViewModel = new ViewModelProvider(getActivity()).get(CaloricLimitViewModel.class);

        newMealButton.setOnClickListener(this::onAddMeal);

        setWeekView();

        previousMonthButton.setOnClickListener(this::setPreviousWeek);
        nextMonthButton.setOnClickListener(this::setNextWeek);

        return view;
    }

    private void onAddMeal(View view) {
        Intent intent = new Intent(getActivity(), AddEditMealActivity.class);
        intent.putExtra(EXTRA_TOTAL_CALORIFIC, totalCalorificOfSelectedDay);
        intent.putExtra(EXTRA_LIMIT, limitOfSelectedDay);
        intent.putExtra(REQUEST_CODE_ADD_EDIT_MEAL, "ADD_MEAL");
        startActivityForResult(intent, NEW_MEAL_ACTIVITY_REQUEST_CODE);
    }

    private void setWeekView(){
        monthViewTextView.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> days = getDaysInWeekArray(selectedDate);
        adapter.setDays(days);
    }

    private ArrayList<LocalDate> getDaysInWeekArray(LocalDate date) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForMonday(date);

        for (int i = 0; i < 7; i++) {
            days.add(current);
            current = current.plusDays(1);
        }

        return days;
    }

    private LocalDate sundayForMonday(LocalDate current) {
        while (current.getDayOfWeek() != DayOfWeek.MONDAY) {
            current = current.minusDays(1);
        }
        return current;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return date.format(formatter);
    }

    private void setNextWeek(View view) {
        selectedDate = selectedDate.plusWeeks(1);
        setWeekView();
    }

    private void setPreviousWeek(View view) {
        selectedDate = selectedDate.minusWeeks(1);
        setWeekView();
    }
    private class MealHolder extends RecyclerView.ViewHolder {

        private MealWithRelations meal;
        private TextView mealCategoryTextView;
        private TextView mealCalorificTextView;
        private TextView mealProteinTextView;
        private TextView mealCarbohydratesTextView;
        private TextView mealFatTextView;
        public MealHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.meal_list_item, parent, false));
            mealCategoryTextView = itemView.findViewById(R.id.meal_category);
            mealCalorificTextView = itemView.findViewById(R.id.meal_calorific);
            mealProteinTextView = itemView.findViewById(R.id.meal_protein);
            mealCarbohydratesTextView = itemView.findViewById(R.id.meal_carbohydrates);
            mealFatTextView = itemView.findViewById(R.id.meal_fat);

            itemView.setOnClickListener(this::EditMeal);
            itemView.setOnLongClickListener(this::DeleteMeal);
            //itemView.setOnTouchListener(this::DetailsMeal);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Intent intent = new Intent(getActivity(), MealDetailsActivity.class);
                        intent.putExtra("EXTRA_MEAL_DETAILS", meal);

                        startActivity(intent);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    return false;
                }
            });
        }

        private boolean DeleteMeal(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
            builder.setTitle(getResources().getString(R.string.meal_delete_question, meal.mealCategory.getName(), meal.meal.getDate().toString()));
            builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mealProductViewModel.deleteByMealId(meal.meal.getMealId());
                    mealViewModel.delete(meal.meal);

                    Snackbar.make(fragmentView.findViewById(R.id.main_fragment_layout), getString(R.string.meal_deleted),
                            Snackbar.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create();
            builder.show();
            return true;
        }

        private void EditMeal(View view) {
            editedMeal = meal.meal;
            Intent intent = new Intent(getActivity(), AddEditMealActivity.class);
            intent.putExtra(EXTRA_TOTAL_CALORIFIC, totalCalorificOfSelectedDay);
            intent.putExtra(EXTRA_LIMIT, limitOfSelectedDay);
            intent.putExtra(REQUEST_CODE_ADD_EDIT_MEAL, "EDIT_MEAL");
            Bundle extras = new Bundle();

            extras.putSerializable(EXTRA_MEAL, meal);
            intent.putExtras(extras);

            startActivityForResult(intent, EDIT_MEAL_ACTIVITY_REQUEST_CODE);
        }

        public void bind(MealWithRelations meal){
            this.meal = meal;
            mealCategoryTextView.setText(meal.mealCategory.getName());
            mealCalorificTextView.setText(getResources().getString(R.string.total_calorific, String.valueOf(meal.meal.getTotalCalorific())));
            mealProteinTextView.setText(getResources().getString(R.string.total_protein, String.valueOf(meal.meal.getTotalProtein())));
            mealCarbohydratesTextView.setText(getResources().getString(R.string.total_carbohydrates, String.valueOf(meal.meal.getTotalCarbohydrates())));
            mealFatTextView.setText(getResources().getString(R.string.total_fat, String.valueOf(meal.meal.getTotalFat())));
        }
    }
    private class MealAdapter extends RecyclerView.Adapter<MealsFragment.MealHolder>{
        private List<MealWithRelations> meals;
        @NonNull
        @Override
        public MealsFragment.MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MealsFragment.MealHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MealsFragment.MealHolder holder, int position) {
            if(meals != null){
                MealWithRelations meal = meals.get(position);
                holder.bind(meal);
            }
            else {
                Log.d("MainActivity", "No meals");
            }
        }

        @Override
        public int getItemCount() {
            if(meals != null){
                return meals.size();
            }
            else{
                return 0;
            }
        }
        void setMeals(List<MealWithRelations> meals){
            this.meals = meals;
            notifyDataSetChanged();
        }
    }

    private class CalendarViewHolder extends RecyclerView.ViewHolder {
        private LocalDate dayOfWeek;
        private final View parentView;
        private final TextView dayOfMonthTextView;
        public  CalendarViewHolder(LayoutInflater inflater, ViewGroup parent){
           super(inflater.inflate(R.layout.calendar_cell, parent, false));
           dayOfMonthTextView = itemView.findViewById(R.id.cellDayText);
           parentView = itemView.findViewById(R.id.parentView);
           ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
           layoutParams.height = (int) (parent.getHeight());

           itemView.setOnLongClickListener(this::onCalendarDayLongClick);
           itemView.setOnClickListener(this::onCalendarDayClick);
       }

        private boolean onCalendarDayLongClick(View view) {
            caloricLimitViewModel.getLimitByDate(dayOfWeek).observe(getActivity(), new Observer<CaloricLimit>() {
                @Override
                public void onChanged(CaloricLimit caloricLimit) {
                    Intent intent = new Intent(getActivity(), AddEditCaloricLimitActivity.class);
                    intent.putExtra(EXTRA_DATE, selectedDay.toString());
                    if(caloricLimit == null){
                        // znaczy ze nie ma limitu i można dodać na dany dzień
                        intent.putExtra(ADD_EDIT_LIMIT_REQUEST_CODE, "ADD_LIMIT");
                    }
                    else{
                        // znaczy że jest limit ale można zmodyfikować
                        intent.putExtra(ADD_EDIT_LIMIT_REQUEST_CODE, "EDIT_LIMIT");
                        intent.putExtra(EXTRA_ACTUAL_LIMIT, caloricLimit);
                    }
                    startActivity(intent);
                }
            });
            return true;
        }

        private void onCalendarDayClick(View view) {
            mealViewModel.getAllByDate(dayOfWeek).observe(getActivity(), new Observer<List<MealWithRelations>>() {
                @Override
                public void onChanged(List<MealWithRelations> mealWithRelations) {
                    caloricLimitViewModel.getLimitByDate(dayOfWeek).observe(getActivity(), new Observer<CaloricLimit>() {
                        @Override
                        public void onChanged(CaloricLimit caloricLimit) {
                            float totalCalorificByDay = 0;
                            for(MealWithRelations mealWR : mealWithRelations){
                                totalCalorificByDay += mealWR.meal.getTotalCalorific();
                            }
                            totalCalorificOfSelectedDay = totalCalorificByDay;


                            caloriesTextView.setText(getResources().getString(R.string.calories_text, String.valueOf(totalCalorificByDay)));
                            if(caloricLimit == null){
                                limitExceededTextView.setVisibility(View.GONE);
                                limitTextView.setText(getResources().getString(R.string.limit_text, getString(R.string.limit_empty)));
                                limitOfSelectedDay = 0;
                            }
                            else{
                                limitOfSelectedDay = caloricLimit.getCaloricLimit();
                                if(totalCalorificByDay>caloricLimit.getCaloricLimit()){
                                    limitExceededTextView.setVisibility(View.VISIBLE);
                                }
                                else{
                                    limitExceededTextView.setVisibility(View.GONE);
                                }
                                limitTextView.setText(getResources().getString(R.string.limit_text, String.valueOf(caloricLimit.getCaloricLimit())));
                            }
                        }
                    });
                }
            });
            if(lastClickedParentView!=null){
                lastClickedParentView.setBackgroundColor(Color.WHITE);
            }
            selectedDay = dayOfWeek;
            mealViewModel.getAllByDate(dayOfWeek).observe(getActivity(), mealAdapter::setMeals);
            parentView.setBackgroundColor(Color.LTGRAY);
            lastClickedParentView = parentView;
        }

        public void bind(LocalDate day){
           if(day == null){
               dayOfMonthTextView.setText("");
           }
           else{
               this.dayOfWeek= day;
               dayOfMonthTextView.setText(String.valueOf(dayOfWeek.getDayOfMonth()));
           }
        }
    }
    private class CalendarAdapter extends RecyclerView.Adapter<MealsFragment.CalendarViewHolder> {
        private ArrayList<LocalDate> daysOfWeek;
        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MealsFragment.CalendarViewHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            if(daysOfWeek != null){
                LocalDate day = daysOfWeek.get(position);
                holder.bind(day);
            }
            else {
                Log.d("MainActivity", "No days");
            }
        }

        @Override
        public int getItemCount() {
            return daysOfWeek.size();
        }
        void setDays(ArrayList<LocalDate> days){
            this.daysOfWeek = days;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}