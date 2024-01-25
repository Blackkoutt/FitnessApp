package com.example.fitnessapp.ui.meals;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.R;
import com.example.fitnessapp.ui.products.ProductsFragment;

import org.w3c.dom.Text;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MealsFragment extends Fragment {
    private Button previousMonthButton;
    private Button nextMonthButton;
    private TextView monthViewTextView;
    private LocalDate selectedDate;
    private RecyclerView recyclerView;
    private MealsFragment.CalendarAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        previousMonthButton = view.findViewById(R.id.previous_month);
        nextMonthButton = view.findViewById(R.id.next_month);
        monthViewTextView = view.findViewById(R.id.moth_view_text_view);
        recyclerView = view.findViewById(R.id.calendar_recycler_view);
        adapter = new MealsFragment.CalendarAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 7));
        selectedDate = LocalDate.now();

        setMonthView();

        previousMonthButton.setOnClickListener(this::setPreviousMonth);
        nextMonthButton.setOnClickListener(this::setNextMonth);

        return view;
    }
    private void setMonthView(){
        monthViewTextView.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = getDaysInMonthArray(selectedDate);
        adapter.setDays(daysInMonth);
    }

    private ArrayList<String> getDaysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);

        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i=1; i<= 42; i++){
            if(i<dayOfWeek || i+1>daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }
            else{
                daysInMonthArray.add(String.valueOf(i+1 - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return date.format(formatter);
    }

    private void setNextMonth(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setPreviousMonth(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    private class CalendarViewHolder extends RecyclerView.ViewHolder {
        private String dayOfMonth;
        private final TextView dayOfMonthTextView;
       public  CalendarViewHolder(LayoutInflater inflater, ViewGroup parent){
           super(inflater.inflate(R.layout.calendar_cell, parent, false));
           dayOfMonthTextView = itemView.findViewById(R.id.cellDayText);
           ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
           layoutParams.height = (int) (parent.getHeight() * 0.166666666);
       }
        public void bind(String dayOfMonth){
            this.dayOfMonth= dayOfMonth;
            dayOfMonthTextView.setText(dayOfMonth);
        }
    }
    private class CalendarAdapter extends RecyclerView.Adapter<MealsFragment.CalendarViewHolder> {
        private ArrayList<String> daysOfMonth;
        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MealsFragment.CalendarViewHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            if(daysOfMonth != null){
                String dayString = daysOfMonth.get(position);
                holder.bind(dayString);
            }
            else {
                Log.d("MainActivity", "No days");
            }
        }

        @Override
        public int getItemCount() {
            return daysOfMonth.size();
        }
        void setDays(ArrayList<String> days){
            this.daysOfMonth = days;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}