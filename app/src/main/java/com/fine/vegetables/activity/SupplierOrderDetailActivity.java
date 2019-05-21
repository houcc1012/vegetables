package com.fine.vegetables.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.SupplierOrderDetailAdapter;
import com.fine.vegetables.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderDetailActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.sumAmount)
    TextView sumAmount;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SupplierOrderDetailAdapter supplierOrderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }
}
