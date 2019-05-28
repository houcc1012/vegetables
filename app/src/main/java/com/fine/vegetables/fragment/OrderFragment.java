package com.fine.vegetables.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.fine.vegetables.R;
import com.fine.vegetables.activity.OrderDetailActivity;
import com.fine.vegetables.adapter.OrderAdapter;
import com.fine.vegetables.listener.OnCountChangeListener;
import com.fine.vegetables.listener.OnSeeDetailListener;
import com.fine.vegetables.model.Order;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Page;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.CustomSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderFragment extends BaseFragment implements AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnLoadMoreListener {

    public static final int ORDER_TYPE_ALL = 0;
    public static final int ORDER_TYPE_NO_SEND = 1;
    public static final int ORDER_TYPE_FINISHED = 2;

    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;

    private OrderAdapter orderAdapter;
    private List<Order> orders = new ArrayList<>();
    private int mPage;
    private int mOrderType;

    public static OrderFragment newInstance(int type) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(Launcher.EX_PAYLOAD, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrderType = getArguments().getInt(Launcher.EX_PAYLOAD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestOrders();
    }

    private void requestOrders() {
        Client.getUserOrderList(mOrderType, mPage, Client.PAGE_SIZE).setTag(TAG)
                .setCallback(new Callback2D<Resp<Page<List<Order>>>, Page<List<Order>>>() {

                    @Override
                    protected void onRespSuccessData(Page<List<Order>> page) {
                        updateOrders(page.getData());
                    }

                    @Override
                    public void onFailure(VolleyError volleyError) {
                        super.onFailure(volleyError);
                        stopRefreshAnimation();
                    }
                }).fireFree();
    }

    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        orderAdapter = new OrderAdapter(orders, getContext());
        orderAdapter.setOnSeeDetailListener(new OnSeeDetailListener() {
            @Override
            public void onClick(String id) {
                Launcher.with(getActivity(), OrderDetailActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, id)
                        .execute();
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(orderAdapter);
    }

    private void updateOrders(List<Order> data) {
        stopRefreshAnimation();
        if (mPage == 0) {
            orderAdapter.clear();
        }
        orderAdapter.add(data);
        if (data.size() >= Client.PAGE_SIZE) {
            mPage++;
        }
        orderAdapter.notifyDataSetChanged();

        if (orderAdapter.getOrderList().isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (getActivity() instanceof OnCountChangeListener) {
            ((OnCountChangeListener) getActivity()).change(orderAdapter.getItemCount());
        }

    }

    private void stopRefreshAnimation() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mSwipeRefreshLayout.isLoading()) {
            mSwipeRefreshLayout.setLoading(false);
        }
    }

    private void reset() {
        mPage = 0;
        mSwipeRefreshLayout.setLoadMoreEnable(true);
    }

    @Override
    public void onRefresh() {
        reset();
        requestOrders();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition =
                (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
        mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
    }

    @Override
    public void onLoadMore() {
        requestOrders();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
