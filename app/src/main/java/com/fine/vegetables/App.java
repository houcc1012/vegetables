package com.fine.vegetables;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fine.httplib.CookieManger;
import com.fine.vegetables.activity.LoginActivity;
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
                Intent intent = new Intent(sContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public static Context getAppContext() {
        return sContext;
    }

}
