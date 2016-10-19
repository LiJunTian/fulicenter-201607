package cn.ucai.fulicenter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.ConvertUtils;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Boutique extends Fragment {
    BoutiqueAdapter boutiqueAdapter;
    ArrayList<BoutiqueBean> boutiqueBeanList;
    LinearLayoutManager layoutManager;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;


    public Fragment_Boutique() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__new_goods, container, false);
        ButterKnife.bind(this, view);

        initData();
        initView();
        setListener();
        downLoadData(I.ACTION_DOWNLOAD);
        return view;
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue)
        );
    }

    private void initData() {
        boutiqueBeanList = new ArrayList<>();
        boutiqueAdapter = new BoutiqueAdapter(getContext(), boutiqueBeanList);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(boutiqueAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void downLoadData(final int action) {
        NetDao.downloadBoutiqueBean(getContext(), new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if (result != null && result.length > 0) {
                    boutiqueAdapter.setMore(true);
                    boutiqueAdapter.setTextFooter("加载更多数据");
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    /*if(action== I.ACTION_DOWNLOAD||action==I.ACTION_PULL_UP){
                        boutiqueAdapter.addData(list);
                    }else{
                        srlBoutique.setRefreshing(false);
                        tvRefresh.setVisibility(View.GONE);
                        boutiqueAdapter.initData(list);
                    }*/
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            boutiqueAdapter.addData(list);
                            break;
                        case I.ACTION_PULL_UP:
                            boutiqueAdapter.initData(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            srl.setRefreshing(false);
                            tvRefresh.setVisibility(View.GONE);
                            boutiqueAdapter.initData(list);
                            break;
                    }
                } else {
                    boutiqueAdapter.setMore(false);
                    boutiqueAdapter.setTextFooter("没有更多数据加载");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
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
                downLoadData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = layoutManager.findLastVisibleItemPosition();
                if (boutiqueAdapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE && position == boutiqueAdapter.getItemCount() - 1) {
                    downLoadData(I.ACTION_PULL_UP);
                }
            }
        });
    }

}
