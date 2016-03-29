package com.example.onedayonly.algorithm.geofenceexample;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String LOG_TAG = GeofenceTransitionsIntentService.class.getName();
    private static final int NOTIFICATION_ID = 2358;
    private NotificationManager mNotificationManager;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    // More from http://developer.android.com/training/location/geofencing.html
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "GeofenceTransitionsIntentService");
        if (intent != null) {

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                String errorMessage = GeofenceErrorMessages.getErrorString(this,
                        geofencingEvent.getErrorCode());
                Log.e(LOG_TAG, errorMessage);
                return;
            }

            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences( );

                // Get the transition details as a String.
                String geofenceTransitionDetails = getGeofenceTransitionDetails(
                        geofenceTransition,
                        triggeringGeofences
                );

                // Send notification and log the transition details.
                sendNotification(geofenceTransitionDetails);
                Log.i(LOG_TAG, geofenceTransitionDetails);
            } else {
                // Log the error.
                Log.e(LOG_TAG, getString(R.string.geofence_transition_invalid_type,
                        geofenceTransition));
            }
        }
    }

    private void sendNotification(String geofenceTransitionDetails) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                        .setContentTitle("Geofence notification")
                        .setContentText(geofenceTransitionDetails);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences) {
        StringBuilder builder = new StringBuilder();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.append("Entered");
        }
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.append("Exited");
        }
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            builder.append("Dwell");
        }

        List<String> fenceNames = new ArrayList<>();
        for (Geofence fence : triggeringGeofences) {
            fenceNames.add(fence.getRequestId());
        }

        builder.append(" ");
        builder.append(TextUtils.join(",", fenceNames));

        return builder.toString();
    }

}
