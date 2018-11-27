package com.gerenvip.banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

public abstract class BaseBannerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    public int mMaxBannerSize = 0;
    public int RATIO_EXPAND = 100;
    private final ViewPager mViewPager;
    private int mCurrentIndex = 0;

    private List<T> mDatas;
    private HashMap<Integer, View> mCacheViews = new HashMap<>();

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
        mCacheViews.clear();
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
        LogUtil.d("BannerView", "isViewFromObject:view=" + view + "; object=" + object);
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogUtil.d("BannerView", "instantiateItem position=" + position);
        int dataPosition = getDataPosition(position);
        T data = mDatas.get(dataPosition);
        View view = mCacheViews.get(dataPosition);
        if (view == null) {
            view = createBannerItemView(data, dataPosition, position);
            mCacheViews.put(dataPosition, view);
        } else {
            updateView(view, data, dataPosition);
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        LogUtil.d("BannerView", "finishUpdate current position=" + position);
        if (position == 0) {
            position = getDataSize();
            mViewPager.setCurrentItem(position, false);
            LogUtil.d("BannerView", "finishUpdate1 position=" + position);
        } else if (position == mMaxBannerSize - 1) {
            position = getDataSize() - 1;
            mViewPager.setCurrentItem(position, false);
            LogUtil.d("BannerView", "finishUpdate2 position=" + position);
        }
    }

    public abstract View createBannerItemView(T data, int dataPosition, int position);

    public abstract void updateView(View view, T data, int dataPosition);

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
