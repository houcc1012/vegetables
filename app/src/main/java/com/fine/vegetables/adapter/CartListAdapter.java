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
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.view.AmountView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Cart> mCartList;

    public CartListAdapter(List<Cart> cartList, Context context) {
        this.mContext = context;
        this.mCartList = cartList;
    }

    public void clear() {
        mCartList.clear();
        notifyDataSetChanged();
    }

    public void add(List<Cart> cartList) {
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
            ((ViewHolder) viewHolder).bindDataWithView(mCartList.get(i), i, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
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
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.amount)
        AmountView amountView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(Cart cart, int position, Context context) {
            Glide.with(context).load(cart.getClass())
//                    .placeholder(R.drawable.ic_default_avatar)
                    .circleCrop()
                    .into(commodityImg);
        }
    }
}
