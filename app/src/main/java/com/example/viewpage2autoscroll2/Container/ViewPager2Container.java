package com.example.viewpage2autoscroll2.Container;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.viewpager2.widget.ViewPager2;

import java.lang.ref.WeakReference;

public class ViewPager2Container extends RelativeLayout {
    ViewPager2 mViewPager2;
    private AutoScrollHandler mHandler;
    int touchSlop;

    public ViewPager2Container(Context context) {
        super(context);

    }

    public ViewPager2Container(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ViewPager2Container(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewPager2Container(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mHandler = new AutoScrollHandler(mViewPager2);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    //取得RelativeLayout所有view，並比對是否有ViewPager2
    //有後將該ViewPager2取得。
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < this.getChildCount(); i++) {
            View childView = this.getChildAt(i);
            if (childView instanceof ViewPager2) {
                this.mViewPager2 = (ViewPager2) childView;
                init();
                break;
            }
        }

    }

    private static final int
            MSG_AUTO_SCROLL = 1,
            DEFAULT_INTERNAL_IM_MILLIS = 5200;
    static int intervalInMillis;
    private boolean autoScroll = false;
    static int i = 0;

    static class AutoScrollHandler extends Handler {
        private final WeakReference<ViewPager2> autoScrollViewPager;

        public AutoScrollHandler(ViewPager2 autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTO_SCROLL:
                    ViewPager2 paper = this.autoScrollViewPager.get();
                    if (paper != null) {
//                        檢查是否有因為重新整裡頁面額而被吃掉的假拖移
                        if (paper.isFakeDragging()) {
                            return;
//                            paper.endFakeDrag();
                        }
//                        paper.setCurrentItem(paper.getCurrentItem() + 1);
                        //因為更改滑動動畫有做模擬拖移一個item的動作，因此不需要再額外多做一個setCurrentItem，如果在多做一個
                        //setCurrentItem反而會造成indexout的報錯
                        MyPagerHelper.setCurrentItem(paper, paper.getCurrentItem() + 1);
                        this.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis);
                        i++;
                        Log.d("AutoScrollHandler", "ViewPager2Container: " + i);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    public void setInterval(int intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    public void startAutoScroll() {
        startAutoScroll(intervalInMillis != 0 ? intervalInMillis : DEFAULT_INTERNAL_IM_MILLIS);
    }

    public void startAutoScroll(int intervalInMillis) {
        // Only post scroll message when necessary.
        if (mViewPager2.getAdapter().getItemCount() > 1) {
            this.intervalInMillis = intervalInMillis;
            autoScroll = true;
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis);
        }
    }

    public void stopAutoScroll() {
        autoScroll = false;
        mHandler.removeMessages(MSG_AUTO_SCROLL);
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    int downX = 0;
    int downY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                mHandler.removeMessages(MSG_AUTO_SCROLL);
                getParent().requestDisallowInterceptTouchEvent(true);//讓viewpager告知其父容器不要攔截響應事件
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = this.getHeight();
                int moveY = (int) ev.getY();
                int maxY = this.getScrollY();
                Log.e("ViewPager2Container", String.valueOf(moveX));
                Log.e("ViewPager2Container", String.valueOf(moveY));
                if (moveY < moveX) {
                    getParent().requestDisallowInterceptTouchEvent(true);//讓viewpager告知其父容器不要攔截響應事件
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);//讓viewpager告知其父容器要攔截響應事件
                }
                break;
            case MotionEvent.ACTION_UP:
                startAutoScroll();
                break;
            default:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}