package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

public class LoginActivity extends BaseActivity {
    private static String TAG = LoginActivity.class.getSimpleName();//得到类的名字
    Context mContext;
    String userName;
    String password;
    @BindView(R.id.login_title)
    RelativeLayout loginTitle;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.ev_login_userName)
    EditText evLoginUserName;
    @BindView(R.id.et_login_rePassword)
    EditText etLoginRePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ButterKnife.bind(this);

        String name = SharePrefrenceUtils.getInstance(mContext).getUser();
        L.e(TAG, "name=" + name);
        if(name!=null){
            evLoginUserName.setText(name);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        /*SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String name = sp.getString("userName","");
        String pw = sp.getString("password","");
        L.e(TAG,"name="+name);
        L.e(TAG,"pw="+pw);*/
//        evLoginUserName.setText("name");
        /*if(name!=null&&!"".equals(name)){
            evLoginUserName.setText(name);
        }
        if(pw!=null&&!"".equals(pw)){
            etLoginRePassword.setText(pw);
        }*/
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.btn_register)
    public void onClick() {
//        MFGT.gotoRegisterActivity(this);
        startActivityForResult(new Intent(this, RegisterActivity.class), I.REQUEST_CODE_REGISTER);
    }

    @OnClick(R.id.title_back)
    public void onBackClick() {
        MFGT.finish(this);
//        MFGT.gotoMainActivity(this);
    }

    @OnClick(R.id.btn_login)
    public void onLogin() {
        userName = evLoginUserName.getText().toString();
        password = etLoginRePassword.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            evLoginUserName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            etLoginRePassword.requestFocus();
            return;
        }
        login();
    }

    private void login() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("登录中...");
        pd.show();
        NetDao.Login(mContext, userName, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                L.e(TAG, "result=" + result);
                if (result == null) {
                    CommonUtils.showLongToast(R.string.login_fail);
                } else {
                    if (result.isRetMsg()) {
                        User user = (User) result.getRetData();
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(user);
                        if (isSuccess) {
                            SharePrefrenceUtils.getInstance(mContext).saveUser(user.getMuserName());
                            FuLiCenterApplication.setUser(user);
                            setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME,userName));
                            MFGT.finish((Activity) mContext);
                        }
//                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));
                        /*SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("userName",userName);
                        edit.putString("password",password);
                        edit.commit();*/
//                        MFGT.finish((Activity) mContext);
//                        CommonUtils.showLongToast("登录成功");
//                        MFGT.gotoPersonCenter(mContext,userName);
                    } else if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                        CommonUtils.showLongToast(R.string.user_not_exist);
                    } else {
                        CommonUtils.showLongToast(R.string.wrong_password);
                    }
                }
                pd.dismiss();
               /* if (result.getRetCode() == 0) {
                    MFGT.gotoPersonCenter(mContext,userName);
                }*/
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            userName = data.getStringExtra(I.User.USER_NAME);
            evLoginUserName.setText(userName);
        }
    }
}
