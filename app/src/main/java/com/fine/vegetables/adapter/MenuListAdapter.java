package com.fine.vegetables.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fine.vegetables.R;

import java.util.List;

public class MenuListAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = 0;
    private List<String> list;

    public MenuListAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder = null;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(context, R.layout.item_category_menu, null);
            holder.name = arg1.findViewById(R.id.name);
            holder.index = arg1.findViewById(R.id.index);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        if (arg0 == selectItem) {
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.name.setTextColor(context.getResources().getColor(R.color.luckyText));
            holder.index.setVisibility(View.VISIBLE);
        } else {
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.name.setTextColor(context.getResources().getColor(R.color.unluckyText));
            holder.index.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(list.get(arg0));
        return arg1;
    }

    static class ViewHolder {
        private TextView name;
        private View index;
    }
}
