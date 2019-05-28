package com.fine.vegetables.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fine.vegetables.R;
import com.fine.vegetables.fragment.OrderFragment;
import com.fine.vegetables.listener.OnCountChangeListener;
import com.fine.vegetables.utils.Display;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.ScrollableViewPager;
import com.fine.vegetables.view.TitleBar;
import com.fine.vegetables.view.slidingTab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements OnCountChangeListener {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ScrollableViewPager viewPager;
    private SupplierPagesAdapter supplierPagesAdapter;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initData(getIntent());
        initView();
    }

    private void initData(Intent intent) {
        currentItem = intent.getIntExtra(Launcher.EX_PAYLOAD, 0);
    }


    private void initView() {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0, false);
        supplierPagesAdapter = new SupplierPagesAdapter(getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(supplierPagesAdapter);
        viewPager.setScrollable(true);
        viewPager.setCurrentItem(currentItem);

        tabLayout.setDistributeEvenly(true);
        tabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getActivity(), R.color.green));
        tabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        tabLayout.setSelectedIndicatorPadding(Display.dp2Px(40, getResources()));
        tabLayout.setPadding(Display.dp2Px(13, getResources()));
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void change(int nowCount) {
//        tabLayout.setRedPointVisibility(1, nowCount > 0 ? View.VISIBLE : View.GONE);
    }

    static class SupplierPagesAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;

        private SupplierPagesAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.all);
                case 1:
                    return mContext.getString(R.string.wait_send);
                case 2:
                    return mContext.getString(R.string.finished);
            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OrderFragment.newInstance(OrderFragment.ORDER_TYPE_ALL);
                case 1:
                    return OrderFragment.newInstance(OrderFragment.ORDER_TYPE_NO_SEND);
                case 2:
                    return OrderFragment.newInstance(OrderFragment.ORDER_TYPE_FINISHED);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        private Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
