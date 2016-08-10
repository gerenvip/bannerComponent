package com.gerenvip.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BannerView<T> extends FrameLayout implements Runnable {

    private ViewPager mViewPager;
    private PagerIndicator mIndicator;

    private Timer mTimer;

    private boolean mIsUserTouched = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private BaseBannerAdapter<T> mBannerAdapter;

    private static final long INTERVAL_DEFAULT = 2000l;

    private long mBannerInterval = INTERVAL_DEFAULT;
    private long mBannerPeriod = INTERVAL_DEFAULT;

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mHandler.post(BannerView.this);
            }
        }
    };
    private boolean mHideIndicator;
    private int mIndicatorHeight;
    private boolean mTimerCancel;

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mHideIndicator = a.getBoolean(R.styleable.BannerView_customIndicator, false);
        mIndicatorHeight = a.getDimensionPixelOffset(R.styleable.BannerView_indicatorHeight, getResources().getDimensionPixelOffset(R.dimen.default_indicator_height));
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.base_banner_view, this, true);
        mTimer = new Timer();
    }

    /**
     * before {@link #setData(List)}
     *
     * @param time
     */
    public void setBannerInterval(long time) {
        mBannerInterval = time;
    }

    /**
     * before {@link #setData(List)}
     *
     * @param time
     */
    public void setBannerPeriod(long time) {
        mBannerPeriod = time;
    }

    public void setData(List<T> list) {
        mBannerAdapter.setData(list);
        mIndicator.setCount(list.size());
        mTimer.schedule(mTimerTask, mBannerInterval, mBannerPeriod);
    }

    public void notifyDataSetChanged() {
        mBannerAdapter.notifyDataSetChanged();
        int dataSize = mBannerAdapter.getDataSize();
        mIndicator.setCount(dataSize);
    }

    public void setCurrent(int current) {
        mViewPager.setCurrentItem(current);
    }

    public int getCurrent() {
        return mViewPager.getCurrentItem();
    }

    public void hideIndicator(boolean hide) {
        mHideIndicator = hide;
        mIndicator.setVisibility(mHideIndicator ? GONE : VISIBLE);
    }

    public void setIndicatorHeight(int height) {
        mIndicator.getLayoutParams().height = height;
        mIndicator.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = (ViewPager) findViewById(R.id.banner);
        mIndicator = (PagerIndicator) findViewById(R.id.indicator);
        if (mHideIndicator) {
            mIndicator.setVisibility(GONE);
        } else {
            mIndicator.getLayoutParams().height = mIndicatorHeight;
            mIndicator.requestLayout();
        }

        mBannerAdapter = createBannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
    }

    protected BaseBannerAdapter<T> createBannerAdapter() {
        return new BannerAdapter(getContext(), mViewPager);
    }

    @Override
    public void run() {
        int nextIndex = mBannerAdapter.getNextIndex();
        if (nextIndex == mBannerAdapter.getCount() - 1) {
            nextIndex = mBannerAdapter.getDataSize() - 1;
            mViewPager.setCurrentItem(nextIndex, false);
        } else {
            mViewPager.setCurrentItem(nextIndex);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTimer != null && mTimerCancel) {
            mTimer.schedule(mTimerTask, mBannerInterval, mBannerPeriod);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimer.cancel();
        mTimerCancel = true;
    }

    protected class BannerAdapter extends BaseBannerAdapter<T> {

        private final LayoutInflater mInflater;

        public BannerAdapter(Context context, ViewPager pager) {
            super(pager);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View createBannerItemView(T data, int dataPosition, int position) {
            return getItemView(mInflater, data, dataPosition);
        }

        @Override
        protected void onPageSelectedInternal(int dataPosition) {
            BannerView.this.onPageSelected(dataPosition);
            mIndicator.setActiveIndex(dataPosition);
        }
    }

    protected void onPageSelected(int position) {
    }

    public abstract View getItemView(LayoutInflater inflater, T data, int dataPosition);
}
