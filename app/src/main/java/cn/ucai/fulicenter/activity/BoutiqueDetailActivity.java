package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

public class BoutiqueDetailActivity extends AppCompatActivity {

    Context mContext;
    int boutiqueId;
    String title;
    int pageId = 1;
    GoodsAdapter goodsAdapter;
    ArrayList<NewGoodsBean> list;
    GridLayoutManager glm;

    @BindView(R.id.boutique_title)
    LinearLayout boutiqueTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.iv_title_arrow)
    ImageView ivTitleArrow;
    @BindView(R.id.tv_title_Name)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_detial);
        ButterKnife.bind(this);
        mContext=this;

        boutiqueId = getIntent().getIntExtra(I.Boutique.ID,0);
        title = getIntent().getStringExtra(I.Boutique.TITLE);
        L.e("boutique", "boutiqueId=" + boutiqueId);

        initAdapter();
        initData();
        setListener();
    }

    private void initData() {
        downloadData(I.ACTION_DOWNLOAD,pageId);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        goodsAdapter = new GoodsAdapter(mContext, list);
        recyclerView.setAdapter(goodsAdapter);

        glm = new GridLayoutManager(getBaseContext(), I.COLUM_NUM);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == goodsAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });

        recyclerView.setLayoutManager(glm);
    }

    private void downloadData(final int action,int pageId) {
        NetDao.downLoadNewGoods(mContext,boutiqueId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    tvTitleName.setText(title);
                    L.e("boutique", "result=" + result[0].toString());
                    goodsAdapter.setMore(true);
                    ArrayList<NewGoodsBean> newGoodsBeenList = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        goodsAdapter.initData(newGoodsBeenList);
                    }else{
                        goodsAdapter.addData(newGoodsBeenList);
                    }
                    if (result.length < I.PAGE_SIZE_DEFAULT) {
                        goodsAdapter.setMore(false);
                    }
                } else {
                    goodsAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.i(error);
            }
        });
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 1;
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downloadData(I.ACTION_PULL_DOWN,pageId);
            }
        });
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = glm.findLastVisibleItemPosition();
                if (position == goodsAdapter.getItemCount() - 1 && goodsAdapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    downloadData(I.ACTION_PULL_UP,pageId);
                }
            }
        });
    }

    @OnClick(R.id.iv_title_arrow)
    public void OnClickListener() {
        MFGT.finish(this);
    }
}
