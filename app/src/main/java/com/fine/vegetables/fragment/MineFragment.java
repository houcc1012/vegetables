package com.fine.vegetables.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fine.httplib.CookieManger;
import com.fine.vegetables.R;
import com.fine.vegetables.activity.LoginActivity;
import com.fine.vegetables.activity.OrderActivity;
import com.fine.vegetables.adapter.OrderAdapter;
import com.fine.vegetables.dialog.ContactDialogFragment;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.model.Order;
import com.fine.vegetables.model.UserInfo;
import com.fine.vegetables.net.Callback;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Page;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.SmartDialog;
import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.fine.vegetables.activity.BaseActivity.ACTION_LOGOUT_SUCCESS;

public class MineFragment extends BaseFragment {


    @BindView(R.id.contract)
    AppCompatTextView contract;
    @BindView(R.id.logout)
    AppCompatTextView logout;
    @BindView(R.id.avatar)
    AppCompatImageView avatar;
    @BindView(R.id.userName)
    AppCompatTextView userName;
    @BindView(R.id.userAccount)
    AppCompatTextView userAccount;
    @BindView(R.id.myOrder)
    AppCompatTextView myOrder;
    @BindView(R.id.index)
    View index;
    @BindView(R.id.waitSend)
    LinearLayout waitSend;
    @BindView(R.id.finished)
    LinearLayout finished;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         initView();
    }

    private void initView() {
        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        userName.setText(userInfo.getName());
        userAccount.setText(userInfo.getAccount());

    }

    public void requestNoSendOrder() {
        Client.getUserOrderList(Order.TYPE_NO_SEND, 0, 1).setTag(TAG)
                .setCallback(new Callback2D<Resp<Page<List<Order>>>, Page<List<Order>>>() {
                    @Override
                    protected void onRespSuccessData(Page<List<Order>> page) {
                        if (page.getData().isEmpty()) {
                            index.setVisibility(View.INVISIBLE);
                        } else {
                            index.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(VolleyError volleyError) {
                        super.onFailure(volleyError);
                    }
                }).fireFree();
    }

    private void requestLogout() {
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
    }


    private void startLoginActivity() {
        Launcher.with(getActivity(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .execute();
    }

    @OnClick({R.id.contract, R.id.logout, R.id.myOrder, R.id.waitSend, R.id.finished})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contract:
                Client.getSupplierPhone().setTag(TAG)
                        .setCallback(new Callback2D<Resp<String>, String>() {
                            @Override
                            protected void onRespSuccessData(String data) {
                                ContactDialogFragment.newInstance(data).show(getFragmentManager());
                            }
                        }).fireFree();
                break;
            case R.id.logout:
                SmartDialog.with(getActivity())
                        .setTitle(getString(R.string.confirm_logout))
                        .setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                dialog.dismiss();
                                requestLogout();
                            }
                        })
                        .setNegative(R.string.cancel, new SmartDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.myOrder:
                Launcher.with(getActivity(), OrderActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, OrderFragment.ORDER_TYPE_ALL)
                        .execute();
                break;
            case R.id.finished:
                Launcher.with(getActivity(), OrderActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, OrderFragment.ORDER_TYPE_FINISHED)
                        .execute();
                break;
            case R.id.waitSend:
                Launcher.with(getActivity(), OrderActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, OrderFragment.ORDER_TYPE_NO_SEND)
                        .execute();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
