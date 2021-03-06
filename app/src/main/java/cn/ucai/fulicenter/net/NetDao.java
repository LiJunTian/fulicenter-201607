package cn.ucai.fulicenter.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MD5;

/**NetDao方法，用于网络请求，下载数据
 * Created by Administrator on 2016/10/17 0017.
 */
public class NetDao {
    public static final String TAG = NetDao.class.getSimpleName();
    /**
     *新品首页或精选二级页面一组商品信息
     * @param context 上下文
     * @param cartId 购物车id
     * @param pageId 页码id
     * @param listener 监听器
     */
    public static void downLoadNewGoods(Context context, int cartId,int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener ){
        L.e(TAG,"NetDao执行");
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(cartId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 响应客户端下载分类中二级页面一组商品信息的请求
     * @param context 上下文
     * @param goodsId 商品id
     * @param listener 监听器
     */
    public static void downloadGoodsDetail(Context context,int goodsId,OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener){
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    /**
     * 提供精选首页数据
     * @param context 上下文
     * @param listener 监听器
     */
    public static void downloadBoutiqueBean(Context context ,OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener){
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    /**
     *下载分类首页中小类商品的数据
     * @param context 上下文
     * @param parenId 父类id
     * @param listener 监听器
     */
    public static void downloadCategoryChild(Context context, int parenId, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener){
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,""+parenId)
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    /**
     * 下载分类首页大类的数据
     * @param context 上下文
     * @param listener 监听器
     */
    public static void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener){
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    /**
     * 响应客户端下载分类中二级页面一组商品信息的请求
     * @param context 上下文
     * @param cartId 购物车id
     * @param pageId 页码id
     * @param listener 监听器
     */
    public static void downloadCategoryChildDetails(Context context,int cartId,int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(cartId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 登录
     * @param context 上下文
     * @param userName 用户名
     * @param password 密码
     * @param listener 监听器
     */
    public static void Login(Context context, String userName, String password, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD,MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 注册
     * @param context 上下文
     * @param userName 用户名
     * @param nick     昵称
     * @param password 密码
     * @param listener 监听器
     */
    public static void Register(Context context,String userName,String nick,String password,OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }

    /**
     * 更新用户昵称
     * @param context 上下文
     * @param userName 用户名
     * @param nick    昵称
     * @param listener 监听器
     */
    public static void updateNick(Context context, String userName, String nick, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 更新用户头像
     * @param context 上下文
     * @param userName 用户名
     * @param file    要上传的头像文件
     * @param listener 监听器
     */
    public static void updateAvatar(Context context,String userName,File file,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,userName)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    /**
     * 根据用户名查找用户信息，包括用户头像信息
     * @param context 上下文
     * @param userName 用户名
     * @param listener 监听器
     */
    public static void syncUserInfo(Context context,String userName,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 下载收藏商品的数量
     * @param context 上下文
     * @param userName 用户名
     * @param listener 监听器
     */
    public static void getCollectsCount(Context context, String userName, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 添加商品收藏
     * @param context 上下文
     * @param goodId  商品id
     * @param userName 用户名
     * @param listener 监听器
     */
    public static void addCollect(Context context, int goodId, String userName, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Goods.KEY_GOODS_ID,""+goodId)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 下载收藏的商品
     * @param context 上下文
     * @param userName 用户名
     * @param pageId  页码id
     * @param listener 监听器
     */
    public static void downloadCollects(Context context, String userName, int pageId,OkHttpUtils.OnCompleteListener<CollectBean[]> listener){
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,userName)
                .addParam(I.PAGE_ID,""+pageId)
                .addParam(I.PAGE_SIZE,""+I.PAGE_SIZE_DEFAULT)
                .targetClass(CollectBean[].class)
                .execute(listener);
    }

    /**
     * 删除收藏的商品
     * @param context 上下文
     * @param goodId  商品id
     * @param userName 用户名
     * @param listener 监听器
     */
    public static void deleteCollect(Context context, int goodId, String userName, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID,""+goodId)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 是否收藏指定商品
     * @param context 上下文
     * @param goodId 商品id
     * @param userName 用户名
     * @param listener 监听器
     */
    public static void isCollected(Context context,int goodId,String userName, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID,""+goodId)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 查询用户的购物车
     * @param context 上下文
     * @param userName 用户名
     * @param listener 监听
     */
    public static void findCarts(Context context, String userName, OkHttpUtils.OnCompleteListener<CartBean[]> listener){
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    /**
     * 查询用户的购物车（用string来解析)
     * @param context 上下文
     * @param userName 用户名
     * @param listener 监听
     */
    public static void findCart(Context context, String userName, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }
    /**
     * 响应客户端添加商品至购物车的请求
     * @param context 上下文
     * @param goodsId 商品id
     * @param userName 用户名
     * @param count   商品数量
     * @param isChecked 是否选中
     * @param listener 监听器
     */
    public static void addCart(Context context, int goodsId, String userName, int count, int isChecked, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,""+goodsId)
                .addParam(I.Cart.USER_NAME,userName)
                .addParam(I.Cart.COUNT,""+count)
                .addParam(I.Cart.IS_CHECKED,""+isChecked)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 删除购物车中的商品
     * @param context 上下文
     * @param id      购物车id
     * @param listener 监听器
     */
    public static void deleteCart(Context context, int id, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,""+id)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 修改购物车中的商品的选中件数
     * @param context 上下文
     * @param cartId  购物车id
     * @param count   数量
     * @param listener 监听器
     */
    public static void updateCart(Context context, int cartId, int count, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,""+cartId)
                .addParam(I.Cart.COUNT,""+count)
                .addParam(I.Cart.IS_CHECKED,""+0)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
