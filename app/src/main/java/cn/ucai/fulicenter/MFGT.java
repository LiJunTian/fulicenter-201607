package cn.ucai.fulicenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.fulicenter.activity.BoutiqueDetailActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.GoodsDetailsActivity;
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
        startActivity(context,intent);
    }

    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoGoodsDetailActivity(Context context, int goodsId){
        Intent intent = new Intent();
        intent.setClass(context,GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
    }

    public static void gotoBoutiqueDetailActivity(Context context,int boutiqueId,String title){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueDetailActivity.class);
        intent.putExtra(I.Boutique.ID,boutiqueId);
        intent.putExtra(I.Boutique.TITLE,title);
        startActivity(context,intent);
    }

    public static void gotoCategoryChildActivity(Context context,int catId,String title){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,title);
        startActivity(context,intent);
    }
}
