package com.fine.vegetables.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.view.AmountView;

import java.util.List;

public class CategoryCommodityAdapter extends BaseAdapter {

    private Context context;
    private List<Commodity> foodDatas;

    public CategoryCommodityAdapter(Context context, List<Commodity> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
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
        Commodity subcategory = foodDatas.get(position);
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
//        viewHold.tv_name.setText(subcategory.getTitle());
//        Uri uri = Uri.parse(subcategory.getImgURL());
//        viewHold.iv_icon.setImageURI(uri);
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
