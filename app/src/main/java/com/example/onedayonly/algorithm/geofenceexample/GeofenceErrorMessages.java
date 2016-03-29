package com.example.onedayonly.algorithm.geofenceexample;

import android.content.Context;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Created by drbergie on 2016/03/29.
 */
public class GeofenceErrorMessages {
    public static String getErrorString(Context context, int errorCode) {

        switch (errorCode)
        {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return context.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return context.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return context.getString(R.string.geofence_too_many_pending_intents);
        }

        return null;
    }
}
