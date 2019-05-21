package com.fine.vegetables;

import android.app.Application;
import android.content.Context;

import com.fine.httplib.CookieManger;
import com.fine.vegetables.net.API;


public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        API.init(sContext.getCacheDir());
        CookieManger.getInstance().init(sContext.getFilesDir());
        handleUncaughtException();
    }

    private void handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
//                Preference.get().setForeground(false);
                System.exit(1);
            }
        });
    }

    public static Context getAppContext() {
        return sContext;
    }

}
