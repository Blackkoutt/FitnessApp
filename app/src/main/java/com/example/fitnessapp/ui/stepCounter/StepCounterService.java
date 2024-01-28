package com.example.fitnessapp.ui.stepCounter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.fitnessapp.R;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("StepCounterService", "No step counter sensor found");
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Notification notification = new NotificationCompat.Builder(this, "StepCounterChannel")
                .setContentTitle(getString(R.string.not_counter))
                .setContentText(getString(R.string.not_counter_working))
                .setSmallIcon(R.drawable.baseline_steps_counter)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            if (previousTotalSteps == 0) {
                previousTotalSteps = totalSteps;
            }
            int stepsSinceLastBoot = totalSteps - previousTotalSteps;
            saveData(stepsSinceLastBoot);
            createNotification(stepsSinceLastBoot);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void createNotification(int currentSteps) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "StepCounterChannel")
                .setContentTitle(getString(R.string.asc))
                .setContentText(getString(R.string.steps_done) + currentSteps)
                .setSmallIcon(R.drawable.baseline_steps_counter);

        if (notificationManager != null) {
            notificationManager.notify(2, builder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
            CharSequence name = "StepCounterChannel";
            String description = "Step Counter Service Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("StepCounterChannel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }

    private void saveData(int steps) {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("key1", steps);
        editor.apply();
    }
}
