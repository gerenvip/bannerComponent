package com.gerenvip.banner.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
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
        mBannerView.enableScroll(true);
        mBannerView.setBannerInterval(3000l);
        mBannerView.getViewPager().setPageTransformer(false, new CubeInTransformer());

        List<DemoBannerView.Item> list = createList();
        mBannerView.setData(list);
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
