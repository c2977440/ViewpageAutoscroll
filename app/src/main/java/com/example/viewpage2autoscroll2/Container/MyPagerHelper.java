package com.example.viewpage2autoscroll2.Container;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.viewpager2.widget.ViewPager2;

public class MyPagerHelper {
    /**
     * 保存前一个animatedValue
     */
    private static int previousValue;
    private static long durationTime = 1000;//scroll時長(毫秒)預設1秒

    /**
     * 设置当前Item
     *
     * @param pager viewpager2
     * @param item  下一個跳轉的item
     */
    public static void setCurrentItem(final ViewPager2 pager, int item) {
        previousValue = 0;
        int currentItem = pager.getCurrentItem();//目前的getCurrentItem
        int pagePxWidth = pager.getWidth();
        int pxToDrag = pagePxWidth * (item - currentItem);//跳轉幾個item的寬度
        final ValueAnimator animator = ValueAnimator.ofInt(0, pxToDrag);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                float currentPxToDrag = (float) (currentValue - previousValue);
                pager.fakeDragBy(-currentPxToDrag);//因為是水平拖移必須是反向，這邊就是達成不需要再前面hander做setCurrentItem的主因
                previousValue = currentValue;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                pager.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(durationTime);
        animator.start();
    }

    public static void setDurationTime(long DurationTime) {
        durationTime = DurationTime;
    }
}
