package com.example.viewpage2autoscroll2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.viewpage2autoscroll2.fragment.AutolScrollFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        Fragment fragment = new AutolScrollFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrameLayout, fragment, null)
                .commitAllowingStateLoss();

    }

    @BindView(R.id.FrameLayout)
    FrameLayout mFrameLayout = null;

}