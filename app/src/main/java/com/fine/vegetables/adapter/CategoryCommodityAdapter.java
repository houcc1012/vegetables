package com.fine.vegetables.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fine.vegetables.R;
import com.fine.vegetables.listener.OnAddCartListener;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.view.AmountView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryCommodityAdapter extends BaseAdapter {

    private Context context;
    private List<Commodity> foodDatas;

    private OnAddCartListener mOnAddCartListener;

    private Set<Integer> mDataSet = new HashSet<>();


    public CategoryCommodityAdapter(Context context, List<Commodity> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }

    public void setOnAddCartListener(OnAddCartListener mOnAddCartListener) {
        this.mOnAddCartListener = mOnAddCartListener;
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    public void addList(List<Commodity> commodities) {
        for (Commodity commodity : commodities) {
            if (mDataSet.add(commodity.getId())) {
                foodDatas.add(commodity);
            }
        }
    }

    public void clear() {
        foodDatas.clear();
        mDataSet.clear();
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commodity commodity = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_commodity, null);
            viewHold = new ViewHold();
            viewHold.commodityImg = convertView.findViewById(R.id.commodityImg);
            viewHold.commodityName = convertView.findViewById(R.id.commodityName);
            viewHold.unit = convertView.findViewById(R.id.unit);
            viewHold.price = convertView.findViewById(R.id.price);
            viewHold.amount = convertView.findViewById(R.id.amount);
            viewHold.addCart = convertView.findViewById(R.id.addCart);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        Glide.with(context).load(commodity.getLogo())
                .circleCrop()
                .into(viewHold.commodityImg);
        viewHold.commodityName.setText(commodity.getName());
        viewHold.unit.setText(context.getString(R.string.unit_, commodity.getUnitName()));
        viewHold.price.setText(context.getString(R.string.yuan_symbol_, String.valueOf(commodity.getPrice())));
        ViewHold finalViewHold = viewHold;
        viewHold.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.valueOf(finalViewHold.amount.getAmount());
                if (amount > 0) {
                    Cart.get().addCommodity(commodity, amount);
                }
                if (mOnAddCartListener != null) {
                    int[] startLocation = new int[2];
                    finalViewHold.commodityImg.getLocationInWindow(startLocation);
                    Drawable drawable = finalViewHold.commodityImg.getDrawable();
                    mOnAddCartListener.onClick(drawable, startLocation);
                }
            }
        });

        return convertView;


    }

    private static class ViewHold {
        private TextView unit;
        private TextView price;
        private AmountView amount;
        private View addCart;
        private TextView commodityName;
        private ImageView commodityImg;
    }
}
