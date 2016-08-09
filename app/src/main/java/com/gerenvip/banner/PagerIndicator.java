package com.gerenvip.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author wangwei on 16/8/9
 *         wangwei@jiandaola.com
 */
public class PagerIndicator extends View {

    private int mIndicatorSize;
    private int mIndicatorPadding;
    private int mIndicatorColor;
    private int mIndicatorActiveColor;

    private int mCount = 0;

    private int mCurrentIndex = 0;

    private Paint mPaint;

    public PagerIndicator(Context context) {
        super(context);
        init();
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        mIndicatorSize = a.getDimensionPixelOffset(R.styleable.PagerIndicator_indicatorSize, mIndicatorSize);
        mIndicatorPadding = a.getDimensionPixelOffset(R.styleable.PagerIndicator_indicatorPadding, mIndicatorPadding);
        mIndicatorColor = a.getColor(R.styleable.PagerIndicator_indicatorNormalColor, mIndicatorColor);
        mIndicatorActiveColor = a.getColor(R.styleable.PagerIndicator_indicatorActiveColor, mIndicatorActiveColor);
        mCurrentIndex = a.getInt(R.styleable.PagerIndicator_defaultSelected, 0);
        a.recycle();

    }

    private void init() {
        mIndicatorSize = getResources().getDimensionPixelSize(R.dimen.default_pager_indicator_size);
        mIndicatorPadding = getResources().getDimensionPixelOffset(R.dimen.default_pager_indicator_padding);
        mIndicatorColor = getResources().getColor(R.color.page_indicator_color);
        mIndicatorActiveColor = getResources().getColor(R.color.page_indicator_color_active);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setCount(int count) {
        mCount = count;
        requestLayout();
    }

    public void setActiveIndex(int index) {
        mCurrentIndex = index;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight();
        width += mIndicatorSize * mCount + mIndicatorPadding * (mCount - 1);
        setMeasuredDimension(width, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = mIndicatorSize / 2;
        for (int i = 0; i < mCount; i++) {
            if (i == mCurrentIndex) {
                mPaint.setColor(mIndicatorActiveColor);
            } else {
                mPaint.setColor(mIndicatorColor);
            }

            float cx = getPaddingLeft() + i * (mIndicatorPadding + mIndicatorSize) + mIndicatorSize / 2;
            float cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, r, mPaint);
        }
    }
}
