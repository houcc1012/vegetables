package com.fine.vegetables.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.adapter.CartListAdapter;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.fine.vegetables.listener.OnSelectAmountChangeListener;
import com.fine.vegetables.listener.OnSelectCartListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.CartItem;
import com.fine.vegetables.utils.KeyBoardUtils;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ToastUtil;
import com.fine.vegetables.view.SmartDialog;
import com.fine.vegetables.view.TitleBar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.selectAll)
    AppCompatTextView selectAll;
    @BindView(R.id.totalPrice)
    AppCompatTextView mTotalPrice;
    @BindView(R.id.totalAmount)
    AppCompatTextView totalAmount;
    @BindView(R.id.submit)
    AppCompatTextView submit;
    @BindView(R.id.totalLayout)
    LinearLayout totalLayout;
    @BindView(R.id.empty)
    ImageView empty;
    @BindView(R.id.goShop)
    TextView goShop;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;
    private CartListAdapter mCartListAdapter;

    private OnCartChangeListener mOnCartChangeListener = new OnCartChangeListener() {
        @Override
        public void onCartChange() {
            if (Cart.get().getCartItems().isEmpty()) {
                emptyLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.VISIBLE);
                if (Cart.get().getCartItems().size() == Cart.get().getSelectedItems().size()) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.get().unRegisterOnCartChangeListener(mOnCartChangeListener);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateCartData();
    }

    private void initView() {
        Cart.get().registerOnCartChangeListener(mOnCartChangeListener);
        if (Cart.get().getCartItems().isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
        }
        titleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartDialog.with(getActivity())
                        .setTitle(getString(R.string.confirm_delect_selected_commodity))
                        .setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                dialog.dismiss();
                                deleteSelectedItems();
                            }
                        })
                        .setNegative(R.string.cancel, new SmartDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItem> cartItems = mCartListAdapter.getSelectedItems();
                if (cartItems.isEmpty()) {
                    ToastUtil.show(R.string.please_select_commodity_then_submit);
                } else {
                    Launcher.with(getActivity(), ConfirmOrderActivity.class)
                            .putExtra(Launcher.EX_PAYLOAD, (Serializable) cartItems)
                            .execute();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        mCartListAdapter = new CartListAdapter(Cart.get().getCartItems(), getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(mCartListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (KeyBoardUtils.isSHowKeyboard(recyclerView)) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyBoardUtils.closeKeyboard(recyclerView);
                        }
                    }, 300);
                }
            }
        });
        mCartListAdapter.setOnSelectCartListener(new OnSelectCartListener() {
            @Override
            public void select(CartItem cartItem) {
                updatePriceAndCount();
            }

            @Override
            public void unSelect(CartItem cartItem) {
                updatePriceAndCount();
            }
        });

        mCartListAdapter.setOnSelectAmountChangeListener(new OnSelectAmountChangeListener() {
            @Override
            public void change(CartItem cartItem) {
                Cart.get().broadcastCartChange();
                updatePriceAndCount();
            }
        });

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAll.isSelected()) {
                    selectAll.setSelected(false);
                    updateAll(false);
                } else {
                    selectAll.setSelected(true);
                    updateAll(true);
                }
            }
        });

    }

    private void deleteSelectedItems() {
        List<CartItem> cartItems = mCartListAdapter.getSelectedItems();
        Cart.get().removeItems(cartItems);
        updateDeleteBtn(false);
        totalAmount.setText(getString(R.string.total_, String.valueOf(0)));
        mTotalPrice.setText(String.valueOf(0.00));
        selectAll.setSelected(false);
        mCartListAdapter.remove(cartItems);
        mCartListAdapter.notifyDataSetChanged();
        emptyLayout.setVisibility(View.VISIBLE);
        totalLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void updatePriceAndCount() {
        List<CartItem> cartItems = mCartListAdapter.getSelectedItems();
        double totalPrice = 0.00;
        for (CartItem item : cartItems) {
            totalPrice = item.getPrice() * item.getCount() + totalPrice;
        }
        updateDeleteBtn(!cartItems.isEmpty());
        totalAmount.setText(getString(R.string.total_, String.valueOf(cartItems.size())));
        mTotalPrice.setText(BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        if (cartItems.size() == Cart.get().getCartItems().size()) {
            selectAll.setSelected(true);
        } else {
            selectAll.setSelected(false);
        }

    }

    private void updateDeleteBtn(boolean canClick) {
        if (canClick) {
            titleBar.setRightViewEnable(true);
            titleBar.setRightTextColor(ContextCompat.getColorStateList(getActivity(), R.color.green));
        } else {
            titleBar.setRightViewEnable(false);
            titleBar.setRightTextColor(ContextCompat.getColorStateList(getActivity(), R.color.unluckyText));
        }
    }

    private void updateAll(boolean allSelected) {
        List<CartItem> cartItems = mCartListAdapter.getCartList();
        double totalPrice = 0.00;
        for (CartItem item : cartItems) {
            item.setSelected(allSelected);
            if (allSelected) {
                totalPrice = item.getPrice() * item.getCount() + totalPrice;
            }
        }
        if (allSelected) {
            updateDeleteBtn(true);
            totalAmount.setText(getString(R.string.total_, String.valueOf(cartItems.size())));
            mTotalPrice.setText(BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            updateDeleteBtn(false);
            totalAmount.setText(getString(R.string.total_, String.valueOf(0)));
            mTotalPrice.setText(String.valueOf(0.00));
        }
        mCartListAdapter.notifyDataSetChanged();
        if (cartItems.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateCartData() {
        swipeRefreshLayout.setRefreshing(false);
        mCartListAdapter.notifyDataSetChanged();
        updatePriceAndCount();
    }

    @Override
    public void onRefresh() {
        updateCartData();
    }


    @OnClick(R.id.goShop)
    public void onViewClicked() {
        Launcher.with(getActivity(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .execute();
    }
}
