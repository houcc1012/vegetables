package com.fine.vegetables.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CommodityAdapter;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Page;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ValidationWatcher;
import com.fine.vegetables.view.CustomSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.search)
    EditText mSearch;
    @BindView(R.id.cartCount)
    TextView mCartCount;
    @BindView(R.id.cart)
    ImageView mCart;
    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;


    private CommodityAdapter mCommodityListAdapter;
    private List<Commodity> mCommodityList = new ArrayList<>();
    private int mPage;
    private String name;
    private OnCartChangeListener mOnCartChangeListener = new OnCartChangeListener() {
        @Override
        public void onCartChange() {
            int count = Cart.get().getCount();
            if (count == 0) {
                mCartCount.setVisibility(View.INVISIBLE);
            } else {
                mCartCount.setVisibility(View.VISIBLE);
                mCartCount.setText(String.valueOf(Cart.get().getCount()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.get().unRegisterOnCartChangeListener(mOnCartChangeListener);
    }

    private void initView() {
        Cart.get().registerOnCartChangeListener(mOnCartChangeListener);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.with(getActivity(), CartActivity.class)
                        .execute();
            }
        });
        int count = Cart.get().getCount();
        if (count > 0) {
            mCartCount.setVisibility(View.VISIBLE);
            mCartCount.setText(String.valueOf(count));
        }
        mSearch.addTextChangedListener(mSearchValidationWatcher);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnLoadMoreListener(this);
        mCommodityListAdapter = new CommodityAdapter(mCommodityList, getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mCommodityListAdapter);

    }

    private ValidationWatcher mSearchValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            mPage = 0;
            name = s.toString();
            requestCommodities(name);
        }
    };

    private void requestCommodities(String name) {
        if (TextUtils.isEmpty(name)) return;
        Client.getCommodityList(name, mPage, Client.PAGE_SIZE).setTag(TAG)
                .setCallback(new Callback2D<Resp<Page<List<Commodity>>>, Page<List<Commodity>>>() {

                    @Override
                    protected void onRespSuccessData(Page<List<Commodity>> page) {
                        updateCommodities(page.getData());
                    }

                    @Override
                    public void onFailure(VolleyError volleyError) {
                        super.onFailure(volleyError);
                        stopRefreshAnimation();
                    }
                }).fireFree();

    }

    private void updateCommodities(List<Commodity> data) {
        stopRefreshAnimation();
        if (mPage == 0) {
            mCommodityListAdapter.clear();
        }
        mCommodityListAdapter.add(data);
        if (data.size() >= Client.PAGE_SIZE) {
            mSwipeRefreshLayout.setLoadMoreEnable(true);
            mPage++;
        }
        mCommodityListAdapter.notifyDataSetChanged();
        if (mCommodityListAdapter.getCommodityList().isEmpty()) {
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
        requestCommodities(name);
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
        requestCommodities(name);
    }
}
