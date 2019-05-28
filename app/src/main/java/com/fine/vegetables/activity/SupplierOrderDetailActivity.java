package com.fine.vegetables.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.SupplierOrderDetailAdapter;
import com.fine.vegetables.listener.OnAmountChangeListener;
import com.fine.vegetables.model.SellerOrder;
import com.fine.vegetables.model.SubmitConfirmOrder;
import com.fine.vegetables.net.Callback;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ToastUtil;
import com.fine.vegetables.view.TitleBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupplierOrderDetailActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.amount)
    AppCompatTextView amount;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.expectMoney)
    TextView expectMoney;
    @BindView(R.id.actualMoney)
    TextView actualMoney;
    @BindView(R.id.finishedLayout)
    LinearLayout finishedLayout;
    @BindView(R.id.sumPrice)
    TextView sumPrice;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.confirmLayout)
    RelativeLayout confirmLayout;

    private SupplierOrderDetailAdapter supplierOrderDetailAdapter;
    private List<SellerOrder.DataBean> mDataList = new ArrayList<>();
    private String mOrderId;
    private SubmitConfirmOrder submitOrder = new SubmitConfirmOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_supplier);
        ButterKnife.bind(this);
        mOrderId = getIntent().getStringExtra(Launcher.EX_PAYLOAD);
        submitOrder.setOrderId(mOrderId);
        initView();
    }

    private void initView() {
        supplierOrderDetailAdapter = new SupplierOrderDetailAdapter(mDataList, getActivity());
        supplierOrderDetailAdapter.setOnAmountChangeListener(new OnAmountChangeListener() {
            @Override
            public void change() {
                updatePriceAndCount();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(supplierOrderDetailAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        requestOrderDetails();
    }

    @OnClick(R.id.confirm)
    public void onViewClicked() {
        List<SubmitConfirmOrder.DataBean> dataBeans = new ArrayList<>();
        List<SellerOrder.DataBean> orders = supplierOrderDetailAdapter.getDataList();
        for (SellerOrder.DataBean item : orders) {
            SubmitConfirmOrder.DataBean dataBean = new SubmitConfirmOrder.DataBean();
            dataBean.setCount(String.valueOf(item.getActualWeight()));
            dataBean.setId(String.valueOf(item.getId()));
            dataBeans.add(dataBean);
        }
        submitOrder.setData(dataBeans);
        Client.comfirmOrder(submitOrder).setTag(TAG)
                .setCallback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        if (resp.isSuccess()) {
                            ToastUtil.show("成功");
                            finish();
                        }
                    }
                }).fireFree();

    }

    private void updatePriceAndCount() {
        List<SellerOrder.DataBean> orders = supplierOrderDetailAdapter.getDataList();
        double totalPrice = 0.00;
        for (SellerOrder.DataBean item : orders) {
            totalPrice = item.getPrice() * item.getActualWeight() + totalPrice;
        }
        sumPrice.setText(getString(R.string.yuan_symbol_, BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
    }


    private void requestOrderDetails() {
        Client.getSupplierOrderDetail(mOrderId).setTag(TAG)
                .setCallback(new Callback2D<Resp<SellerOrder>, SellerOrder>() {

                    @Override
                    protected void onRespSuccessData(SellerOrder data) {
                        updateOrder(data);
                        sendSuccessBroadcast(getActivity());
                    }
                }).fireFree();
    }

    private void updateOrder(SellerOrder data) {
        //结束订单
        if (data.getStatus() == 2) {
            confirmLayout.setVisibility(View.GONE);
            finishedLayout.setVisibility(View.VISIBLE);
            expectMoney.setText(getString(R.string.expect_money_, String.valueOf(data.getTotalMoney())));
            actualMoney.setText(getString(R.string.yuan_symbol_, String.valueOf(data.getActualMoney())));
        } else {
            confirmLayout.setVisibility(View.VISIBLE);
            finishedLayout.setVisibility(View.GONE);
        }
        amount.setText(getString(R.string.total_, String.valueOf(data.getList().size())));
        supplierOrderDetailAdapter.setOrderType(data.getStatus());
        supplierOrderDetailAdapter.clear();
        supplierOrderDetailAdapter.add(data.getList());
        supplierOrderDetailAdapter.notifyDataSetChanged();
        updatePriceAndCount();
    }


    private void sendSuccessBroadcast(FragmentActivity activity) {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.ACTION_ORDER_MODIFY);
        intent.putExtra(Launcher.EX_PAYLOAD, mOrderId);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }
}
