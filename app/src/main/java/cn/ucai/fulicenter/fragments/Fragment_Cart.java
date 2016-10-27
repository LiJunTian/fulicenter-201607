package cn.ucai.fulicenter.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Cart extends BaseFragment {
    updateCartReceiver receiver;
    Context mContext;
    CartAdapter cartAdapter;
    ArrayList<CartBean> cartBeanList;
    LinearLayoutManager layoutManager;

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_sumPrice)
    TextView tvSumPrice;
    @BindView(R.id.tv_savePrice)
    TextView tvSavePrice;
    @BindView(R.id.ll_cart_sum)
    LinearLayout llCartSum;
    @BindView(R.id.tv_cart_clean)
    TextView tvCartClean;

    public Fragment_Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__cart, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue)
        );

        cartBeanList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), cartBeanList);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        setCartLayout(false);
    }

    private void setCartLayout(boolean hasCart) {
        llCartSum.setVisibility(hasCart?View.VISIBLE:View.GONE);
        tvCartClean.setVisibility(hasCart?View.GONE: View.VISIBLE);
        recyclerView.setVisibility(hasCart?View.VISIBLE:View.GONE);
    }

    @Override
    protected void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    L.e("cart", "s=" + s);
                    cartBeanList = ResultUtils.getCartFromJson(s);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (cartBeanList != null && cartBeanList.size() > 0) {
                        cartAdapter.initData(cartBeanList);
                        setCartLayout(true);
                    }else{
                        setCartLayout(false);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    @Override
    protected void setListener() {
        receiver = new updateCartReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        mContext.registerReceiver(receiver, filter);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

   /* @Override //LocalBroadcastManager保密性较好
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("update goods count");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int cartCount = intent.getIntExtra("count", 0);
                cartAdapter.initCartCount(cartCount);
            }
        };
        broadcastManager.registerReceiver(receiver, filter);
    }*/

    private void sumPrice() {
        int sumPrice = 0;
        int rankPrice = 0;
        if (cartBeanList != null && cartBeanList.size() > 0) {
            for (CartBean c : cartBeanList) {
                if (c.isChecked()) {
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            tvSumPrice.setText("￥" + Double.valueOf(sumPrice));
            tvSavePrice.setText("￥" + Double.valueOf(rankPrice));
        } else {
            tvSumPrice.setText("￥0");
            tvSavePrice.setText("￥0");
        }
    }

    private int getPrice(String price) {
        String str = price.substring(price.indexOf("￥") + 1);
        int p = Integer.parseInt(str);
        return p;
    }

    class updateCartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            L.e("broadcast", "收到广播啦...");
            sumPrice();
            boolean isClean = intent.getBooleanExtra(I.ACTION_CART_IS_CLEAN, false);
            L.e("broadcast","isClean="+isClean);
            setCartLayout(isClean);
            boolean isUpdate = intent.getBooleanExtra(I.ACTION_UPDATE_CART,false);
            if(isUpdate){
                initData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterForContextMenu(recyclerView);
        }
    }
}
