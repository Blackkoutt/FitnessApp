package com.example.fitnessapp.ui.meals;

import android.graphics.Color;
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
import java.time.DayOfWeek;
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
    private  View lastClickedParentView = null;


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

        setWeekView();

        previousMonthButton.setOnClickListener(this::setPreviousWeek);
        nextMonthButton.setOnClickListener(this::setNextWeek);

        return view;
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
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(lastClickedParentView!=null){
                       lastClickedParentView.setBackgroundColor(Color.WHITE);
                   }
                   parentView.setBackgroundColor(Color.LTGRAY);
                   lastClickedParentView = parentView;
               }
           });
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