package com.fine.vegetables.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.model.UserInfo;
import com.fine.vegetables.net.Callback;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ToastUtil;
import com.fine.vegetables.utils.ValidationWatcher;
import com.fine.vegetables.view.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.phoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.phoneNumberClear)
    ImageView mPhoneNumberClear;
    @BindView(R.id.password)
    PasswordEditText mPassword;
    @BindView(R.id.login)
    TextView mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(LocalUser.getUser().getPhone())) {
            mPhoneNumber.setText(LocalUser.getUser().getPhone());
            formatAccountNumber();
            mPhoneNumber.clearFocus();
            mPassword.requestFocus();
        }
        mPhoneNumber.addTextChangedListener(mPhoneValidationWatcher);
        mPassword.addTextChangedListener(mValidationWatcher);

        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhoneNumber.removeTextChangedListener(mPhoneValidationWatcher);
        mPassword.removeTextChangedListener(mValidationWatcher);
    }

    @OnClick({R.id.phoneNumberClear, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phoneNumberClear:
                mPhoneNumber.setText("");
                break;
            case R.id.login:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {

        final String phoneNumber = getPhoneNumber();
        String password = mPassword.getPassword();
        password = md5Encrypt(password);
        mLogin.setEnabled(false);
        Client.login(phoneNumber, password).setTag(TAG)
                .setCallback(new Callback<Resp<UserInfo>>() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mLogin.setEnabled(true);
                    }

                    @Override
                    protected void onRespSuccess(Resp<UserInfo> resp) {
                        LocalUser.getUser().setUserInfo(resp.getData(), phoneNumber);
                        if (resp.getData().getType() == UserInfo.TYPE_BUYER) {
                            Launcher.with(getActivity(), MainActivity.class)
                                    .execute();
                        } else {
                            Launcher.with(getActivity(), SupplierOrderActivity.class)
                                    .execute();
                        }
                        sendLoginSuccessBroadcast();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).fire();
    }

    private void initListener() {
        mPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!TextUtils.isEmpty(mPhoneNumber.getText().toString()) && hasFocus) {
                    mPhoneNumberClear.setVisibility(View.VISIBLE);
                } else {
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
                mPassword.requestFocus();
            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean enable = checkLoginButtonEnable();
            if (enable != mLogin.isEnabled()) {
                mLogin.setEnabled(enable);
            }
        }
    };


    private ValidationWatcher mPhoneValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            mValidationWatcher.afterTextChanged(s);

            formatAccountNumber();

            mPhoneNumberClear.setVisibility(checkClearBtnVisible() ? View.VISIBLE : View.INVISIBLE);
        }
    };

    private boolean checkClearBtnVisible() {
        String phone = mPhoneNumber.getText().toString();
        return !TextUtils.isEmpty(phone);
    }

    private String getPhoneNumber() {
        return mPhoneNumber.getText().toString().trim().replaceAll(" ", "");
    }

    private boolean checkLoginButtonEnable() {
        String phone = getPhoneNumber();
        String password = mPassword.getPassword();

        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return false;
        }

        return true;

    }

    private void formatAccountNumber() {
        String oldPhone = mPhoneNumber.getText().toString();
        String newPhone = oldPhone.replaceAll(" ", "");
        if (!newPhone.equalsIgnoreCase(oldPhone)) {
            mPhoneNumber.setText(newPhone);
            mPhoneNumber.setSelection(newPhone.length());
        }
    }

    private void sendLoginSuccessBroadcast() {
        LocalBroadcastManager.getInstance(getActivity())
                .sendBroadcast(new Intent(ACTION_LOGIN_SUCCESS));
    }


}
