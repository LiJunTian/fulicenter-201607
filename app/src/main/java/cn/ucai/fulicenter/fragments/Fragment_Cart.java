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
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Cart extends BaseFragment {
    private final String TAG = Fragment_Cart.class.getSimpleName();
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
    @BindView(R.id.tv_cart_buy)
    TextView tvCartBuy;

    String cartIds = "";
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
        llCartSum.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        tvCartClean.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(hasCart ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    L.e(TAG, "s=" + s);
                    cartBeanList = ResultUtils.getCartFromJson(s);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (cartBeanList != null && cartBeanList.size() > 0) {
                        cartAdapter.initData(cartBeanList);
                        setCartLayout(true);
                    } else {
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
        cartIds = "";
        if (cartBeanList != null && cartBeanList.size() > 0) {
            for (CartBean c : cartBeanList) {
                if (c.isChecked()) {
                    cartIds += c.getId()+",";
                    L.e(TAG,"cartIds="+cartIds);
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

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    class updateCartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            L.e(TAG, "收到广播啦...");
            sumPrice();
            boolean isClean = intent.getBooleanExtra(I.ACTION_CART_IS_CLEAN, false);
            L.e(TAG,"isClean=" + isClean);
            if (isClean) {
                setCartLayout(false);
            }

            boolean isUpdate = intent.getBooleanExtra(I.ACTION_UPDATE_CART, false);
            if (isUpdate) {
                initData();
            }

            /*boolean isPaySuccess = intent.getBooleanExtra(I.PAY_SUCCESS,false);
            if(isPaySuccess){
               String[] ids = cartIds.split(",");
                for(String id : ids){
                    int goodId = Integer.parseInt(id);
                    NetDao.deleteCart(context, goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                initData();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterForContextMenu(recyclerView);
        }
    }

    @OnClick(R.id.tv_cart_buy)
    public void OnBuy(){
        if(cartIds!=null&&cartIds.length()>0){
            MFGT.gotoPayActivity(mContext,cartIds);
        }else{
            CommonUtils.showLongToast("还没添加商品呢");
        }
    }
}
