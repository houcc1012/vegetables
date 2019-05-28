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
import com.fine.vegetables.listener.OnSeeDetailListener;
import com.fine.vegetables.model.Order;
import com.fine.vegetables.utils.DateUtil;
import com.fine.vegetables.view.AmountView;
import com.fine.vegetables.view.CommodityImagesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Order> mOrderList;
    private OnSeeDetailListener mOnSeeDetailListener;

    public OrderAdapter(List<Order> orders, Context context) {
        this.mContext = context;
        this.mOrderList = orders;

    }

    public void setOnSeeDetailListener(OnSeeDetailListener onSeeDetailListener) {
        this.mOnSeeDetailListener = onSeeDetailListener;
    }

    public void clear() {
        mOrderList.clear();
        notifyDataSetChanged();
    }

    public List<Order> getOrderList() {
        return mOrderList;
    }

    public void add(List<Order> orders) {
        mOrderList.addAll(orders);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext, mOnSeeDetailListener);
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.commodityImgs)
        CommodityImagesView commodityImgs;
        @BindView(R.id.seeDetail)
        ImageView seeDetail;
        @BindView(R.id.totalAmount)
        TextView totalAmount;
        @BindView(R.id.orderId)
        TextView orderId;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Order order, int position, Context context, OnSeeDetailListener onSeeDetailListener) {
            time.setText(DateUtil.format(order.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH));
            orderStatus.setText(order.getStatus() == 1 ? context.getString(R.string.wait_send) : context.getString(R.string.finished));
            commodityImgs.loadImages(order.getUrls());
            orderId.setText(context.getString(R.string.order_id_, order.getOrderId()));
            totalAmount.setText(context.getString(R.string.total_commodity_, String.valueOf(order.getTotalCount())));
            seeDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeDetailListener != null) {
                        onSeeDetailListener.onClick(order.getOrderId());
                    }
                }
            });
            commodityImgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeDetailListener != null) {
                        onSeeDetailListener.onClick(order.getOrderId());
                    }
                }
            });
        }
    }
}
