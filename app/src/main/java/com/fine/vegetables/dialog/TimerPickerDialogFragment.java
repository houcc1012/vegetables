package com.fine.vegetables.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.fine.vegetables.R;
import com.fine.vegetables.activity.BaseActivity;
import com.fine.vegetables.adapter.TimerAdapter;
import com.fine.vegetables.utils.DateUtil;
import com.fine.vegetables.utils.Launcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择送货时间
 */

public class TimerPickerDialogFragment extends BottomDialogFragment {
    @BindView(R.id.close)
    ImageView mClose;
    @BindView(R.id.lvDate)
    ListView mLvDate;
    @BindView(R.id.lvTime)
    ListView mLvTime;
    @BindView(R.id.confirm)
    TextView mConfirm;

    private TimerAdapter mDateAdapter;
    private TimerAdapter mTimerAdapter;
    private List<String> dateList = new ArrayList<>();
    private List<String> timeList = new ArrayList<>();

    private Unbinder mBind;

    public static TimerPickerDialogFragment newInstance() {
        Bundle args = new Bundle();
        TimerPickerDialogFragment fragment = new TimerPickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_timer_picker, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initView() {
        initSendDateList();
        mDateAdapter = new TimerAdapter(getContext(), dateList, TimerAdapter.TYPE_DATE);
        mTimerAdapter = new TimerAdapter(getContext(), timeList, TimerAdapter.TYPE_TIME);
        mLvDate.setAdapter(mDateAdapter);
        mLvTime.setAdapter(mTimerAdapter);
        mLvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDateAdapter.getSelectItem() != position) {
                    mDateAdapter.setSelectItem(position);
                    mDateAdapter.notifyDataSetChanged();
                    initSendTimeList(new Date(DateUtil.format(dateList.get(position), DateUtil.FORMAT_DATE_ARENA)));
                    mTimerAdapter.notifyDataSetChanged();
                }
            }
        });
        mLvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTimerAdapter.setSelectItem(position);
                mTimerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initSendDateList() {
        //五天
        Date todayDate = new Date();
        for (int i = 0; i < 5; i++) {
            dateList.add(DateUtil.addDate(todayDate, i, DateUtil.FORMAT_DATE_ARENA));
        }
        initSendTimeList(new Date());

    }

    public void initSendTimeList(Date date) {
        timeList.clear();
        if (DateUtil.isToday(date, new Date())) {
            Date nextHourTime = DateUtil.getNextHourTime(date);
            for (int i = 1; i < 24; i++) {
                String hhmm = DateUtil.addHour(nextHourTime, i - 1, DateUtil.FORMAT_HOUR);
                String hhmm2 = DateUtil.addHour(nextHourTime, i, DateUtil.FORMAT_HOUR);
                if (hhmm.compareTo("06:00") < 0) {
                    continue;
                }
                timeList.add(hhmm + "-" + hhmm2);
                if (hhmm.compareTo("21:00") >= 0) {
                    break;
                }
            }
        } else {
            String[] times = getResources().getStringArray(R.array.send_times);
            for (String time : times) {
                timeList.add(time);
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @OnClick({R.id.close, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String date = dateList.get(mDateAdapter.getSelectItem());
                String time = timeList.get(mTimerAdapter.getSelectItem());
                sendSuccessBroadcast(getActivity(), date, time);
                this.dismiss();
                break;
            case R.id.close:
                this.dismiss();
                break;
        }
    }

    private void sendSuccessBroadcast(FragmentActivity activity, String date, String times) {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.ACTION_SELECT_TIME);
        intent.putExtra(Launcher.EX_PAYLOAD, date);
        intent.putExtra(Launcher.EX_PAYLOAD_1, times);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }
}
