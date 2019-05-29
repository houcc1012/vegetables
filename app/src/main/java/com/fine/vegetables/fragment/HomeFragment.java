package com.fine.vegetables.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.fine.vegetables.R;
import com.fine.vegetables.activity.MainActivity;
import com.fine.vegetables.activity.SearchActivity;
import com.fine.vegetables.adapter.CommodityAdapter;
import com.fine.vegetables.listener.OnAddCartListener;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Page;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.KeyBoardUtils;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.RefreshRecyclerView;
import com.fine.vegetables.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    Unbinder unbinder;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RefreshRecyclerView mRecyclerView;

    private CommodityAdapter mCommodityListAdapter;
    private List<Commodity> mCommodityList = new ArrayList<>();
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        mCommodityListAdapter = new CommodityAdapter(mCommodityList, getActivity());
        mCommodityListAdapter.setOnAddCartListener(new OnAddCartListener() {
            @Override
            public void onClick(Drawable drawable, int[] startLocation) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).startCartAnim(drawable, startLocation);
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mCommodityListAdapter);
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setOnLoadMoreListener(new RefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMoreListener() {
                requestCommodities();
            }
        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (KeyBoardUtils.isSHowKeyboard(recyclerView)) {
//                    recyclerView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeyBoardUtils.closeKeyboard(recyclerView);
//                        }
//                    }, 300);
//                }
//
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requestCommodities();
    }

    private void requestCommodities() {
        Client.getCommodityList(null, mPage, Client.PAGE_SIZE).setTag(TAG)
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
            mRecyclerView.setLoadMoreEnable(true);
            mPage++;
        }
        mRecyclerView.notifyData();
        mCommodityListAdapter.notifyDataSetChanged();

    }

    private void stopRefreshAnimation() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick(R.id.titleBar)
    public void onViewClicked(View view) {
        Launcher.with(getActivity(), SearchActivity.class)
                .execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRefresh() {
        mPage = 0;
        mRecyclerView.setLoadMoreEnable(true);
        requestCommodities();
    }
}

