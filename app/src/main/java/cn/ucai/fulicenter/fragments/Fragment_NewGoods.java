package cn.ucai.fulicenter.fragments;


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
    static int ACTION_DOWNLOAD = 0;
    static int ACTION_PULL = 1;
    static int ACTION_PULLDONW = 2;
    final int CART_ID = 0;
    static int PAGE_ID = 1;
    static int PAGE_SIZE = 10;

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
        return view;
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
        GridLayoutManager glm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        NetDao.downLoadNewGoods(getContext(),PAGE_ID,new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {

               if(result!=null&&result.length>0){
//                   L.i("main","result:"+result.toString());
                   ArrayList<NewGoodsBean> arrayList = ConvertUtils.array2List(result);
                   L.i("main","arrayList:"+arrayList.get(0).toString());
                   mAdapter.initData(arrayList);
               }
            }
            @Override
            public void onError(String error) {
                L.i(error);
            }
        });
    }
}
