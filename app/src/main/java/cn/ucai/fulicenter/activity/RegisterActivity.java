package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.CommonUtils;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.MFGT;
import cn.ucai.fulicenter.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.OkHttpUtils;

public class RegisterActivity extends BaseActivity {
    private static String TAG = "RegisterActivity";
    RegisterActivity mContext;
    String userName;
    String nick;
    String passWord;
    String rePassword;
    @BindView(R.id.register_title_back)
    ImageView registerTitleBack;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_nick)
    EditText etRegisterNick;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.btn_register_register)
    Button btnRegisterRegister;
    @BindView(R.id.ev_register_rePassword)
    EditText evRegisterRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.register_title_back)
    public void onClick() {
        MFGT.finish(this);
    }

    @OnClick(R.id.btn_register_register)
    public void onRegister() {
        userName = etRegisterUsername.getText().toString();
        nick = etRegisterNick.getText().toString();
        passWord = etRegisterPassword.getText().toString();
        rePassword = evRegisterRePassword.getText().toString();
        if(TextUtils.isEmpty(userName)){
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            etRegisterUsername.requestFocus();
            return;
        }else if(!userName.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            etRegisterUsername.requestFocus();
            return;
        }else if(TextUtils.isEmpty(nick)){
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            etRegisterNick.requestFocus();
            return;
        }else if(TextUtils.isEmpty(passWord)){
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            etRegisterPassword.requestFocus();
            return;
        } else if(TextUtils.isEmpty(rePassword)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            etRegisterPassword.requestFocus();
            return;
        } else if(!passWord.equals(rePassword)){
            CommonUtils.showShortToast(R.string.two_input_password);
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
            NetDao.Register(mContext, userName, nick, passWord, new OkHttpUtils.OnCompleteListener<Result>() {
                @Override
                public void onSuccess(Result result) {
                    pd.dismiss();
                    if(result==null){
                        CommonUtils.showLongToast(R.string.register_fail);
                    }else {
                        if(result.isRetMsg()){
                            CommonUtils.showLongToast(R.string.register_success);
                            setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showLongToast(R.string.register_fail_exists);
                            etRegisterUsername.requestFocus();
                        }
                    }
                }
                @Override
                public void onError(String error) {
                    pd.dismiss();
                }
            });
    }
}
