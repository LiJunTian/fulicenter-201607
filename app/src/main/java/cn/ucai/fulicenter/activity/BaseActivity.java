package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ucai.fulicenter.MFGT;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    protected abstract void setListener();

    protected abstract void initData();

    protected abstract void initView();

    public void onBackPressed(View view){
        MFGT.finish(this);
    }
}
