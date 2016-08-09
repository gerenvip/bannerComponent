package com.gerenvip.banner.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gerenvip.banner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei on 16/8/9
 *         wangwei@jiandaola.com
 */
public class DemoActivity extends AppCompatActivity {

    private DemoBannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        mBannerView = (DemoBannerView) findViewById(R.id.banner_view);

        List<DemoBannerView.Item> list = createList();
        mBannerView.setData(list);
    }

    public List<DemoBannerView.Item> createList() {
        ArrayList<DemoBannerView.Item> list = new ArrayList<>();
        DemoBannerView.Item item = new DemoBannerView.Item();
        item.resId = R.mipmap.img1;
        item.name = "name-1";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img2;
        item.name = "name-2";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img3;
        item.name = "name-3";
        list.add(item);

        item = new DemoBannerView.Item();
        item.resId = R.mipmap.img4;
        item.name = "name-4";
        list.add(item);
        return list;
    }


}
