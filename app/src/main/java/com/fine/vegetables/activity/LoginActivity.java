package com.fine.vegetables.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.view.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.phoneNumber) EditText phoneNumber;
    @BindView(R.id.phoneNumberClear) ImageView phoneNumberClear;
    @BindView(R.id.password) PasswordEditText password;
    @BindView(R.id.login) TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

}
