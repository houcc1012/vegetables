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
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.view.AmountView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommodityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Commodity> mCommodityList;

    public CommodityListAdapter(List<Commodity> commodityList, Context context) {
        this.mContext = context;
        this.mCommodityList = commodityList;
    }

    public void clear() {
        mCommodityList.clear();
        notifyDataSetChanged();
    }

    public void add(List<Commodity> commodities) {
        mCommodityList.addAll(commodities);
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

        public void bindDataWithView(Commodity commodity, int position, Context context) {
            Glide.with(context).load(commodity.getClass())
//                    .placeholder(R.drawable.ic_default_avatar)
                    .circleCrop()
                    .into(commodityImg);
        }
    }
}
