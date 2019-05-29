package com.fine.vegetables.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fine.vegetables.R;
import com.fine.vegetables.listener.OnSelectAmountChangeListener;
import com.fine.vegetables.listener.OnSelectCartListener;
import com.fine.vegetables.model.CartItem;
import com.fine.vegetables.view.AmountView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<CartItem> mCartList;
    private OnSelectCartListener mOnSelectCartListener;
    private OnSelectAmountChangeListener mOnSelectAmountChangeListener;

    public CartListAdapter(List<CartItem> cartList, Context context) {
        this.mContext = context;
        this.mCartList = cartList;
    }

    public void remove(List<CartItem> items) {
        mCartList.removeAll(items);
    }

    public void setOnSelectCartListener(OnSelectCartListener onSelectCartListener) {
        mOnSelectCartListener = onSelectCartListener;
    }

    public void setOnSelectAmountChangeListener(OnSelectAmountChangeListener mOnSelectAmountChangeListener) {
        this.mOnSelectAmountChangeListener = mOnSelectAmountChangeListener;
    }

    public void setCartList(List<CartItem> mCartList) {
        this.mCartList = mCartList;
    }

    public void add(List<CartItem> cartList) {
        mCartList.addAll(cartList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mCartList.get(i), i, mContext, mOnSelectCartListener, mOnSelectAmountChangeListener);
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public List<CartItem> getCartList() {
        return mCartList;
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : mCartList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.index)
        TextView index;
        @BindView(R.id.commodityImg)
        ImageView commodityImg;
        @BindView(R.id.commodityName)
        TextView commodityName;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.unitName)
        TextView unitName;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.amount)
        AmountView amountView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(CartItem cart, int position, Context context, OnSelectCartListener mOnSelectCartListener, OnSelectAmountChangeListener onSelectAmountChangeListener) {
            Glide.with(context).load(cart.getLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(cart.getName());
            unit.setText(context.getString(R.string.unit_, cart.getUnitName()));
            unitName.setText(cart.getUnitName());
            price.setText(context.getString(R.string.yuan_symbol_, String.valueOf(cart.getPrice())));
            amountView.setAmount(String.valueOf(cart.getCount()));
            amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, String amount) {
                    Integer nowCount = Integer.valueOf(amount);
                    if (nowCount != cart.getCount()) {
                        if (nowCount >= 1000) {
                            amountView.setAmount(String.valueOf(999));
                            cart.setCount(999);
                        } else {
                            cart.setCount(nowCount);
                        }
                        if (onSelectAmountChangeListener != null) {
                            onSelectAmountChangeListener.change(cart);
                        }
                    }
                }
            });
            amountView.clearFocus();
            index.setSelected(cart.isSelected());
            index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index.isSelected()) {
                        index.setSelected(false);
                        cart.setSelected(false);
                        if (mOnSelectCartListener != null) {
                            mOnSelectCartListener.unSelect(cart);
                        }
                    } else {
                        index.setSelected(true);
                        cart.setSelected(true);
                        if (mOnSelectCartListener != null) {
                            mOnSelectCartListener.select(cart);
                        }
                    }

                }
            });
        }
    }
}
