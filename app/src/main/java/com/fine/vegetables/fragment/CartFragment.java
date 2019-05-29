package com.fine.vegetables.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.activity.ConfirmOrderActivity;
import com.fine.vegetables.activity.MainActivity;
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
import butterknife.Unbinder;

public class CartFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    Unbinder unbinder;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.totalLayout)
    View totalLayout;
    @BindView(R.id.selectAll)
    TextView selectAll;
    @BindView(R.id.totalPrice)
    TextView mTotalPrice;
    @BindView(R.id.totalAmount)
    TextView mTotalAmount;
    @BindView(R.id.submit)
    TextView submit;
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
                mRecyclerView.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cart.get().registerOnCartChangeListener(mOnCartChangeListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        if (Cart.get().getCartItems().isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
        }

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
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
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mCartListAdapter = new CartListAdapter(Cart.get().getCartItems(), getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(mCartListAdapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (KeyBoardUtils.isSHowKeyboard(mRecyclerView)) {
//                    mRecyclerView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeyBoardUtils.closeKeyboard(mRecyclerView);
//                        }
//                    }, 300);
//                }
//            }
//        });
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
        mTotalAmount.setText(getString(R.string.total_, String.valueOf(0)));
        mTotalPrice.setText(String.valueOf(0.00));
        selectAll.setSelected(false);
        mCartListAdapter.remove(cartItems);
        mCartListAdapter.notifyDataSetChanged();
    }

    private void updatePriceAndCount() {
        List<CartItem> cartItems = mCartListAdapter.getSelectedItems();
        double totalPrice = 0.00;
        for (CartItem item : cartItems) {
            totalPrice = item.getPrice() * item.getCount() + totalPrice;
        }
        updateDeleteBtn(!cartItems.isEmpty());
        mTotalAmount.setText(getString(R.string.total_, String.valueOf(cartItems.size())));
        mTotalPrice.setText(BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        if (cartItems.size() == Cart.get().getCartItems().size()) {
            selectAll.setSelected(true);
        } else {
            selectAll.setSelected(false);
        }

    }

    private void updateDeleteBtn(boolean canClick) {
        if (canClick) {
            mTitleBar.setRightViewEnable(true);
            mTitleBar.setRightTextColor(ContextCompat.getColorStateList(getContext(), R.color.green));
        } else {
            mTitleBar.setRightViewEnable(false);
            mTitleBar.setRightTextColor(ContextCompat.getColorStateList(getContext(), R.color.unluckyText));
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
            mTotalAmount.setText(getString(R.string.total_, String.valueOf(cartItems.size())));
            mTotalPrice.setText(BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            updateDeleteBtn(false);
            mTotalAmount.setText(getString(R.string.total_, String.valueOf(0)));
            mTotalPrice.setText(String.valueOf(0.00));
        }
        mCartListAdapter.notifyDataSetChanged();
        if (cartItems.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateCartData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Cart.get().unRegisterOnCartChangeListener(mOnCartChangeListener);
    }

    public void updateCartData() {
        mSwipeRefreshLayout.setRefreshing(false);
        mCartListAdapter.setCartList(Cart.get().getCartItems());
        mCartListAdapter.notifyDataSetChanged();
        updatePriceAndCount();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        updateCartData();
    }

    @OnClick(R.id.goShop)
    public void onViewClicked() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).goHomePage();
        }
    }
}
