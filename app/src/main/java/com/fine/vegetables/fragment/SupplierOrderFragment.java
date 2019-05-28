package com.fine.vegetables.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.fine.vegetables.activity.SupplierOrderDetailActivity;
import com.fine.vegetables.adapter.SupplierOrderAdapter;
import com.fine.vegetables.dialog.AddressDialogFragment;
import com.fine.vegetables.dialog.ConfirmOrderDialogFragment;
import com.fine.vegetables.listener.OnConfirmOrderClickListener;
import com.fine.vegetables.listener.OnSeeAddressClickListener;
import com.fine.vegetables.listener.OnSeeDetailListener;
import com.fine.vegetables.model.SellerOrder;
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

import static com.fine.vegetables.activity.BaseActivity.ACTION_ORDER_MODIFY;

public class SupplierOrderFragment extends BaseFragment implements AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;

    private SupplierOrderAdapter supplierOrderAdapter;
    private List<SellerOrder> orders = new ArrayList<>();
    private int mPage;
    private int mOrderType;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_ORDER_MODIFY.equalsIgnoreCase(intent.getAction())) {
                String orderId = intent.getStringExtra(Launcher.EX_PAYLOAD);
                reset();
                requestOrders();
            }
        }
    };


    public static SupplierOrderFragment newInstance(int type) {
        SupplierOrderFragment fragment = new SupplierOrderFragment();
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter(ACTION_ORDER_MODIFY));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        supplierOrderAdapter = new SupplierOrderAdapter(orders, getContext(), mOrderType);
        supplierOrderAdapter.setOnSeeDetailListener(new OnSeeDetailListener() {
            @Override
            public void onClick(String id) {
                Launcher.with(getActivity(), SupplierOrderDetailActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, id)
                        .execute();
            }
        });
        supplierOrderAdapter.setOnConfirmOrderClickListener(new OnConfirmOrderClickListener<SellerOrder>() {
            @Override
            public void click(SellerOrder sellerOrder) {
                if (sellerOrder.getConfirm() == 0) {
                    Launcher.with(getActivity(), SupplierOrderDetailActivity.class)
                            .putExtra(Launcher.EX_PAYLOAD, sellerOrder.getOrderId())
                            .execute();
                } else {
                    ConfirmOrderDialogFragment.newInstance(sellerOrder).show(getFragmentManager());
                }
            }
        });
        supplierOrderAdapter.setOnSeeAddressClickListener(new OnSeeAddressClickListener<SellerOrder>() {
            @Override
            public void click(SellerOrder sellerOrder) {
                AddressDialogFragment.newInstance(sellerOrder).show(getFragmentManager());
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(supplierOrderAdapter);
    }

    private void requestOrders() {
        Client.getSellerOrderList(mOrderType, mPage, Client.PAGE_SIZE).setTag(TAG)
                .setCallback(new Callback2D<Resp<Page<List<SellerOrder>>>, Page<List<SellerOrder>>>() {

                    @Override
                    protected void onRespSuccessData(Page<List<SellerOrder>> page) {
                        updateOrders(page.getData());
                    }

                    @Override
                    public void onFailure(VolleyError volleyError) {
                        super.onFailure(volleyError);
                        stopRefreshAnimation();
                    }
                }).fireFree();
    }


    @Override
    public void onResume() {
        super.onResume();
        requestOrders();
    }

    private void updateOrders(List<SellerOrder> data) {
        stopRefreshAnimation();
        if (mPage == 0) {
            supplierOrderAdapter.clear();
        }
        supplierOrderAdapter.add(data);
        if (data.size() >= Client.PAGE_SIZE) {
            mPage++;
        }
        supplierOrderAdapter.notifyDataSetChanged();
        if (supplierOrderAdapter.getOrderList().isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
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
}
