package cn.ucai.fulicenter;

import android.content.Context;

import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class NetDao {
    static int CAT_ID = 0;
    static int PAGE_ID = 1;
    public static void downLoadNewGoods(Context context, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean> listener ){
        OkHttpUtils<NewGoodsBean> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.Boutique.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean.class)
                .execute(listener);
    }
}
