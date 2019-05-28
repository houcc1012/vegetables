package com.fine.vegetables.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fine.vegetables.R;
import com.fine.vegetables.fragment.CartFragment;
import com.fine.vegetables.fragment.CategoryFragment;
import com.fine.vegetables.fragment.HomeFragment;
import com.fine.vegetables.fragment.MineFragment;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.LocalUser;
import com.fine.vegetables.model.UserInfo;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.view.BottomTabs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public static final int PAGE_CART = 2;
    public static final int PAGE_MINE = 3;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.bottomTabs)
    BottomTabs mBottomTabs;

    private View mCartView;
    private MainFragmentsAdapter mMainFragmentsAdapter;

    //动画执行时长
    private int animationDuration = 700;
    //正在执行的动画数量
    private int animationNumber = 0;
    //是否完成清理
    private boolean isClean;
    private FrameLayout frameLayout;
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //清除动画后留下的垃圾
                    try {
                        frameLayout.removeAllViews();
                    } catch (Exception e) {

                    }
                    isClean = false;
                    break;
                default:
                    break;
            }
        }
    };

    private OnCartChangeListener mOnCartChangeListener = new OnCartChangeListener() {
        @Override
        public void onCartChange() {
            mBottomTabs.setPointNum(PAGE_CART, Cart.get().getCount());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        if (LocalUser.getUser().getUserInfo() != null && LocalUser.getUser().getUserInfo().getType() == UserInfo.TYPE_SUPPLIER) {
            Launcher.with(getActivity(), SupplierOrderActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                    .execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.get().unRegisterOnCartChangeListener(mOnCartChangeListener);
    }

    public void goHomePage() {
        mViewPager.setCurrentItem(0);
    }

    private void initView() {
        Cart.get().registerOnCartChangeListener(mOnCartChangeListener);
        mBottomTabs.setPointNum(PAGE_CART, Cart.get().getCount());
        mCartView = mBottomTabs.getChildAt(2);
        frameLayout = createAnimLayout();
        mMainFragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainFragmentsAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomTabs.selectTab(position);
                if (position == PAGE_MINE) {
                    MineFragment mineFragment = (MineFragment) mMainFragmentsAdapter.getFragment(position);
                    if (mineFragment != null) {
                        mineFragment.requestNoSendOrder();
                    }
                }
                if (position == PAGE_CART) {
                    CartFragment cartFragment = (CartFragment) mMainFragmentsAdapter.getFragment(position);
                    if (cartFragment != null) {
                        cartFragment.updateCartData();
                    }
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

    public void startCartAnim(Drawable drawable, int[] startLocation) {
        if (isClean) {
            try {
                frameLayout.removeAllViews();
                isClean = false;
                setAnim(drawable, startLocation);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        } else {
            setAnim(drawable, startLocation);
        }
    }


    private void setAnim(Drawable drawable, int[] startLocation) {


        Animation mScaleAnimation = new ScaleAnimation(1.5f, 0.3f, 1.5f, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.1f);
        mScaleAnimation.setDuration(animationDuration);
        mScaleAnimation.setFillAfter(true);


        final ImageView iview = new ImageView(this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(frameLayout, iview, startLocation);
        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        mCartView.getLocationInWindow(end_location);
        int endX = end_location[0] + mCartView.getWidth() / 3 - startLocation[0];
        int endY = end_location[1] + 5 - startLocation[1];

        Animation mTranslateAnimation = new TranslateAnimation(0, endX, 0, endY);
//        Animation mRotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mRotateAnimation.setDuration(animationDuration);
        mTranslateAnimation.setDuration(animationDuration);
        AnimationSet mAnimationSet = new AnimationSet(true);

        mAnimationSet.setFillAfter(true);
//        mAnimationSet.addAnimation(mRotateAnimation);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mTranslateAnimation);

        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                animationNumber++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                animationNumber--;
                if (animationNumber == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

        });
        view.startAnimation(mAnimationSet);

    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                dip2px(this, 90), dip2px(this, 90));
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;
    }

    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        isClean = true;
        try {
            frameLayout.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
        super.onLowMemory();
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
