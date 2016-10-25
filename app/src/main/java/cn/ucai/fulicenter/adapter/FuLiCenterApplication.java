package cn.ucai.fulicenter.adapter;

import android.app.Application;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    static User user;
    public FuLiCenterApplication(){
        instance = this;
    }

    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance = new FuLiCenterApplication();
        }
        return instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        user = u;
    }
}
