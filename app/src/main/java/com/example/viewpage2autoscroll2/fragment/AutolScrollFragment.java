package com.example.viewpage2autoscroll2.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.viewpage2autoscroll2.Container.MyPagerHelper;
import com.example.viewpage2autoscroll2.R;
import com.example.viewpage2autoscroll2.Container.ViewPager2Container;
import com.example.viewpage2autoscroll2.adapter.AutolScrollFragmentAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AutolScrollFragment extends Fragment {
    View rootView = null;
    private Unbinder mUnbinder = null;
    AutolScrollFragmentAdapter mAutolScrollFragmentAdapter = null;
    private int currentPosition;
    ArrayList<String> mHomePageNewsVOs;
    ArrayList<String> mHomePageNewsVOs2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomePageNewsVOs=new ArrayList<>();
        for(int i=0;i<10;i++){
            mHomePageNewsVOs.add(String.valueOf(i));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_autol_scroll, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void init() {
        itemlistInit();
        if (mAutolScrollFragmentAdapter == null) {
            mviewPager2Container.setInterval(5200);//自動輪播時間
            MyPagerHelper.setDurationTime(1000);//動畫滑動時間(毫秒)
            mAutolScrollFragmentAdapter = new AutolScrollFragmentAdapter(mHomePageNewsVOs2);
            mAutoScrollViewPager.setAdapter(mAutolScrollFragmentAdapter);
            currentPosition = 1;
            if (mAutoScrollViewPager.isFakeDragging()) {
                mAutoScrollViewPager.endFakeDrag();
            }
            mAutoScrollViewPager.setCurrentItem(currentPosition, false);
            mviewPager2Container.startAutoScroll();
            mAutoScrollViewPager.registerOnPageChangeCallback(Callback);

        }


    }

    private ViewPager2.OnPageChangeCallback Callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            currentPosition = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            int mHomePageNewsVOsSize = mHomePageNewsVOs2.size();
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (currentPosition == 0) {
                    //檢查是否有因為重新整裡頁面額而被吃掉的假拖移
                    if (mAutoScrollViewPager.isFakeDragging()) {
                        mAutoScrollViewPager.endFakeDrag();
                    }
                    mAutoScrollViewPager.setCurrentItem(mHomePageNewsVOsSize - 2, false);//切换，不要动画效果
                } else if (currentPosition == mHomePageNewsVOsSize - 1) {
                    //檢查是否有因為重新整裡頁面額而被吃掉的假拖移
                    if (mAutoScrollViewPager.isFakeDragging()) {
                        mAutoScrollViewPager.endFakeDrag();
                    }
                    mAutoScrollViewPager.setCurrentItem(1, false);//切换，不要动画效果
                }
            }
        }
    };

    private void itemlistInit() {
//      index:                      0、1、2、3
        //圖片無限迴圈作法，原本的list為 0、1、2、3......16
        //      index:                 0、1、2、3、4.......15、16
        //在list前後個放入第一個和最後一個，16、0、1、2、3.......16、1
        //再將postion預設0更改為1這樣第一個圖片就會是第一個不會是假的第16個。
        //當index變更為0時候強制設定為15(圖片1左滑)
        //當index變更為16時候強制設定為1(圖片16右滑)
        mHomePageNewsVOs2 = new ArrayList<>();
        mHomePageNewsVOs2.add(mHomePageNewsVOs.get(mHomePageNewsVOs.size() - 1));
        for (int i = 0; i < mHomePageNewsVOs.size(); i++) {
            mHomePageNewsVOs2.add(i + 1, mHomePageNewsVOs.get(i));
        }
        mHomePageNewsVOs2.add(mHomePageNewsVOs.get(0));
    }



    @BindView(R.id.viewpager2)
    ViewPager2 mAutoScrollViewPager = null;
    @BindView(R.id.viewPager2Container)
    ViewPager2Container mviewPager2Container;
}
