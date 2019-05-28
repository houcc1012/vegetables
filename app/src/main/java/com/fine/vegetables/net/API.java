package com.fine.vegetables.net;

import android.text.TextUtils;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fine.httplib.ApiCallback;
import com.fine.httplib.ApiHeaders;
import com.fine.httplib.ApiIndeterminate;
import com.fine.httplib.ApiParams;
import com.fine.httplib.CookieManger;
import com.fine.httplib.GsonRequest;
import com.fine.httplib.RequestManager;
import com.fine.vegetables.BuildConfig;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class API extends RequestManager {
    private static String HOST = BuildConfig.HOST;

    public static String getMainPage() {
        return HOST;
    }

    public static void setHost(String newHost) {
        HOST = newHost;
    }

    public static String getDomain() {
        return HOST;
    }

    private static Set<String> sCurrentUrls = new HashSet<>();

    private int mMethod;
    private String mTag;
    private String mUri;
    private ApiCallback<?> mCallback;
    private ApiParams mApiParams;
    private ApiIndeterminate mIndeterminate;
    private RetryPolicy mRetryPolicy;
    private String mBody;

    public API(String uri) {
        this(Request.Method.GET, uri, null, 0);
    }

    public API(String uri, ApiParams apiParams) {
        this(Request.Method.GET, uri, apiParams, 0);
    }

    public API(String uri, ApiParams apiParams, int version) {
        this(Request.Method.GET, uri, apiParams, version);
    }


    public API(int method, String uri) {
        this(method, uri, null, 0);

    }

    public API(int method, String uri, ApiParams apiParams) {
        this(method, uri, apiParams, 0);
    }

    public API(int method, String uri, ApiParams apiParams, int version) {
        this(method, uri, apiParams, null, version);
    }

    public API(int method, String uri, String jsonBody) {
        this(method, uri, null, jsonBody, 0);
    }

    public API(int method, String uri, ApiParams apiParams, String jsonBody, int version) {
        mUri = uri;
        mApiParams = apiParams;
        mMethod = method;
        mTag = "";
        mBody = jsonBody;
    }


    public API setTag(String tag) {
        this.mTag = tag;
        return this;
    }

    public API setIndeterminate(ApiIndeterminate apiProgress) {
        mIndeterminate = apiProgress;
        return this;
    }

    public API setCallback(ApiCallback<?> callback) {
        this.mCallback = callback;
        return this;
    }

    public API setRetryPolicy(RetryPolicy policy) {
        this.mRetryPolicy = policy;
        return this;
    }

    public void fire() {
        synchronized (sCurrentUrls) {
            String url = createUrl();

            if (sCurrentUrls.add(mTag + "#" + url)) {
                createThenEnqueue(url);
            }
        }
    }

    public void fireFree() {
        String url = createUrl();

        createThenEnqueue(url);
    }

    private String createUrl() {
        String url = new StringBuilder(getDomain()).append(mUri).toString();
        if (mMethod == Request.Method.GET && mApiParams != null) {
            url = url + mApiParams.toString();
            mApiParams = null;
        }
        return url;
    }


    private void createThenEnqueue(String url) {
        ApiHeaders headers = new ApiHeaders();
        String cookies = CookieManger.getInstance().getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            headers.put("Cookie", cookies);
        }

        Type type;
        if (mCallback != null) {
            mCallback.setUrl(url);
            mCallback.setOnFinishedListener(new RequestFinishedListener());
            mCallback.setTag(mTag);
            mCallback.setIndeterminate(mIndeterminate);
            mCallback.onStart();
            type = mCallback.getGenericType();

        } else { // create a default callback for handle request finish event
            mCallback = new ApiCallback<Object>() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "onReceiveResponse: result(default): " + o);
                }

                @Override
                public void onFailure(VolleyError volleyError) {
                    Log.d(TAG, "onFailure: error(default): " +
                            volleyError == null ? null : volleyError.toString());
                }
            };
            mCallback.setUrl(url);
            mCallback.setOnFinishedListener(new RequestFinishedListener());
            type = mCallback.getGenericType();
        }

        GsonRequest request
                = new GsonRequest(mMethod, url, headers, mApiParams, mBody, type, mCallback);
        request.setTag(mTag);

        if (mRetryPolicy != null) {
            request.setRetryPolicy(mRetryPolicy);
        }

        enqueue(request);
        Log.d(TAG, request.toString());
    }

    private static class RequestFinishedListener implements ApiCallback.onFinishedListener {

        public void onFinished(String tag, String url) {
            if (sCurrentUrls != null) {
                //Log.d(TAG, "onFinished: " + url);
                sCurrentUrls.remove(tag + "#" + url);
            }
        }
    }

    public static void cancel(String tag) {
        RequestManager.cancel(tag);
        Iterator<String> iterator = sCurrentUrls.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            if (str.startsWith(tag + "#")) {
                Log.d(TAG, "cancel: " + str);
                iterator.remove();
            }
        }
    }
}
