package com.gerenvip.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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

    private static final long INTERVAL_DEFAULT = 2000;

    private long mScheduleDelay = INTERVAL_DEFAULT;
    private long mBannerPeriod = INTERVAL_DEFAULT;

    private TimerTask mTimerTask;
    private boolean mHideIndicator;
    private int mIndicatorHeight;
    private boolean mTimerCancel;
    private boolean mEnableAutoScroll = true;
    private boolean mRunning;

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

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.base_banner_view, this, true);
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
                    mHandler.removeCallbacks(mDelayResetRunnable);
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP
                        || action == MotionEvent.ACTION_CANCEL) {
//                    mIsUserTouched = false;
                    mHandler.removeCallbacks(mDelayResetRunnable);
                    mHandler.postDelayed(mDelayResetRunnable, 1000);
                }
                return false;
            }
        });
    }

    /**
     * before {@link #setData(List)}
     *
     * @param time
     */
    public void setBannerScheduleDelay(long time) {
        mScheduleDelay = time;
    }

    /**
     * before {@link #setData(List)}
     *
     * @param time
     */
    public void setBannerPeriod(long time) {
        mBannerPeriod = time;
    }

    public void enableAutoScroll(boolean enableScroll) {
        boolean oldValue = mEnableAutoScroll;
        mEnableAutoScroll = enableScroll;
        adjustAutoScroll(oldValue);
    }

    public boolean isAutoScrollEnable() {
        return mEnableAutoScroll;
    }

    private void adjustAutoScroll(boolean oldValue) {
        if (oldValue != mEnableAutoScroll) {
            if (!mEnableAutoScroll) {
                stopAutoScroll();
            } else {
                Log.w("BannerView", "please call startAutoScroll to real start auto scroll");
            }
        }
    }

    public void setData(List<T> list) {
        mBannerAdapter.setData(list);
        mIndicator.setCount(list.size());
        startTimer();
    }

    private void scheduleScroll() {
        if (!mEnableAutoScroll) {
            return;
        }
        mTimer.schedule(mTimerTask, mScheduleDelay, mBannerPeriod);
        mRunning = true;
    }

    private void ensureTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = createTask();
        }
    }

    private TimerTask createTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (!mIsUserTouched) {
                    mHandler.post(BannerView.this);
                } else {
                    mHandler.removeCallbacks(BannerView.this);
                }
            }
        };
    }

    /**
     * 调用关闭自动滚动
     */
    public void stopAutoScroll() {
        stopTimer();
    }

    private void stopTimer() {
        try {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
                mTimerCancel = true;
                mTimerTask.cancel();
                mTimerTask = null;
            }
            mRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        if (!mEnableAutoScroll) {
            return;
        }
        if (isScheduleRunning()) {
            Log.i("BannerView", "Timer already running");
            return;
        }
        ensureTimer();
        scheduleScroll();
    }

    private boolean isScheduleRunning() {
        return mRunning;
    }

    /**
     * 调用开启自动滚动
     */
    public boolean startAutoScroll() {
        if (!mEnableAutoScroll) {
            LogUtil.d("BannerView", "can not startTimer, you must call enableAutoScroll() to enable auto scroll");
            return false;
        }
        startTimer();
        return true;
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

    /**
     * 是否隐藏指示器
     *
     * @param hide true 隐藏，反之 显示
     */
    public void hideIndicator(boolean hide) {
        mHideIndicator = hide;
        mIndicator.setVisibility(mHideIndicator ? GONE : VISIBLE);
    }

    /**
     * 设置指示器的高度
     *
     * @param height px
     */
    public void setIndicatorHeight(int height) {
        mIndicator.getLayoutParams().height = height;
        mIndicator.requestLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private Runnable mDelayResetRunnable = new Runnable() {
        @Override
        public void run() {
            mIsUserTouched = false;
        }
    };

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setPageTransformer(@Nullable ViewPager.PageTransformer transformer) {
        mViewPager.setPageTransformer(false, transformer);
    }

    protected BaseBannerAdapter<T> createBannerAdapter() {
        return new BannerAdapter(getContext(), mViewPager);
    }

    @Override
    public void run() {
        int dataSize = mBannerAdapter.getDataSize();
        if (dataSize <= 1) {
            return;
        }
        int nextIndex = mBannerAdapter.getNextIndex();
        if (nextIndex == mBannerAdapter.getCount() - 1) {
            nextIndex = mBannerAdapter.getDataSize() - 1;
            mViewPager.setCurrentItem(nextIndex, false);
        } else {
            mViewPager.setCurrentItem(nextIndex, true);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTimer != null && mTimerCancel) {
            startTimer();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
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
        public void updateView(View view, T data, int dataPosition) {
            BannerView.this.updateView(view, data, dataPosition);
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

    public void updateView(View view, T data, int dataPosition) {
    }
}
