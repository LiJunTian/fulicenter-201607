package cn.ucai.fulicenter.fragments;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.CommonUtils;
import cn.ucai.fulicenter.ConvertUtils;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_NewGoods extends Fragment {
    final static int ACTION_DOWNLOAD = 0;
    final static int ACTION_PULL_UP = 1;
    final static int ACTION_PULL_DONW = 2;
    final int CART_ID = 0;
    static int PAGE_ID = 1;
    static int PAGE_SIZE = 10;

    GridLayoutManager glm;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    MainActivity mContext;
    ArrayList<NewGoodsBean> list;
    GoodsAdapter mAdapter;
    public Fragment_NewGoods() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__new_goods, container, false);
        ButterKnife.bind(this, view);
        initView();
        setListener();
        return view;
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                L.i("main","visibility设置为显示");
                PAGE_ID = 1;
                downLoadGoods(ACTION_PULL_DONW,PAGE_ID);
            }
        });
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = glm.findLastVisibleItemPosition();
                if(position>=mAdapter.getItemCount()-1&&mAdapter.isMore()&&newState==recyclerView.SCROLL_STATE_IDLE){
                    PAGE_ID++;
                    L.e("main","pageId="+PAGE_ID);
                    downLoadGoods(ACTION_PULL_UP,PAGE_ID);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.google_yellow)
        );

        list = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(),list);
        glm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==mAdapter.getItemCount()-1){
                    return 2;
                }
                return 1;
            }
        });


        recyclerView.setAdapter(mAdapter);
        //
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        downLoadGoods(ACTION_DOWNLOAD,PAGE_ID);
    }

    private void downLoadGoods(final int action , int pageId) {
        NetDao.downLoadNewGoods(getContext(),pageId,new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                /*srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);*/
               if(result!=null&&result.length>0){
                   ArrayList<NewGoodsBean> arrayList = ConvertUtils.array2List(result);
                   mAdapter.setMore(true);
                   L.i("main","arrayList:"+arrayList.get(0).toString());
                   mAdapter.setFooterText("上拉加载更多");
                   switch (action){
                       case ACTION_DOWNLOAD:
                           mAdapter.initData(arrayList);
                           break;
                       case ACTION_PULL_DONW:
                           mAdapter.initData(arrayList);
                           srl.setRefreshing(false);
                           tvRefresh.setVisibility(View.GONE);
                           break;
                       case ACTION_PULL_UP:
                           mAdapter.addData(arrayList);
                           L.i("main","pull_up被执行");
                           break;
                   }
               }else{
                        mAdapter.setMore(false);
                       if(action==ACTION_PULL_UP){
                           mAdapter.setFooterText("没有更多数据");
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
}
