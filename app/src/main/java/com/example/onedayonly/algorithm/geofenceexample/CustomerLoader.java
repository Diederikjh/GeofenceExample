package com.example.onedayonly.algorithm.geofenceexample;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class CustomerLoader {

    public static final String CUSTOMER_TAG_NAME = "customer";
    private static final String LOG_TAG = CustomerLoader.class.getName();

    public void loadCustomersFromXMLFile(Context context) throws XmlPullParserException, IOException {
        Resources res = context.getResources();
        XmlResourceParser xmlPullParser = res.getXml(R.xml.out);

        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xmlPullParser.getName().equals(CUSTOMER_TAG_NAME)) {
                    onCustomerElementFound(xmlPullParser);
                }
            }
            eventType = xmlPullParser.next();
        }
    }

    private void onCustomerElementFound(XmlResourceParser xmlPullParser) {

        String namespace = null;

        int id = xmlPullParser.getAttributeIntValue(namespace, "id", 0);
        String name = xmlPullParser.getAttributeValue(namespace, "name");

        double latitude = xmlPullParser.getAttributeFloatValue(namespace, "latitude", 0F);
        double longitude = xmlPullParser.getAttributeFloatValue(namespace, "longitude", 0F);
        double accuracy = xmlPullParser.getAttributeFloatValue(namespace, "accuracy", 0F);

        onCustomerLocationFound(id, name, latitude, longitude, accuracy);
    }

    private void onCustomerLocationFound(int id, String name, double latitude, double longitude, double accuracy) {

        // TODO make amazing things happen here!
        Log.i(LOG_TAG, String.format("Found new customer %s", name));

    }

}
