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

import com.fine.vegetables.R;
import com.fine.vegetables.activity.SearchActivity;
import com.fine.vegetables.adapter.CommodityListAdapter;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.utils.Launcher;
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
    RecyclerView mRecyclerView;

    private CommodityListAdapter mCommodityListAdapter;
    private List<Commodity> mCommodityList = new ArrayList<>();

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
        mCommodityListAdapter = new CommodityListAdapter(mCommodityList, getActivity());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mCommodityListAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        requestCommodities();
    }

    private void requestCommodities() {
    }

    private void updateCommodities() {

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
        requestCommodities();
    }
}
