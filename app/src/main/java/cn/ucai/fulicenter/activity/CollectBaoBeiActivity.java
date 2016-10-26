package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectAdapter;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class CollectBaoBeiActivity extends BaseActivity {
    User user;
    Context mContext;
    int PAGE_ID = 1;
    GridLayoutManager glm;
    CollectAdapter mAdapter;
    ArrayList<CollectBean> list;

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.iv_collect_back)
    ImageView ivCollectBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect_bao_bei);
        ButterKnife.bind(this);
        mContext = this;
        user = FuLiCenterApplication.getUser();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    @Override
    protected void initData() {
        downLoadGoods(I.ACTION_DOWNLOAD, PAGE_ID);
    }

    @Override
    protected void onResume() {
        L.e("CollectBaoBeiActivity","CollectBaoBeiActivity.onResume():执行啦");
        super.onResume();
        initData();
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.google_yellow)
        );

        list = new ArrayList<>();
        mAdapter = new CollectAdapter(mContext, list);
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                L.i("main", "visibility设置为显示");
                PAGE_ID = 1;
                downLoadGoods(I.ACTION_PULL_DOWN, PAGE_ID);
            }
        });
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = glm.findLastVisibleItemPosition();
                if (position >= mAdapter.getItemCount() - 1 && mAdapter.isMore() && newState == recyclerView.SCROLL_STATE_IDLE) {
                    PAGE_ID++;
                    L.e("main", "pageId=" + PAGE_ID);
                    downLoadGoods(I.ACTION_PULL_UP, PAGE_ID);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void downLoadGoods(final int action, int pageId) {
        NetDao.downloadCollects(mContext, user.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                L.e("downloadCollects", "result=" + result);
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> arrayList = ConvertUtils.array2List(result);
                    mAdapter.setMore(true);
                    L.i("main", "arrayList:" + arrayList.get(0).toString());
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(arrayList);
                    } else {
                        mAdapter.addData(arrayList);
                    }
                    if (result.length < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                    int count = Integer.parseInt(getIntent().getStringExtra(I.Collect.COUNT));
                    if(count!=0){
                        mAdapter.clearData(0);
                    }
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

    @OnClick(R.id.iv_collect_back)
    public void onBack(){
        MFGT.finish(this);
    }

}
