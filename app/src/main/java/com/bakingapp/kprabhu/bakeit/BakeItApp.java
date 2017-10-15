package com.bakingapp.kprabhu.bakeit;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by kprabhu on 9/17/17.
 */

public class BakeItApp extends Application {
    static BakeItApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = (BakeItApp) getApplicationContext();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static BakeItApp getContext() {
        return context;
    }
}
