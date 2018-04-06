package com.project.activityrecognitionsample;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityDispatcher {

    private final long interval = 30000;
    private ActivityRecognitionClient mActivityRecognitionClient;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DetectedActivitiesIntentService.setActivityDispatcher(this);

        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                interval,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "Tracking started",
                        Toast.LENGTH_SHORT)
                        .show();


            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("ACT", "Failed to start tracking");
                Toast.makeText(mContext,
                        "Failed to start tracking",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        DetectedActivitiesIntentService.setActivityDispatcher(null);

        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                getActivityDetectionPendingIntent());
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "Stopped tracking activities",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("ACT", "Failed to enable activity recognition.");
                Toast.makeText(mContext, "Failed to remove tracking activities",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void dispatchUserActivities(ArrayList<UserActivity> userActivities) {
        StringBuilder stringBuilder = new StringBuilder();

        for (UserActivity userActivity : userActivities) {
            stringBuilder.append("\n" + userActivity.getName());
        }

        TextView textView = findViewById(R.id.sample_data_textview);

        textView.setText(stringBuilder.toString());

    }
}
