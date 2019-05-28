package com.fine.vegetables.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fine.vegetables.R;

public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "AmountView";
    private boolean isNumber = true;

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private TextView btnDecrease;
    private TextView btnIncrease;


    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = this.findViewById(R.id.etAmount);
        btnDecrease = this.findViewById(R.id.btnDecrease);
        btnIncrease = this.findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);
        isNumber = etAmount.getInputType() != InputType.TYPE_NUMBER_FLAG_DECIMAL;

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
        int inputType = obtainStyledAttributes.getInt(R.styleable.AmountView_inputType, InputType.TYPE_CLASS_NUMBER);
        isNumber = inputType != InputType.TYPE_NUMBER_FLAG_DECIMAL;
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        etAmount.setInputType(inputType);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (isNumber) {
                int amount = Integer.valueOf(etAmount.getText().toString());
                if (amount > 1) {
                    amount--;
                    etAmount.setText(amount + "");
                }
            } else {
                double amount = Double.valueOf(etAmount.getText().toString());
                if (amount == (int) amount) {
                    if (amount > 1) {
                        amount = amount - 1;
                        etAmount.setText((int) amount + "");
                    }
                } else if (amount > 0.01) {
                    amount = amount - 0.01;
                    etAmount.setText(amount + "");
                }
            }
        } else if (i == R.id.btnIncrease) {
            if (isNumber) {
                int amount = Integer.valueOf(etAmount.getText().toString());
                amount = amount + 1;
                etAmount.setText(amount + "");
            } else {
                double amount = Double.valueOf(etAmount.getText().toString());
                if (amount == (int) amount) {
                    amount = amount + 1;
                    etAmount.setText((int) amount + "");
                } else {
                    amount = amount + 0.01;
                    etAmount.setText(amount + "");
                }
            }
        }
        if (mListener != null) {
            mListener.onAmountChange(this, etAmount.getText().toString());
        }

        etAmount.clearFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;

    }

    public String getAmount() {
        return etAmount.getText().toString();
    }

    public void setAmount(String amount) {
        etAmount.setText(amount);
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, String amount);
    }

}