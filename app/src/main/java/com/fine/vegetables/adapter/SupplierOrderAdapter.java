package com.fine.vegetables.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.model.Order;
import com.fine.vegetables.view.CommodityImagesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Order> mOrderList;
    public static final int NO_DELIVERY_ORDER = 0;
    public static final int FINISHED_ORDER = 1;
    private int orderType;

    public SupplierOrderAdapter(List<Order> orders, Context context, int orderType) {
        this.mContext = context;
        this.mOrderList = orders;

    }

    public void clear() {
        mOrderList.clear();
        notifyDataSetChanged();
    }

    public void add(List<Order> orders) {
        mOrderList.addAll(orders);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (orderType == NO_DELIVERY_ORDER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_order, viewGroup, false);
            return new SupplierOrderAdapter.NoDeliveryViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_history_order, viewGroup, false);
            return new SupplierOrderAdapter.FinishedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SupplierOrderAdapter.NoDeliveryViewHolder) {
            ((NoDeliveryViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext);
        }
        if (viewHolder instanceof SupplierOrderAdapter.FinishedViewHolder) {
            ((FinishedViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext);
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

    static class NoDeliveryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userAccount)
        TextView userAccount;
        @BindView(R.id.seeAddress)
        TextView seeAddress;
        @BindView(R.id.images)
        CommodityImagesView images;
        @BindView(R.id.right)
        ImageView right;
        @BindView(R.id.totalAmount)
        ImageView totalAmount;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.confirm)
        TextView confirm;


        public NoDeliveryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Order order, int position, Context context) {

        }
    }

    static class FinishedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userAccount)
        TextView userAccount;
        @BindView(R.id.seeAddress)
        TextView seeAddress;
        @BindView(R.id.images)
        CommodityImagesView images;
        @BindView(R.id.right)
        ImageView right;
        @BindView(R.id.totalAmount)
        TextView totalAmount;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.actualPrice)
        TextView actualPrice;
        @BindView(R.id.orderPrice)
        TextView orderPrice;

        public FinishedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Order order, int position, Context context) {
        }
    }
}
