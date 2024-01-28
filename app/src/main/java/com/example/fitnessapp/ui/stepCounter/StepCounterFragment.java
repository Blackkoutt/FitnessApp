package com.example.fitnessapp.ui.stepCounter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp.R;


public class StepCounterFragment extends Fragment {
    private ProgressBar progressBar;
    private TextView steps;
    private TextView distance;
    private TextView caloriesBurned;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_counter, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        steps = rootView.findViewById(R.id.steps);
        distance = rootView.findViewById(R.id.distance);
        caloriesBurned = rootView.findViewById(R.id.caloriesBurned);

        //resetSteps();
        loadData();

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        startStepCounterService();
    }

    private void startStepCounterService() {
        Intent serviceIntent = new Intent(requireContext(), StepCounterService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(serviceIntent);
        } else {
            requireContext().startService(serviceIntent);
        }
    }

    private void resetSteps() {
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "LongPress to reset steps", Toast.LENGTH_SHORT).show();
            }
        });
        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("key1", 0);
                editor.apply();
                steps.setText("0");
                progressBar.setProgress(0);
                return true;
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedSteps = sharedPref.getInt("key1", 0);
        steps.setText(String.valueOf(savedSteps));
        progressBar.setProgress(savedSteps);
        double calculatedDistance = calculateDistance(savedSteps);
        double calculatedCaloriesBurned = calculateCaloriesBurned(savedSteps);

        distance.setText(getString(R.string.distance, calculatedDistance));
        caloriesBurned.setText(getString(R.string.caloriesBurned, calculatedCaloriesBurned));
    }
    private double calculateDistance(int stepsCount) {
        double stepLength = 0.5;
        return stepsCount * stepLength;
    }

    private double calculateCaloriesBurned(int stepsCount) {
        double caloriesPerStep = 0.04;
        return stepsCount * caloriesPerStep;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().stopService(new Intent(requireContext(), StepCounterService.class));
    }
}
