package com.fine.vegetables.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.activity.BaseActivity;
import com.fine.vegetables.model.SellerOrder;
import com.fine.vegetables.net.Callback;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择送货时间
 */

public class ConfirmOrderDialogFragment extends CenterDialogFragment {


    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.confirmFund)
    TextView confirmFund;
    @BindView(R.id.shouldFund)
    TextView shouldFund;
    @BindView(R.id.shouldLayout)
    LinearLayout shouldLayout;
    @BindView(R.id.actualFund)
    EditText actualFund;
    @BindView(R.id.actualLayout)
    LinearLayout actualLayout;
    @BindView(R.id.confirm)
    TextView confirm;
    private Unbinder mBind;
    private String mPhoneNumber;

    private SellerOrder mOrder;

    public static ConfirmOrderDialogFragment newInstance(SellerOrder order) {
        Bundle args = new Bundle();
        args.putParcelable(Launcher.EX_PAYLOAD, order);
        ConfirmOrderDialogFragment fragment = new ConfirmOrderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrder = getArguments().getParcelable(Launcher.EX_PAYLOAD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_confirm_order, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }


    private void initView() {
        userName.setText(mOrder.getName());
        shouldFund.setText(getString(R.string.yuan_symbol_, mOrder.getActualMoney()));
        actualFund.setText(mOrder.getActualMoney());
    }

    @OnClick({R.id.close, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                this.dismiss();
                break;
            case R.id.confirm:
                String actualMoney = actualFund.getText().toString();
                if (TextUtils.isEmpty(actualMoney)) {
                    ToastUtil.show("请填写实收金额");
                    return;
                }
                requestConfirmOrder(actualMoney);
                this.dismiss();
                break;
        }
    }

    private void requestConfirmOrder(String actualMoney) {
        Client.confirmOrder(mOrder.getOrderId(), actualMoney).setTag(TAG)
                .setCallback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        if (resp.isSuccess()) {
                            sendSuccessBroadcast(getActivity());
                        }
                    }
                }).fireFree();
    }

    private void sendSuccessBroadcast(FragmentActivity activity) {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.ACTION_ORDER_MODIFY);
        intent.putExtra(Launcher.EX_PAYLOAD, mOrder.getOrderId());
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }

}
