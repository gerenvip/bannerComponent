package com.gerenvip.banner.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.gerenvip.banner.R;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    private DemoBannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        mBannerView = (DemoBannerView) findViewById(R.id.banner_view);
        mBannerView.enableAutoScroll(true);
        mBannerView.setBannerPeriod(3000l);
        mBannerView.setBannerScheduleDelay(2000);
        mBannerView.getViewPager().setPageTransformer(false, new DefaultTransformer());

        List<DemoBannerView.Item> list = createList();
        mBannerView.setData(list);

        FrameLayout bannerContent = findViewById(R.id.banner_content);

        DemoBannerView bannerView = new DemoBannerView(this);
        bannerContent.addView(bannerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bannerView.enableAutoScroll(true);
        bannerView.setBannerPeriod(4000);
        bannerView.setPageTransformer(new CubeInTransformer());
        List<DemoBannerView.Item> data = createList();
        bannerView.setData(data);
    }

    public void startTimer(View view) {
        boolean state = mBannerView.startAutoScroll();
        Toast.makeText(this, "restart -> " + state, Toast.LENGTH_SHORT).show();
    }

    public void stopTimer(View view) {
        mBannerView.stopAutoScroll();
        Toast.makeText(this, "stop timer ", Toast.LENGTH_SHORT).show();
    }

    public void toggle(View view) {
        boolean autoScrollEnable = mBannerView.isAutoScrollEnable();
        mBannerView.enableAutoScroll(!autoScrollEnable);
        if (!autoScrollEnable) {
            mBannerView.startAutoScroll();
        }
        Toast.makeText(this, autoScrollEnable ? "stop scroll ok " : "start scroll ok", Toast.LENGTH_SHORT).show();
    }

    public List<DemoBannerView.Item> createList() {
        ArrayList<DemoBannerView.Item> list = new ArrayList<>();
        DemoBannerView.Item item = new DemoBannerView.Item();
        item.resId = R.mipmap.img1;
        item.name = "name-0";
        item.url = "http://img15.3lian.com/2015/c1/83/d/1.jpg";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img2;
        item.name = "name-1";
        item.url = "http://img15.3lian.com/2015/c1/83/d/2.jpg";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img3;
        item.name = "name-2";
        item.url = "http://img15.3lian.com/2015/c1/83/d/3.jpg";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img4;
        item.name = "name-3";
        item.url = "http://img15.3lian.com/2015/c1/83/d/4.jpg";
        list.add(item);
        return list;
    }


}
