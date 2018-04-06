package com.project.activityrecognitionsample;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "DetectedActivitiesIS";
    private static ActivityDispatcher activityDispatcher;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     *
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
        ArrayList<UserActivity> userActivities = new ArrayList<>();

        // Log each activity.
        Log.i(TAG, "activities detected");
        for (DetectedActivity da : detectedActivities) {
            Log.i(TAG, da.getType() + " " + da.getConfidence() + "%");


            UserActivity userActivity = new UserActivity(getActivityName(da.getType()), da.getConfidence());
            userActivities.add(userActivity);
        }

        if (activityDispatcher != null)
            activityDispatcher.dispatchUserActivities(userActivities);
    }


    public static ActivityDispatcher getActivityDispatcher() {
        return activityDispatcher;
    }

    public static void setActivityDispatcher(ActivityDispatcher activityDispatcher) {
        DetectedActivitiesIntentService.activityDispatcher = activityDispatcher;
    }

    private String getActivityName(int TYPE) {
        switch (TYPE) {

            case DetectedActivity.IN_VEHICLE:
                return "Driving";

            case DetectedActivity.ON_BICYCLE:
                return "Cycling";


            case DetectedActivity.RUNNING:
                return "Running";

            case DetectedActivity.ON_FOOT:
                return "Walking";

            case DetectedActivity.STILL:
                return "Standing";

            default:
                return "Doing Nothing";
        }

    }
}