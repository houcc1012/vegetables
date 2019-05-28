package com.fine.vegetables.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.utils.DateUtil;

import java.util.Date;
import java.util.List;

public class TimerAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = 0;
    private List<String> list;
    private int type;

    public static final int TYPE_DATE = 0;
    public static final int TYPE_TIME = 1;

    public TimerAdapter(Context context, List<String> list, int type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public void setList(List<String> list) {
        this.list = list;
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
            arg1 = View.inflate(context, R.layout.item_timer, null);
            holder.time = arg1.findViewById(R.id.time);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        if (arg0 == selectItem) {
            holder.time.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.time.setTextColor(context.getResources().getColor(R.color.unluckyText));
        }
        if (type == TYPE_TIME) {
            holder.time.setText(list.get(arg0));
        } else {
            long date = DateUtil.format(list.get(arg0), DateUtil.FORMAT_DATE_ARENA);
            Date today = new Date();
            String dateStr = DateUtil.format(date, DateUtil.FORMAT_NOT_HOUR);
            if (DateUtil.isToday(new Date(date), today)) {
                holder.time.setText(DateUtil.TODAY + " " + dateStr);
            } else if (DateUtil.isTomorrow(new Date(date), today)) {
                holder.time.setText(DateUtil.TOMORROW + " " + dateStr);
            } else {
                holder.time.setText(DateUtil.getDayOfWeek(date) + " " + dateStr);
            }
        }
        return arg1;
    }

    static class ViewHolder {
        private TextView time;
    }
}
