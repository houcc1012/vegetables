package com.fine.vegetables.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fine.vegetables.R;

import java.util.List;

public class CommodityImagesView extends LinearLayout {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    public CommodityImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_commodity_images, this);
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);

    }

    public void loadImages(String... imageUrls) {
        switch (imageUrls.length) {
            case 1:
                loadImage(imageView1, imageUrls[0]);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                loadImage(imageView2, imageUrls[0]);
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                loadImage(imageView3, imageUrls[0]);
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.VISIBLE);
                imageView4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                loadImage(imageView4, imageUrls[0]);
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView4.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }

}
