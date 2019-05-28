package com.fine.vegetables.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fine.vegetables.R;
import com.fine.vegetables.activity.MainActivity;
import com.fine.vegetables.activity.SearchActivity;
import com.fine.vegetables.adapter.CategoryCommodityAdapter;
import com.fine.vegetables.adapter.MenuListAdapter;
import com.fine.vegetables.listener.OnAddCartListener;
import com.fine.vegetables.model.Category;
import com.fine.vegetables.model.Commodity;
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
import butterknife.OnClick;
import butterknife.Unbinder;

public class CategoryFragment extends BaseFragment implements AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.lvMenu)
    ListView lvMenu;
    @BindView(R.id.lvCategory)
    ListView lvCommodity;
    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;

    private List<Category> menuList = new ArrayList<>();
    private List<Commodity> commodityList = new ArrayList<>();
    private MenuListAdapter menuAdapter;
    private CategoryCommodityAdapter commodityAdapter;
    private int mPage;
    private int currentGroupIndex;
    private Category category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mSwipeRefreshLayout.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        menuAdapter = new MenuListAdapter(getContext(), menuList);
        lvMenu.setAdapter(menuAdapter);
        commodityAdapter = new CategoryCommodityAdapter(getContext(), commodityList);
        commodityAdapter.setOnAddCartListener(new OnAddCartListener() {
            @Override
            public void onClick(Drawable drawable, int[] startLocation) {
                if (getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).startCartAnim(drawable,startLocation);
                }
            }
        });
        lvCommodity.setAdapter(commodityAdapter);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = (Category) parent.getItemAtPosition(position);
                if (currentGroupIndex != position) {
                    currentGroupIndex = position;
                    menuAdapter.setSelectItem(position);
                    menuAdapter.notifyDataSetInvalidated();
                    mPage = 0;
                    requestCommodities(category.getGroupId());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestGroups();
    }

    private void requestCommodities(Integer groupId) {
        Client.getCommodityList(groupId, mPage, Client.PAGE_SIZE).setTag(TAG)
                .setCallback(new Callback2D<Resp<Page<List<Commodity>>>, Page<List<Commodity>>>() {

                    @Override
                    protected void onRespSuccessData(Page<List<Commodity>> page) {
                        updateCommodities(page.getData());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopRefreshAnimation();
                    }
                }).fireFree();
    }

    private void updateCommodities(List<Commodity> data) {
        if (mPage == 0) {
            commodityAdapter.clear();
        }
        if (data.size() < Client.PAGE_SIZE) {
            mSwipeRefreshLayout.setLoading(false);
        } else {
            mPage++;
        }
        commodityAdapter.addList(data);
        commodityAdapter.notifyDataSetChanged();
    }

    private void requestGroups() {
        Client.getCategoryList().setTag(TAG)
                .setCallback(new Callback2D<Resp<List<Category>>, List<Category>>() {
                    @Override
                    protected void onRespSuccessData(List<Category> data) {
                        updateGroups(data);
                    }
                }).fireFree();
    }

    private void updateGroups(List<Category> data) {
        menuAdapter.addList(data);
        menuAdapter.notifyDataSetChanged();
        category = data.get(0);
        requestCommodities(category.getGroupId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        reset();
        requestCommodities(category.getGroupId());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition =
                (lvCommodity == null || lvCommodity.getChildCount() == 0) ? 0 : lvCommodity.getChildAt(0).getTop();
        mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
    }

    @Override
    public void onLoadMore() {
        requestCommodities(category.getGroupId());
    }

    private void reset() {
        mPage = 0;
        mSwipeRefreshLayout.setLoadMoreEnable(true);
    }

    @OnClick(R.id.titleBar)
    public void onViewClicked(View view) {
        Launcher.with(getActivity(), SearchActivity.class)
                .execute();
    }

    private void stopRefreshAnimation() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mSwipeRefreshLayout.isLoading()) {
            mSwipeRefreshLayout.setLoading(false);
        }
    }

}
