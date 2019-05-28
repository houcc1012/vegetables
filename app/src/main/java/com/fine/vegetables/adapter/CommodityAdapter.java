package com.fine.vegetables.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fine.vegetables.R;
import com.fine.vegetables.listener.OnAddCartListener;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.view.AmountView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommodityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Commodity> mCommodityList;
    private OnAddCartListener mOnAddCartListener;
    private Set<Integer> mDataSet = new HashSet<>();

    public CommodityAdapter(List<Commodity> commodityList, Context context) {
        this.mContext = context;
        this.mCommodityList = commodityList;
    }

    public void clear() {
        mCommodityList.clear();
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public void setOnAddCartListener(OnAddCartListener mOnAddCartListener) {
        this.mOnAddCartListener = mOnAddCartListener;
    }

    public List<Commodity> getCommodityList() {
        return mCommodityList;
    }

    public void add(List<Commodity> commodities) {
        for (Commodity commodity : commodities) {
            if (mDataSet.add(commodity.getId())) {
                mCommodityList.add(commodity);
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_comodity, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mCommodityList.get(i), i, mContext, mOnAddCartListener);
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
        ImageView commodityImg;
        @BindView(R.id.commodityName)
        TextView commodityName;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.amount)
        AmountView amountView;
        @BindView(R.id.addCart)
        View addCart;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(final Commodity commodity, int position, Context context, OnAddCartListener mOnAddCartListener) {
            Glide.with(context).load(commodity.getLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(commodity.getName());
            unit.setText(context.getString(R.string.unit_, commodity.getUnitName()));
            price.setText(context.getString(R.string.yuan_symbol_, String.valueOf(commodity.getPrice())));
            addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = Integer.valueOf(amountView.getAmount());
                    if (amount > 0) {
                        Cart.get().addCommodity(commodity, amount);
                    }
                    if (mOnAddCartListener != null) {
                        int[] startLocation = new int[2];
                        commodityImg.getLocationInWindow(startLocation);
                        Drawable drawable = commodityImg.getDrawable();
                        mOnAddCartListener.onClick(drawable, startLocation);
                    }
                }
            });
            amountView.clearFocus();
        }
    }
}
