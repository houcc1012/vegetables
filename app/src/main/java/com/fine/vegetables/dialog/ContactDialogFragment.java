package com.fine.vegetables.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.activity.BaseActivity;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择送货时间
 */

public class ContactDialogFragment extends CenterDialogFragment {


    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.confirm)
    TextView confirm;
    private Unbinder mBind;
    private String mPhoneNumber;

    public static ContactDialogFragment newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString(Launcher.EX_PAYLOAD, phone);
        ContactDialogFragment fragment = new ContactDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoneNumber = getArguments().getString(Launcher.EX_PAYLOAD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_contact, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        phone.setText(getString(R.string.phone_, mPhoneNumber));
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

    @OnClick({R.id.close, R.id.cancel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.confirm:
                if (getActivity() instanceof BaseActivity){
                    ((BaseActivity) getActivity()).call(mPhoneNumber);
                }
                this.dismiss();
                break;
        }
    }

}
