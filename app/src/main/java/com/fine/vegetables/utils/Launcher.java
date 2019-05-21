package com.fine.vegetables.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Launcher {
    private static final String TAG = "Launcher";

    public static final String EX_PAYLOAD = "payload";
    public static final String EX_PAYLOAD_1 = "payload1";
    public static final String EX_PAYLOAD_2 = "payload2";
    public static final String EX_PAYLOAD_3 = "payload3";
    public static final String EX_PAYLOAD_4 = "payload4";

    private static Launcher sInstance;

    private Context mContext;
    private Intent mIntent;
    private Class<?> mClass;

    private Launcher() {
        mIntent = new Intent();
    }

    public static Launcher with(Context context, Class<?> clazz) {
        sInstance = new Launcher();
        sInstance.mContext = new WeakReference<>(context).get();
        sInstance.mIntent.setClass(sInstance.mContext, clazz);
        sInstance.mClass = clazz;
        return sInstance;
    }

    public Launcher putExtra(String key, ArrayList<? extends Parcelable> value) {
        mIntent.putParcelableArrayListExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, Parcelable parcelable) {
        mIntent.putExtra(key, parcelable);
        return this;
    }

    public Launcher putExtra(String key, int value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, long value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, double value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, String value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, boolean value) {
        mIntent.putExtra(key, value);
        return this;
    }

    /**
     * @param key
     * @param data
     * @return
     * @deprecated
     */
    public Launcher putExtra(String key, Serializable data) {
        mIntent.putExtra(key, data);
        return this;
    }

    public Launcher putExtra(String key, String[] value) {
        mIntent.putExtra(key, value);
        return this;
    }

    public Launcher putExtra(String key, Bundle bundle) {
        mIntent.putExtra(key, bundle);
        return this;
    }

    public Launcher setFlags(int flag) {
        mIntent.setFlags(flag);
        return this;
    }

    public Launcher addFlags(int flag) {
        mIntent.addFlags(flag);
        return this;
    }

    public void execute() {
        if (mContext != null) {
            mContext.startActivity(mIntent);
            mClass = null;
            mContext = null;
            mIntent = null;
        }
    }

    public void executeForResult(int requestCode) {
        if (mContext != null) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(mIntent, requestCode);
                mClass = null;
                mContext = null;
                mIntent = null;
            }
        }
    }
}
