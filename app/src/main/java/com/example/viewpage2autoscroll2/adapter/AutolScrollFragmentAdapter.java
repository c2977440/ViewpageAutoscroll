package com.example.viewpage2autoscroll2.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.viewpage2autoscroll2.R;
import com.example.viewpage2autoscroll2.fragment.AutolScrollFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AutolScrollFragmentAdapter extends RecyclerView.Adapter<AutolScrollFragmentAdapter.ViewHolder> {
    static int previousX, previousY;
    private CountDownTimer countDownTimer = null;
    private boolean disableClickEvent;
    ArrayList<String> mHomePageNewsVOs2 = new ArrayList<>();


    public AutolScrollFragmentAdapter(ArrayList<String> HomePageNewsVOs2) {
        mHomePageNewsVOs2 = HomePageNewsVOs2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(mLayoutInflater.inflate(R.layout.autollscroll_view, parent, false));
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.autollscroll_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /* 設定圖片 */
        holder.mSimpleDraweeView.setImageResource(R.mipmap.ic_launcher);
//        holder.mSimpleDraweeView.setMaxWidth(mscreenWidth);
        holder.mSimpleDraweeView.setAspectRatio(1.777f);


        /* 設定輪播指示器 */
        holder.mCarouselIndicatorTextView.setText(
                String.format(
                        Locale.getDefault(),
                        "%d/%d",
                        position,
                        getItemCount() - 2
                )
        );

        holder.mRootRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        previousX = x;
                        previousY = y;
//
                        //Log.d("TAG", "touched down(" + x + ", " + y + ")");

                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//
                        //int previousX = (int)mCarouselViewHolder.mRootRelativeLayout.getTag();
                        if (previousX < x - 10) {
                            //Log.d("TAG", "swipe right");
                        }
                        if (previousX > x + 10) {
                            //Log.d("TAG", "swipe left");

                        }
                        if ((Math.abs(x - previousX) < 10) && (Math.abs(y - previousY) < 10) && !disableClickEvent) {
                            //Log.d("TAG", "Click!!");
                            disableClickEvent = true;
                            startCountDownTimer();
                        }
                        //Log.d("TAG", "touched up(" + x + ", " + y + ")");
                        break;
                }

                return true;
            }

        });


    }

    private void startCountDownTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    disableClickEvent = false;
                }
            }.start();
        } else {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    @Override
    public int getItemCount() {
        return mHomePageNewsVOs2.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public SimpleDraweeView getCarouselCoverSimpleDraweeView() {
            return mSimpleDraweeView;
        }

        @BindView(R.id.SimpleDraweeView)
        SimpleDraweeView mSimpleDraweeView = null;
        @BindView(R.id.RootRelativeLayout)
        RelativeLayout mRootRelativeLayout = null;
        @BindView(R.id.carouselIndicatorTextView)
        TextView mCarouselIndicatorTextView = null;

    }
}