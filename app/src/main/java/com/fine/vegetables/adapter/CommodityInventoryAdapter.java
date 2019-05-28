package com.fine.vegetables.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fine.vegetables.R;
import com.fine.vegetables.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommodityInventoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<CartItem> mCommodityList;

    public CommodityInventoryAdapter(List<CartItem> commodityList, Context context) {
        this.mContext = context;
        this.mCommodityList = commodityList;
    }

    public void clear() {
        mCommodityList.clear();
        notifyDataSetChanged();
    }

    public void add(List<CartItem> commodities) {
        mCommodityList.addAll(commodities);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_commodity_inventory, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mCommodityList.get(i), i, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mCommodityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.commodityImg)
        AppCompatImageView commodityImg;
        @BindView(R.id.commodityName)
        AppCompatTextView commodityName;
        @BindView(R.id.totalPrice)
        AppCompatTextView totalPrice;
        @BindView(R.id.price)
        AppCompatTextView price;
        @BindView(R.id.amount)
        AppCompatTextView amount;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(final CartItem cartItem, int position, Context context) {
            Glide.with(context).load(cartItem.getLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(cartItem.getName());
            price.setText(context.getString(R.string.unit_price_, String.valueOf(cartItem.getPrice())));
            amount.setText(context.getString(R.string.amount_, String.valueOf(cartItem.getCount())));
            totalPrice.setText(BigDecimal.valueOf(cartItem.getPrice() * cartItem.getCount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
    }
}
