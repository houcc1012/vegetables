package com.fine.vegetables.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fine.vegetables.R;
import com.fine.vegetables.fragment.CartFragment;
import com.fine.vegetables.fragment.CategoryFragment;
import com.fine.vegetables.fragment.HomeFragment;
import com.fine.vegetables.fragment.MineFragment;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.view.BottomTabs;
import com.fine.vegetables.view.ScrollableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ScrollableViewPager mViewPager;
    @BindView(R.id.bottomTabs)
    BottomTabs mBottomTabs;

    private MainFragmentsAdapter mMainFragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mMainFragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainFragmentsAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setScrollable(true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomTabs.selectTab(position);
                if (LocalUser.getUser().isLogin()) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0);

        mBottomTabs.setOnTabClickListener(new BottomTabs.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                mBottomTabs.selectTab(position);
                mViewPager.setCurrentItem(position, false);
            }
        });
    }


    private static class MainFragmentsAdapter extends FragmentPagerAdapter {

        FragmentManager mFragmentManager;

        public MainFragmentsAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new CategoryFragment();
                case 2:
                    return new CartFragment();
                case 3:
                    return new MineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
