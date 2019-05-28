package com.fine.vegetables.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CommodityInventoryAdapter;
import com.fine.vegetables.model.CartItem;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommodityInventoryActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.totalAmount)
    AppCompatTextView totalAmount;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<CartItem> mCartItems;
    private CommodityInventoryAdapter mCommodityInventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_order_list);
        ButterKnife.bind(this);
        mCartItems = (List<CartItem>) getIntent().getSerializableExtra(Launcher.EX_PAYLOAD);
        initView();
    }

    private void initView() {
        totalAmount.setText(getString(R.string.total_, String.valueOf(mCartItems.size())));
        mCommodityInventoryAdapter = new CommodityInventoryAdapter(mCartItems, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(mCommodityInventoryAdapter);

    }

}
