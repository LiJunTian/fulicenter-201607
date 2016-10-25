package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class RevisePersonalActivity extends BaseActivity {
    private final String TAG = RevisePersonalActivity.class.getSimpleName();
    User user;
    Context mContext;

    String userName;
    String nick;
    String reNick;
    @BindView(R.id.iv_revise_back)
    ImageView ivReviseBack;
    @BindView(R.id.et_revise_nick)
    EditText etReviseNick;
    @BindView(R.id.btn_revise_save)
    Button btnReviseSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_revise_personal);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);

        userName = getIntent().getStringExtra(I.User.USER_NAME);
         nick = getIntent().getStringExtra(I.User.NICK);
         reNick = etReviseNick.getText().toString();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            etReviseNick.setText(user.getMuserNick());
            etReviseNick.setSelectAllOnFocus(true);
        }else{
            finish();
        }
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.iv_revise_back)
    public void onBack(){
        MFGT.finish(this);
    }

    @OnClick(R.id.btn_revise_save)
    public void onSave(){
        if(user!=null){
            String nick = etReviseNick.getText().toString();
            if(nick.equals(user.getMuserNick())){
                CommonUtils.showLongToast("昵称与原昵称一致");
            }else if(TextUtils.isEmpty(nick)){
                CommonUtils.showLongToast("昵称为空");
            }else{
                updateNick(user.getMuserName(),nick);
            }
        }
    }

    private void updateNick(String userName,String nick) {
        NetDao.updateNick(mContext, userName, nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s,User.class);
                if(result!=null){
                    if(result.isRetMsg()){
                        L.e(TAG,"result="+result);
                        User user = (User) result.getRetData();
                        L.e(TAG,"user="+user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.updateUser(user);
                        if(isSuccess){
                            FuLiCenterApplication.setUser(user);
                            setResult(RESULT_OK);
                            MFGT.finish((Activity) mContext);
                        }
                        CommonUtils.showLongToast("昵称更新成功");
                    }else if(result.getRetCode()==I.MSG_USER_SAME_NICK){
                        CommonUtils.showLongToast("昵称未修改");
                    }else{
                        CommonUtils.showLongToast("昵称修改失败");
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
