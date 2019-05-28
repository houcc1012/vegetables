package com.fine.vegetables.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.httplib.CookieManger;
import com.fine.vegetables.R;
import com.fine.vegetables.activity.BaseActivity;
import com.fine.vegetables.activity.LoginActivity;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.net.Callback;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择送货时间
 */

public class QuitDialogFragment extends CenterDialogFragment {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.quit)
    TextView quit;
    private Unbinder mBind;
    private String mUserAccount;

    public static QuitDialogFragment newInstance(String userAccount) {
        Bundle args = new Bundle();
        args.putString(Launcher.EX_PAYLOAD, userAccount);
        QuitDialogFragment fragment = new QuitDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserAccount = getArguments().getString(Launcher.EX_PAYLOAD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_quit, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        phone.setText(getString(R.string.login_account_, mUserAccount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }


    @OnClick({R.id.close, R.id.quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                this.dismiss();
                break;
            case R.id.quit:
                Client.logout().setTag(TAG)
                        .setCallback(new Callback<Resp<JsonObject>>() {
                            @Override
                            protected void onRespSuccess(Resp<JsonObject> resp) {
                                if (resp.isSuccess()) {
                                    LocalUser.getUser().logout();
                                    CookieManger.getInstance().clearRawCookies();
                                    startLoginActivity();
                                }
                            }
                        })
                        .fire();
                break;
        }
    }

    private void startLoginActivity() {
        Launcher.with(getActivity(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .execute();
        this.dismiss();
    }

}
