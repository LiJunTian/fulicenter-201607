package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.CommonUtils;
import cn.ucai.fulicenter.FlowIndicator;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.SlideAutoLoopView;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

public class GoodsDetailsActivity extends AppCompatActivity {
    SlideAutoLoopView slideAutoLoopView;
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
    GoodsDetailsActivity mContext;
    @BindView(R.id.slideAutoLoopView)
    SlideAutoLoopView mSalv;
    @BindView(R.id.flowIndicator)
    FlowIndicator mIndicator;
    @BindView(R.id.webViewGoodBrief)
    WebView mWebViewGoodBrief;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsId=" + goodsId);

        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {

    }

    private void initView() {

    }

    private void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
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
        mSalv.startPlayLoop(mIndicator, getAlbumImgUrl(details), getAlbumImgCount(details));
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
}
