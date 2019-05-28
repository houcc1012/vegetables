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
import com.fine.vegetables.model.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Order.DataBean> mDataList;

    public OrderDetailAdapter(List<Order.DataBean> orders, Context context) {
        this.mContext = context;
        this.mDataList = orders;

    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void add(List<Order.DataBean> orders) {
        mDataList.addAll(orders);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mDataList.get(i), i, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
        @BindView(R.id.sumAmount)
        AppCompatTextView sumAmount;
        @BindView(R.id.price)
        AppCompatTextView price;
        @BindView(R.id.actualAmout)
        AppCompatTextView actualAmount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Order.DataBean item, int position, Context context) {
            Glide.with(context).load(item.getVegetableLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(item.getVegetableName());
            price.setText(context.getString(R.string.yuan_symbol_,String.valueOf(item.getPrice())));
            sumAmount.setText(item.getExpectWeight() + item.getUnitName());
            actualAmount.setText(context.getString(R.string.actual_amount_, String.valueOf(item.getActualWeight()), item.getUnitName()));
        }
    }
}
