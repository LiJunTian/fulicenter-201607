package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.FlowIndicator;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;
import cn.ucai.sharesdk.onekeyshare.OnekeyShare;

public class GoodsDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_title_arrow)
    ImageView ivTitleArrow;
    @BindView(R.id.iv_title_cart)
    ImageView ivTitleCart;
    @BindView(R.id.iv_title_boutique)
    ImageView ivTitleBoutique;
    @BindView(R.id.iv_title_share)
    ImageView ivTitleShare;
    @BindView(R.id.LL_GoodDetail_title)
    LinearLayout LLGoodDetailTitle;
    @BindView(R.id.tvGoodsDetail_GoodsEnglishName)
    TextView tvGoodsDetailGoodsEnglishName;
    @BindView(R.id.tvGoodsDetail_GoodsName)
    TextView tvGoodsDetailGoodsName;
    @BindView(R.id.tvGoodsDetail_GoodsPrice)
    TextView tvGoodsDetailGoodsPrice;
    @BindView(R.id.RL_GoodDetail_Name)
    RelativeLayout RLGoodDetailName;
    @BindView(R.id.RL_GoodDetail_Image)
    RelativeLayout RLGoodDetailImage;

    int goodsId;
//    NewGoodsBean goods;
    boolean isCollected = false;
    int collectCount = 0;
    GoodsDetailsActivity mContext;
    @BindView(R.id.slideAutoLoopView)
    SlideAutoLoopView mSalv;
    @BindView(R.id.flowIndicator)
    FlowIndicator mIndicator;
    @BindView(R.id.webViewGoodBrief)
    WebView mWebViewGoodBrief;
    @BindView(R.id.tvCollectCount)
    TextView tvCollectCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
//        goods = (NewGoodsBean) getIntent().getSerializableExtra(I.GoodsDetails.KEY_GOODS_ID);
        L.e("details", "goodsId=" + goodsId);

        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    public void onBackPressed(View view) {
        finish();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
                    ivTitleShare.setTag(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("main,error=" + error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        tvGoodsDetailGoodsEnglishName.setText(details.getGoodsEnglishName());
        tvGoodsDetailGoodsName.setText(details.getGoodsName());
        tvGoodsDetailGoodsPrice.setText(details.getCurrencyPrice());
        mSalv.startPlayLoop(mIndicator,getAlbumImgUrl(details),getAlbumImgCount(details));
        mWebViewGoodBrief.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }

    @OnClick(R.id.iv_title_arrow)
    public void OnClick() {
        MFGT.finish(this);
    }

    @OnClick(R.id.iv_title_share)
    public void showShare() {
        GoodsDetailsBean detail = (GoodsDetailsBean) ivTitleShare.getTag();

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("标题");
        oks.setText(detail.getGoodsName());

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        oks.setTitleUrl(detail.getShareUrl());

        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        oks.setText(detail.getGoodsBrief());

        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg ");
        String url = "http://101.251.196.90:8000/FuLiCenterServerV2.0/downloadImage?imageurl=201509/thumb_img/";
        oks.setImageUrl(url+detail.getGoodsThumb());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //s.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
        oks.setUrl(detail.getShareUrl());

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
//        oks.setComment("福利社是个不错的app，你值得拥有!");

        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
        oks.setSite("福利社");

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
        oks.setSiteUrl(detail.getShareUrl());

        // 启动分享GUI
        oks.show(this);
    }

    @OnClick(R.id.iv_title_boutique)
    public void onCollectGoods() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            if (isCollected) {
                NetDao.deleteCollect(mContext, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null) {
                            if (result.isSuccess()) {
                                isCollected = false;
                                ivTitleBoutique.setImageResource(R.mipmap.bg_collect_in);
                                CommonUtils.showLongToast("删除收藏成功");
                            } else {
                                CommonUtils.showLongToast("删除收藏失败");
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("error=" + error);
                        CommonUtils.showLongToast("删除收藏失败");
                    }
                });
            } else {
                NetDao.addCollect(mContext, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null) {
                            if (result.isSuccess()) {
                                isCollected = true;
                                ivTitleBoutique.setImageResource(R.mipmap.bg_collect_out);
                                CommonUtils.showLongToast("收藏成功");
                            } else {
                                CommonUtils.showLongToast("收藏失败");
                            }
                        } else {
                            CommonUtils.showLongToast("收藏失败");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("error=" + error);
                        CommonUtils.showLongToast("收藏失败");
                    }
                });
            }
        }
    }

    private void isCollected() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isCollected(mContext, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;
                        updateGoodsCollectStatus();
                    } else {
                        isCollected = false;
                        updateGoodsCollectStatus();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodsCollectStatus();
                }
            });

        }
        updateGoodsCollectStatus();
    }

    private void updateGoodsCollectStatus() {
        if (isCollected) {
            ivTitleBoutique.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivTitleBoutique.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.iv_title_cart)
    public void onAddCart(){
            User user = FuLiCenterApplication.getUser();
            NetDao.addCart(mContext,goodsId,user.getMuserName(),1,0, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        CommonUtils.showLongToast("添加到购物车成功");
                        mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART).putExtra(I.ACTION_UPDATE_CART,true));
                    }
                }
                @Override
                public void onError(String error) {

                }
            });
        }
}
