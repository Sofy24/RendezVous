package com.example.rendezvous;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;
    private static Activity currentActivity;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }



    public static Context getContext() {
        return context;
    }

    public static void setContext(Context mContext) {
        context = mContext;
    }
}
