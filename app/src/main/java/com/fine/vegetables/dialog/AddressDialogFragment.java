package com.fine.vegetables.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.model.SellerOrder;
import com.fine.vegetables.utils.Launcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择送货时间
 */

public class AddressDialogFragment extends CenterDialogFragment {

    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.phoneNumber)
    TextView mPhoneNumber;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.confirm)
    TextView mConfirm;


    private Unbinder mBind;
    private SellerOrder mOrder;

    public static AddressDialogFragment newInstance(SellerOrder order) {
        Bundle args = new Bundle();
        AddressDialogFragment fragment = new AddressDialogFragment();
        args.putParcelable(Launcher.EX_PAYLOAD, order);
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
        View view = inflater.inflate(R.layout.dialog_fragment_address, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        mUserName.setText(mOrder.getName());
        mPhoneNumber.setText(getString(R.string.phone_, mOrder.getPhone()));
        mAddress.setText(getString(R.string.address_, mOrder.getAddr()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @OnClick(R.id.confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
