package com.mobile_computing;

import android.app.Application;

public class MobileComputingApplication extends Application {

    private static MobileComputingApplication instance;
    public static final String PREFERENCE_FILE_NAME = "MOBILE_COMPUTING_PREFERENCE";

    public static MobileComputingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
