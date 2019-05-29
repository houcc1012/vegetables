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
import com.fine.vegetables.listener.OnAmountChangeListener;
import com.fine.vegetables.model.SellerOrder;
import com.fine.vegetables.view.AmountView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SellerOrder.DataBean> mDataList;
    public static final int NO_DELIVERY_ORDER = 1;
    public static final int FINISHED_ORDER = 2;
    private int mOrderType;
    private OnAmountChangeListener mOnAmountChangeListener;

    public SupplierOrderDetailAdapter(List<SellerOrder.DataBean> datas, Context context) {
        this.mContext = context;
        this.mDataList = datas;

    }

    public void setOrderType(int orderType) {
        this.mOrderType = orderType;
    }

    public void setOnAmountChangeListener(OnAmountChangeListener mOnAmountChangeListener) {
        this.mOnAmountChangeListener = mOnAmountChangeListener;
    }

    public List<SellerOrder.DataBean> getDataList() {
        return mDataList;
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void add(List<SellerOrder.DataBean> orders) {
        mDataList.addAll(orders);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (mOrderType == FINISHED_ORDER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail, viewGroup, false);
            return new FinishedViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_order_detail, viewGroup, false);
            return new NoDeliveryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NoDeliveryViewHolder) {
            ((NoDeliveryViewHolder) viewHolder).bindDataWithView(mDataList.get(i), mContext, mOnAmountChangeListener);
        }
        if (viewHolder instanceof FinishedViewHolder) {
            ((FinishedViewHolder) viewHolder).bindDataWithView(mDataList.get(i), mContext);
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

    static class NoDeliveryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commodityImg)
        AppCompatImageView commodityImg;
        @BindView(R.id.commodityName)
        AppCompatTextView commodityName;
        @BindView(R.id.sumAmount)
        AppCompatTextView sumAmount;
        @BindView(R.id.price)
        AppCompatTextView price;
        @BindView(R.id.amount)
        AmountView amountView;
        @BindView(R.id.unit)
        AppCompatTextView unit;


        public NoDeliveryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(SellerOrder.DataBean data, Context context, OnAmountChangeListener mOnAmountChangeListener) {

            Glide.with(context).load(data.getVegetableLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(data.getVegetableName());
            price.setText(context.getString(R.string.yuan_symbol_, String.valueOf(data.getPrice())));
            sumAmount.setText(data.getExpectWeight() + data.getUnitName());
            unit.setText(data.getUnitName());
            amountView.setAmount(data.getActualWeight());
            amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, String amount) {
                    Double nowCount = Double.valueOf(amount);
                    if (nowCount != Double.valueOf(data.getActualWeight())) {
                        if (nowCount >= 1000) {
                            amountView.setAmount(String.valueOf(999));
                            data.setActualWeight("999");
                        } else {
                            data.setActualWeight(String.valueOf(nowCount));
                        }
                        if (mOnAmountChangeListener != null) {
                            mOnAmountChangeListener.change();
                        }
                    }
                }
            });
        }
    }

    static class FinishedViewHolder extends RecyclerView.ViewHolder {

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

        public FinishedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(SellerOrder.DataBean data, Context context) {
            Glide.with(context).load(data.getVegetableLogo())
                    .circleCrop()
                    .into(commodityImg);
            commodityName.setText(data.getVegetableName());
            price.setText(context.getString(R.string.yuan_symbol_, String.valueOf(data.getPrice())));
            sumAmount.setText(data.getExpectWeight() + data.getUnitName());
            actualAmount.setText(context.getString(R.string.actual_amount_, data.getActualWeight(), data.getUnitName()));
        }
    }
}
