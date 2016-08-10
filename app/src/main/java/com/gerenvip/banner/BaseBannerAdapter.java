package com.gerenvip.banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseBannerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    public int mMaxBannerSize = 0;
    public int RATIO_EXPAND = 100;
    private final ViewPager mViewPager;
    private int mCurrentIndex = 0;

    private List<T> mDatas;

    public BaseBannerAdapter(ViewPager pager) {
        mViewPager = pager;
        mViewPager.addOnPageChangeListener(this);
    }

    public void setData(List<T> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    private void calculateMaxBannerSize() {
        if (mDatas != null) {
            mMaxBannerSize = mDatas.size() * RATIO_EXPAND;
        }
    }

    public int getDataSize() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public final void notifyDataSetChanged() {
        calculateMaxBannerSize();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (getDataSize() < 2) {
            return getDataSize();
        } else {
            return mMaxBannerSize;
        }
    }

    public int getNextIndex() {
        return (mCurrentIndex + 1) % mMaxBannerSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int dataPosition = getDataPosition(position);
        T data = mDatas.get(dataPosition);
        View itemView = createBannerItemView(data, dataPosition, position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        if (position == 0) {
            position = getDataSize();
            mViewPager.setCurrentItem(position, false);
        } else if (position == mMaxBannerSize - 1) {
            position = getDataSize() - 1;
            mViewPager.setCurrentItem(position, false);
        }
    }

    public abstract View createBannerItemView(T data, int dataPosition, int position);

    private int getDataPosition(int position) {
        if (getDataSize() == 0) {
            return 0;
        } else {
            return position % getDataSize();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public final void onPageSelected(int position) {
        mCurrentIndex = position;
        int dataPosition = getDataPosition(position);
        onPageSelectedInternal(dataPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    protected abstract void onPageSelectedInternal(int dataPosition);
}
