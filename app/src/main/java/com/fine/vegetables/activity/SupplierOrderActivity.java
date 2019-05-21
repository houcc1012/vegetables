package com.fine.vegetables.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.fine.vegetables.R;
import com.fine.vegetables.fragment.SupplierHistoryOrderFragment;
import com.fine.vegetables.fragment.SupplierNoDeliveryOrderFragment;
import com.fine.vegetables.utils.Display;
import com.fine.vegetables.view.slidingTab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SupplierPagesAdapter supplierPagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_supplier);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0, false);
        supplierPagesAdapter = new SupplierPagesAdapter(getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(supplierPagesAdapter);
//        viewPager.addOnPageChangeListener(this);

        tabLayout.setDistributeEvenly(true);
        tabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        tabLayout.setSelectedIndicatorPadding(Display.dp2Px(60, getResources()));
        tabLayout.setPadding(Display.dp2Px(13, getResources()));
        tabLayout.setViewPager(viewPager);
//        mTitleBar.setOnTitleBarClickListener(this);
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
                    return mContext.getString(R.string.no_send_order);
                case 1:
                    return mContext.getString(R.string.history_order);
            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SupplierNoDeliveryOrderFragment();
                case 1:
                    return new SupplierHistoryOrderFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        private Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }

}
