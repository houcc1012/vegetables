package com.fine.vegetables.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.contract)
    TextView contract;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userAccount)
    TextView userAccount;
    @BindView(R.id.myOrder)
    TextView myOrder;
    @BindView(R.id.waitSend)
    View waitSend;
    @BindView(R.id.finished)
    View finished;
    @BindView(R.id.index)
    View index;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestOrder();
    }

    private void requestOrder() {
    }

    @OnClick({R.id.contract, R.id.logout, R.id.myOrder, R.id.waitSend, R.id.finished})
    public void onViewClicked(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
