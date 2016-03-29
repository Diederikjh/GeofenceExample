package com.example.onedayonly.algorithm.geofenceexample;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final float GEOFENCE_RADIUS_M = 100;
    private static final long GEOFENCE_EXPIRATION_MS = 10 * 60 * 1000 ;  // 10 min
    public static final int REQUEST_CODE = 654;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new MyConnectionCallbacks())
                    .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                    .addApi(LocationServices.API)
                    .build();
        }

        mGeofenceList = new ArrayList<Geofence>();

        createGeofenceList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    // From http://developer.android.com/training/location/geofencing.html
    private void onConnectedToPlayServices() {

        Log.i(LOG_TAG, "Adding geofence!");

        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(new StatusResultCallback());

    }

    private void createGeofenceList() {
        String geofenceID = "EMSS1";

        double emssHQLatitude = -33.9655475;
        double emssHQLongitude = 18.8358019;

        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(geofenceID)

                .setCircularRegion(
                        emssHQLatitude,
                        emssHQLongitude,
                        GEOFENCE_RADIUS_M
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_MS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, REQUEST_CODE, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    // Not actually used, but might be useful for removing gefences.
    private void removeGeofenc() {
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                // This is the same pending intent that was used in addGeofences().
                getGeofencePendingIntent()
        ).setResultCallback(new StatusResultCallback()); // Result processed in onResult().
    }

    private static class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(LOG_TAG, "Failed to connect");
        }
    }

    private static class StatusResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(Status status) {
            Log.i(LOG_TAG, "Result received");
        }
    }

    private class MyConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(LOG_TAG, "Connection established");
            MainActivity.this.onConnectedToPlayServices();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e(LOG_TAG, "Connection suspended");
        }
    }
}
