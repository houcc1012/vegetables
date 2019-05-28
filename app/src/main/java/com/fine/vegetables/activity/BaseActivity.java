package com.fine.vegetables.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.fine.httplib.ApiIndeterminate;
import com.fine.httplib.CookieManger;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.net.API;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.SecurityUtil;
import com.fine.vegetables.utils.TimerHandler;
import com.fine.vegetables.utils.ToastUtil;
import com.fine.vegetables.view.RequestProgress;

import java.security.NoSuchAlgorithmException;

public class BaseActivity extends StatusBarActivity implements
        ApiIndeterminate, TimerHandler.TimerCallback {

    public static final String ACTION_TOKEN_EXPIRED = "com.sbai.fin.token_expired";
    public static final String ACTION_LOGIN_SUCCESS = "com.login_success";
    public static final String ACTION_LOGOUT_SUCCESS = "com.logout_success";
    public static final String ACTION_SELECT_TIME = "select_time";
    public static final String ACTION_ORDER_MODIFY = "order_modify";

    public static final String EX_TOKEN_EXPIRED_MESSAGE = "token_expired_msg";
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    private String mPhone;


    protected String TAG;

    private TimerHandler mTimerHandler;
    private RequestProgress mRequestProgress;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_TOKEN_EXPIRED.equalsIgnoreCase(intent.getAction())) {
                LocalUser.getUser().logout();
                CookieManger.getInstance().clearRawCookies();
                Launcher.with(getActivity(), LoginActivity.class).execute();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mRequestProgress = new RequestProgress(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                API.cancel(TAG);
            }
        });
    }

    private void scrollToTop(View view) {
        if (view instanceof AbsListView) {
            ((AbsListView) view).smoothScrollToPositionFromTop(0, 0);
        } else if (view instanceof RecyclerView) {
            ((RecyclerView) view).smoothScrollToPosition(0);
        } else if (view instanceof ScrollView) {
            ((ScrollView) view).smoothScrollTo(0, 0);
        }
    }

    protected void scrollToTop(View anchor, final View view) {
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop(view);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION_TOKEN_EXPIRED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        API.cancel(TAG);

        mRequestProgress.dismissAll();

        stopScheduleJob();
    }


    protected FragmentActivity getActivity() {
        return this;
    }

    @Override
    public void onHttpUiShow(String tag) {
        if (mRequestProgress != null) {
            mRequestProgress.show(this);
        }
    }

    @Override
    public void onHttpUiDismiss(String tag) {
        if (mRequestProgress != null) {
            mRequestProgress.dismiss();
        }
    }

    protected void startScheduleJob(int millisecond, long delayMillis) {
        stopScheduleJob();

        if (mTimerHandler == null) {
            mTimerHandler = new TimerHandler(this);
        }
        mTimerHandler.sendEmptyMessageDelayed(millisecond, delayMillis);
    }

    protected void startScheduleJob(int millisecond) {
        startScheduleJob(millisecond, 0);
    }

    protected void stopScheduleJob() {
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler.resetCount();
        }
    }

    @Override
    public void onTimeUp(int count) {
    }

    /**
     * md5 加密
     *
     * @param value
     * @return
     */
    protected String md5Encrypt(String value) {
        try {
            return SecurityUtil.md5Encrypt(value);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return value;
        }
    }


    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    ToastUtil.show("请允许拨号权限后再试");
                } else {
                    call(mPhone);
                }
                break;
        }
    }

    /**
     * 拨打电话（直接拨打）
     *
     * @param phone 电话
     */
    public void call(String phone) {
        mPhone = phone;
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }
}
