package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
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

public class PayActivity extends BaseActivity implements PaymentHandler {
    private final String TAG = PayActivity.class.getSimpleName();
    private static String URL = "http://218.244.151.190/demo/charge";
    Context mContext;
    User user;
    String cartIds;
    String[] ids;
    ArrayList<CartBean> mList;
    @BindView(R.id.pay_sumMoney)
    TextView paySumMoney;
    @BindView(R.id.iv_pay_back)
    ImageView ivPayBack;

    int rankPrice;
    @BindView(R.id.et_pay_userName)
    EditText etPayUserName;
    @BindView(R.id.et_pay_phone)
    EditText etPayPhone;
    @BindView(R.id.ev_pay_street)
    EditText evPayStreet;
    @BindView(R.id.btn_pay_pay)
    Button btnPayPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        L.e(TAG, "cartIds=" + cartIds);

        user = FuLiCenterApplication.getUser();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }else{
            ids = cartIds.split(",");
            getOrderList();
        }
    }

    private void getOrderList() {
        NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(result);
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mList.addAll(list);
                    SumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void initView() {

    }


    private void SumPrice() {
        rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        rankPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    }
                }
            }
            paySumMoney.setText("总价:￥" + rankPrice);
            L.e(TAG,"rankPrice"+rankPrice);
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    @OnClick(R.id.iv_pay_back)
    public void OnBack() {
        MFGT.finish(this);
    }

    @OnClick(R.id.btn_pay_pay)
    public void OnPay() {
        String userName = etPayUserName.getText().toString();
        String phone = etPayPhone.getText().toString();
        String street = evPayStreet.getText().toString();
        if(TextUtils.isEmpty(userName)){
            CommonUtils.showLongToast("用户名不能为空");
            etPayUserName.requestFocus();
            return;
        }else if(TextUtils.isEmpty(phone)){
            CommonUtils.showLongToast("手机号码不能为空");
            etPayPhone.requestFocus();
            return;
        }else if(TextUtils.isEmpty(street)){
            CommonUtils.showLongToast("街道地址不能为空");
            evPayStreet.requestFocus();
            return;
        }
        gotoStatement();
    }

    private void gotoStatement() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
        L.e(TAG,"执行到对话框啦");
    }

    @Override
    public void handlePaymentResult(Intent data) {
        L.e(TAG,"执行到handlePaymentResult啦");
        if (data != null) {
            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果
            L.e(TAG,"code=" + data.getExtras().getInt("code"));
            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    L.e(TAG,result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
         int code = data.getExtras().getInt("code");
            switch(code){
                case 1:
                    //1.使用广播发送成功支付，到cart让其刷新数据
//                    this.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART).putExtra(I.PAY_SUCCESS,true));
                    //2.在地址界面，支付成功直接刷新购物车,在Fragment_Cart中调用onResume方法:initData();
                    for(String id : ids) {
                        int goodId = Integer.parseInt(id);
                        NetDao.deleteCart(mContext, goodId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    L.e(TAG,"删除已购买商品成功");
                                }
                            }

                            @Override
                            public void onError(String error) {
                                L.e(TAG,"error="+error);
                            }
                        });
                    }
                    MFGT.finish((Activity) mContext);
                    break;
                case -1:
                    CommonUtils.showLongToast("支付失败");
                    break;
            }
        }
    }
}
