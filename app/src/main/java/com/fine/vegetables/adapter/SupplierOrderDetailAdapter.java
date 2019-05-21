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
import com.fine.vegetables.model.Order;
import com.fine.vegetables.view.AmountView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Order> mOrderList;
    public static final int ALL_ORDER = 0;
    public static final int NO_DELIVERY_ORDER = 1;
    public static final int FINISHED_ORDER = 2;

    public SupplierOrderDetailAdapter(List<Order> orders, Context context, int orderType) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_order_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext);
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

        @BindView(R.id.commodityImg)
        ImageView commodityImg;
        @BindView(R.id.commodityName)
        TextView commodityName;
        @BindView(R.id.sumAmount)
        TextView sumAmount;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.amount)
        AmountView amount;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Order cart, int position, Context context) {

        }
    }
}
