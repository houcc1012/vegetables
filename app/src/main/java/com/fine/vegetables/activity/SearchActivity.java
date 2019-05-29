package com.fine.vegetables.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CommodityAdapter;
import com.fine.vegetables.listener.OnAddCartListener;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Page;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.KeyBoardUtils;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ValidationWatcher;
import com.fine.vegetables.view.CustomSwipeRefreshLayout;
import com.fine.vegetables.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.search)
    EditText mSearch;
    @BindView(R.id.cartCount)
    TextView mCartCount;
    @BindView(R.id.cart)
    ImageView mCart;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RefreshRecyclerView mRecyclerView;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;


    private CommodityAdapter mCommodityListAdapter;
    private List<Commodity> mCommodityList = new ArrayList<>();
    private int mPage;
    private String name;

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
            int count = Cart.get().getCount();
            if (count == 0) {
                mCartCount.setVisibility(View.INVISIBLE);
            } else {
                mCartCount.setVisibility(View.VISIBLE);
                if (count <= 999) {
                    mCartCount.setText(String.valueOf(count));
                } else {
                    mCartCount.setText("999");
                }
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
        frameLayout = createAnimLayout();
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
            if (count <= 999) {
                mCartCount.setText(String.valueOf(count));
            } else {
                mCartCount.setText("999");
            }
        }
        mSearch.addTextChangedListener(mSearchValidationWatcher);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mCommodityListAdapter = new CommodityAdapter(mCommodityList, getActivity());
        mCommodityListAdapter.setOnAddCartListener(new OnAddCartListener() {
            @Override
            public void onClick(Drawable drawable, int[] startLocation) {
                startCartAnim(drawable, startLocation);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mCommodityListAdapter);
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (KeyBoardUtils.isSHowKeyboard(mSearch)) {
                    mSearch.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyBoardUtils.closeKeyboard(mSearch);
                        }
                    }, 300);
                }

            }
        });
        mRecyclerView.setOnLoadMoreListener(new RefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMoreListener() {
                requestCommodities(name);
            }
        });

    }

    private ValidationWatcher mSearchValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            mPage = 0;
            name = s.toString();
            if (TextUtils.isEmpty(name)) {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                requestCommodities(name);
                mRecyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
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
            mRecyclerView.setLoadMoreEnable(true);
            mPage++;
        }
        mRecyclerView.notifyData();
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
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mRecyclerView.setLoadMoreEnable(true);
        requestCommodities(name);
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
        mCart.getLocationInWindow(end_location);
        int endX = end_location[0] - mCart.getWidth() / 2 - startLocation[0];
        int endY = end_location[1] - startLocation[1];

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
