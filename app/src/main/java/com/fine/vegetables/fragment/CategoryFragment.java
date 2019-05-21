package com.fine.vegetables.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CategoryAdapter;
import com.fine.vegetables.adapter.MenuListAdapter;
import com.fine.vegetables.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.lvMenu)
    ListView lvMenu;
    @BindView(R.id.lvCategory)
    ListView lvCategory;
    @BindView(R.id.title)
    TextView title;

    private List<String> menuList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private List<Integer> showTitle;
    private MenuListAdapter menuAdapter;
    private CategoryAdapter categoryAdapter;
    private int currentItem;

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
        menuAdapter = new MenuListAdapter(getContext(),menuList);
        lvMenu.setAdapter(menuAdapter);
        categoryAdapter = new CategoryAdapter(getContext(),categoryList);
        lvCategory.setAdapter(categoryAdapter);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                title.setText(menuList.get(position));
                lvCategory.setSelection(showTitle.get(position));
            }
        });
        lvCategory.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    title.setText(menuList.get(currentItem));
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
