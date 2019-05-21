package com.fine.vegetables.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.model.Category;
import com.fine.vegetables.model.Commodity;
import com.fine.vegetables.view.GridViewForScrollView;

import java.util.List;

/**
 *  右侧ListView的适配器
 */
public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> foodDatas;

    public CategoryAdapter(Context context, List<Category> foodDatas) {
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
        Category category = foodDatas.get(position);
        List<Commodity> commodities = category.getCommodities();
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category, null);
            viewHold = new ViewHold();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        CategoryCommodityAdapter adapter = new CategoryCommodityAdapter(context, commodities);
        viewHold.blank.setText(category.getName());
        viewHold.gridView.setAdapter(adapter);
        return convertView;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
        private TextView blank;
    }
}
