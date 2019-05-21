package com.fine.vegetables.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CartListAdapter;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    Unbinder unbinder;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.totalLayout)
    View totalLayout;
    @BindView(R.id.selectAll)
    TextView selectAll;
    @BindView(R.id.totalPrice)
    TextView totalPrice;
    @BindView(R.id.totalAmount)
    TextView totalAmount;
    @BindView(R.id.submit)
    TextView submit;

    private CartListAdapter mCartListAdapter;
    private List<Cart> mCartList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mCartListAdapter = new CartListAdapter(mCartList, getActivity());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mCartListAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        requestCart();
    }

    private void requestCart() {
    }

    private void updateCommodities() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        requestCart();
    }
}
