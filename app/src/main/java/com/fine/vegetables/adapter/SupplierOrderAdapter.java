package com.fine.vegetables.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.listener.OnConfirmOrderClickListener;
import com.fine.vegetables.listener.OnSeeAddressClickListener;
import com.fine.vegetables.listener.OnSeeDetailListener;
import com.fine.vegetables.model.SellerOrder;
import com.fine.vegetables.utils.DateUtil;
import com.fine.vegetables.view.CommodityImagesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SellerOrder> mOrderList;
    public static final int NO_DELIVERY_ORDER = 1;
    public static final int FINISHED_ORDER = 2;
    private int orderType;
    private OnSeeDetailListener mOnSeeDetailListener;
    private OnConfirmOrderClickListener<SellerOrder> mOnConfirmOrderClickListener;
    private OnSeeAddressClickListener<SellerOrder> mOnSeeAddressClickListener;

    public SupplierOrderAdapter(List<SellerOrder> orders, Context context, int orderType) {
        this.mContext = context;
        this.mOrderList = orders;
        this.orderType = orderType;

    }

    public List<SellerOrder> getOrderList() {
        return mOrderList;
    }

    public void setOnSeeAddressClickListener(OnSeeAddressClickListener<SellerOrder> mOnSeeAddressClickListener) {
        this.mOnSeeAddressClickListener = mOnSeeAddressClickListener;
    }

    public void setOnSeeDetailListener(OnSeeDetailListener mOnSeeDetailListener) {
        this.mOnSeeDetailListener = mOnSeeDetailListener;
    }

    public void setOnConfirmOrderClickListener(OnConfirmOrderClickListener<SellerOrder> mOnConfirmOrderClickListener) {
        this.mOnConfirmOrderClickListener = mOnConfirmOrderClickListener;
    }

    public void clear() {
        mOrderList.clear();
        notifyDataSetChanged();
    }

    public void add(List<SellerOrder> orders) {
        mOrderList.addAll(orders);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (orderType == NO_DELIVERY_ORDER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_order, viewGroup, false);
            return new NoDeliveryViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_history_order, viewGroup, false);
            return new FinishedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NoDeliveryViewHolder) {
            ((NoDeliveryViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext, mOnSeeDetailListener, mOnConfirmOrderClickListener, mOnSeeAddressClickListener);
        }
        if (viewHolder instanceof FinishedViewHolder) {
            ((FinishedViewHolder) viewHolder).bindDataWithView(mOrderList.get(i), i, mContext, mOnSeeDetailListener, mOnConfirmOrderClickListener, mOnSeeAddressClickListener);
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
        AppCompatImageView right;
        @BindView(R.id.totalAmount)
        TextView totalAmount;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.confirm)
        TextView confirm;


        public NoDeliveryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(SellerOrder order, int position, Context context, OnSeeDetailListener onSeeDetailListener, OnConfirmOrderClickListener<SellerOrder> onConfirmOrderClickListener, OnSeeAddressClickListener<SellerOrder> onSeeAddressClickListener) {
            time.setText(DateUtil.format(order.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH));
            images.loadImages(order.getUrls());
            totalAmount.setText(context.getString(R.string.total_commodity_, String.valueOf(order.getTotalCount())));
            userName.setText(order.getName());
//            userAccount.setText(order.getPhone());
            userAccount.setText(order.getOrderId());
            seeAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeAddressClickListener != null) {
                        onSeeAddressClickListener.click(order);
                    }
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onConfirmOrderClickListener != null) {
                        onConfirmOrderClickListener.click(order);
                    }
                }
            });
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeDetailListener != null) {
                        onSeeDetailListener.onClick(order.getOrderId());
                    }
                }
            });

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
        AppCompatImageView right;
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

        public void bindDataWithView(SellerOrder order, int position, Context context, OnSeeDetailListener onSeeDetailListener, OnConfirmOrderClickListener<SellerOrder> mOnItemClickListener, OnSeeAddressClickListener<SellerOrder> onSeeAddressClickListener) {
            time.setText(DateUtil.format(order.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH));
            images.loadImages(order.getUrls());
            totalAmount.setText(context.getString(R.string.total_commodity_, String.valueOf(order.getTotalCount())));
            userName.setText(order.getName());
//            userAccount.setText(order.getPhone());
            userAccount.setText(order.getOrderId());
            actualPrice.setText(context.getString(R.string.yuan_symbol_, order.getActualMoney()));
            orderPrice.setText(context.getString(R.string.yuan_symbol_, order.getTotalMoney()));
            seeAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeAddressClickListener != null) {
                        onSeeAddressClickListener.click(order);
                    }
                }
            });
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSeeDetailListener != null) {
                        onSeeDetailListener.onClick(order.getOrderId());
                    }
                }
            });
            images.setOnClickListener(new View.OnClickListener() {
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
