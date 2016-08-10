package com.gerenvip.banner.example;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerenvip.banner.BannerView;
import com.gerenvip.banner.R;

public class DemoBannerView extends BannerView<DemoBannerView.Item> {

    private Context mCxt;

    public DemoBannerView(Context context) {
        super(context);
        mCxt = context;
    }

    public DemoBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCxt = context;
    }

    @Override
    public View getItemView(LayoutInflater inflater, Item data, int dataPosition) {
        View view = inflater.inflate(R.layout.item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.pos);
        textView.setText(dataPosition + "-" + data.name);
        imageView.setImageResource(data.resId);
        final int pos = dataPosition;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCxt, "click banner item :" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public static class Item {
        public int resId;
        public String name;
    }
}