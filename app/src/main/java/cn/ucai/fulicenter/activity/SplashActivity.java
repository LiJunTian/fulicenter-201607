package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.MFGT;
import cn.ucai.fulicenter.R;

public class SplashActivity extends AppCompatActivity {
    private final long sleepTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        },sleepTime);

    }
}
