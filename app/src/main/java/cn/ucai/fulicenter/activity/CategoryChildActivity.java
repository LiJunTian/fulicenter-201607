package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.CatChildFilterButton;
import cn.ucai.fulicenter.CommonUtils;
import cn.ucai.fulicenter.ConvertUtils;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.MFGT;
import cn.ucai.fulicenter.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

public class CategoryChildActivity extends BaseActivity {
    int cartId;
    String title;
    Context mContext;
    int pageId = 1;
    GoodsAdapter goodsAdapter;
    ArrayList<NewGoodsBean> list;
    GridLayoutManager glm;

    @BindView(R.id.category_title_back)
    ImageView categoryTitleBack;
    /*@BindView(R.id.category_title_name)
    TextView categoryTitleName;*/
    @BindView(R.id.category_title)
    RelativeLayout categoryTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addTime)
    Button btnSortAddTime;
    @BindView(R.id.catChildFilterButton)
    CatChildFilterButton catChildFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        mContext = this;
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    @Override
    protected void initData() {
        downloadData(I.ACTION_DOWNLOAD, pageId);
        cartId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        title = getIntent().getStringExtra(I.CategoryGroup.NAME);
        L.e("childId", "id=" + cartId + ",title=" + title);
        catChildFilterButton.setText(title);
        ArrayList<CategoryChildBean> list = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        catChildFilterButton.setOnCatFilterClickListener(title,list);
    }

    @Override
    protected void initView() {
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

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 1;
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downloadData(I.ACTION_PULL_DOWN, pageId);
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
                    downloadData(I.ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    private void downloadData(final int action, int pageId) {
        NetDao.downloadCategoryChildDetails(mContext, cartId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    L.e("boutique", "result=" + result[0].toString());
                    goodsAdapter.setMore(true);
                    ArrayList<NewGoodsBean> newGoodsBeenList = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        goodsAdapter.initData(newGoodsBeenList);
                    } else {
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

    @OnClick(R.id.category_title_back)
    public void OnClick() {
        MFGT.finish(this);
    }

    static boolean priceAsc = false;
    static boolean addTimeAsc = false;
    static int sortBy;

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addTime})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                priceAsc = !priceAsc;
                break;
            case R.id.btn_sort_addTime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btnSortAddTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                addTimeAsc = !addTimeAsc;
                break;
        }
        goodsAdapter.setSortBy(sortBy);
    }

    /*boolean isDown = true;
    @OnClick(R.id.catChildFilterButton)
    public void onClick(){
        if(isDown){
            Drawable up = getResources().getDrawable(R.drawable.arrow2_up);
            up.setBounds(0,0,up.getIntrinsicWidth(),up.getIntrinsicHeight());
            catChildFilterButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,up,null);
            isDown = !isDown;
        }else{
            Drawable up = getResources().getDrawable(R.drawable.arrow2_down);
            up.setBounds(0,0,up.getIntrinsicWidth(),up.getIntrinsicHeight());
            catChildFilterButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,up,null);
            isDown = !isDown;
        }
//        catChildFilterButton.setOnCatFilterClickListener();
    }*/
}
