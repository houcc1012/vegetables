package com.fine.vegetables.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.OrderDetailPushAdapter;
import com.fine.vegetables.model.Order;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailPushActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.expectMoney)
    TextView expectMoney;
    @BindView(R.id.sumAmount)
    AppCompatTextView sumAmount;

    private List<Order.DataBean> mDataList = new ArrayList<>();
    private OrderDetailPushAdapter mOrderDetailAdapter;
    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_push);
        ButterKnife.bind(this);
        mOrderId = getIntent().getStringExtra(Launcher.EX_PAYLOAD);
        initView();
    }

    private void initView() {
        mOrderDetailAdapter = new OrderDetailPushAdapter(mDataList, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(mOrderDetailAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        requestOrderDetails();
    }

    private void requestOrderDetails() {
        Client.getUserOrderDetail(mOrderId).setTag(TAG)
                .setCallback(new Callback2D<Resp<Order>, Order>() {

                    @Override
                    protected void onRespSuccessData(Order data) {
                        updateOrder(data);
                    }
                }).fireFree();
    }

    private void updateOrder(Order data) {
        expectMoney.setText(getString(R.string.yuan_symbol_, String.valueOf(data.getTotalMoney())));
        sumAmount.setText(getString(R.string.total_, String.valueOf(data.getList().size())));
        mOrderDetailAdapter.clear();
        mOrderDetailAdapter.add(data.getList());
        mOrderDetailAdapter.notifyDataSetChanged();
    }
}
