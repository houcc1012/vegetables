package com.fine.vegetables.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fine.vegetables.R;
import com.fine.vegetables.dialog.TimerPickerDialogFragment;
import com.fine.vegetables.model.Cart;
import com.fine.vegetables.model.CartItem;
import com.fine.vegetables.model.SubmitOrder;
import com.fine.vegetables.net.Callback2D;
import com.fine.vegetables.net.Client;
import com.fine.vegetables.net.Resp;
import com.fine.vegetables.utils.DateUtil;
import com.fine.vegetables.utils.Launcher;
import com.fine.vegetables.utils.ToastUtil;
import com.fine.vegetables.view.CommodityImagesView;
import com.fine.vegetables.view.TitleBar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.selectTime)
    TextView mSelectTime;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.commodityImgs)
    CommodityImagesView commodityImgs;
    @BindView(R.id.seeDetail)
    ImageView seeDetail;
    @BindView(R.id.amount)
    AppCompatTextView amount;
    @BindView(R.id.sumPrice)
    AppCompatTextView sumPrice;
    @BindView(R.id.confirm)
    AppCompatTextView confirm;

    private List<CartItem> mCartItems;
    private SubmitOrder submitOrder = new SubmitOrder();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_SELECT_TIME.equalsIgnoreCase(intent.getAction())) {
                String dateStr = intent.getStringExtra(Launcher.EX_PAYLOAD);
                String timeStr = intent.getStringExtra(Launcher.EX_PAYLOAD_1);
                long date = DateUtil.format(dateStr, DateUtil.FORMAT_DATE_ARENA);
                Date today = new Date();
                if (DateUtil.isToday(new Date(date), today)) {
                    mSelectTime.setText(DateUtil.TODAY + timeStr);
                } else if (DateUtil.isTomorrow(new Date(date), today)) {
                    mSelectTime.setText(DateUtil.TOMORROW + timeStr);
                } else {
                    mSelectTime.setText(DateUtil.getDayOfWeek(date) + dateStr);
                }
                String[] times = timeStr.split("-");
                submitOrder.setStartTime(DateUtil.format(dateStr + " " + times[0], DateUtil.DEFAULT_FORMAT_NOT_SECOND));
                submitOrder.setEndTime(DateUtil.format(dateStr + " " + times[1], DateUtil.DEFAULT_FORMAT_NOT_SECOND));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initData(getIntent());
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION_SELECT_TIME));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void initView() {
        double totalPrice = 0.00;
        List<String> urls = new ArrayList<>();
        List<SubmitOrder.DataBean> dataBeans = new ArrayList<>();
        for (CartItem item : mCartItems) {
            totalPrice = item.getPrice() * item.getCount() + totalPrice;
            urls.add(item.getLogo());
            SubmitOrder.DataBean dataBean = new SubmitOrder.DataBean();
            dataBean.setId(item.getId());
            dataBean.setCount(String.valueOf(item.getCount()));
            dataBeans.add(dataBean);
        }
        submitOrder.setData(dataBeans);
        amount.setText(getString(R.string._total, String.valueOf(mCartItems.size())));
        sumPrice.setText(BigDecimal.valueOf(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        commodityImgs.loadImages(urls);
    }

    private void initData(Intent intent) {
        mCartItems = (List<CartItem>) intent.getSerializableExtra(Launcher.EX_PAYLOAD);
    }

    @OnClick({R.id.selectTime, R.id.seeDetail, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectTime:
                TimerPickerDialogFragment.newInstance().show(getSupportFragmentManager());
                break;
            case R.id.seeDetail:
                Launcher.with(getActivity(), CommodityInventoryActivity.class)
                        .putExtra(Launcher.EX_PAYLOAD, (Serializable) mCartItems)
                        .execute();
                break;
            case R.id.confirm:
                if (mSelectTime.getText().toString().equalsIgnoreCase(getString(R.string.please_select_send_time))) {
                    ToastUtil.show(R.string.please_select_send_time);
                    return;
                }
                confirm.setEnabled(false);
                Client.submitOrder(submitOrder).setTag(TAG)
                        .setCallback(new Callback2D<Resp<String>, String>() {
                            @Override
                            protected void onRespSuccessData(String data) {
                                Cart.get().removeItems(mCartItems);
                                Launcher.with(getActivity(), OrderDetailActivity.class)
                                        .putExtra(Launcher.EX_PAYLOAD, data)
                                        .execute();
                                finish();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                confirm.setEnabled(true);
                            }
                        }).fireFree();
                break;
            default:
                break;
        }
    }
}
