package cn.ucai.fulicenter;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.activity.MainActivity;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class MFGT {//从哪来到哪去move from go to
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context,MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
