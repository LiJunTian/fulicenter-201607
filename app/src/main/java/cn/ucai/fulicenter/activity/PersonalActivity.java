package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.ImageLoader;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;

public class PersonalActivity extends BaseActivity {
    Context mContext;
    User user;
    OnSetAvatarListener osal;

    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.iv_personal_avatar)
    ImageView ivPersonalAvatar;
    @BindView(R.id.iv_personal_setAvatar)
    ImageView ivPersonalSetAvatar;
    @BindView(R.id.tv_personal_userName)
    TextView tvPersonalUserName;
    @BindView(R.id.iv_personal_setUserName)
    ImageView ivPersonalSetUserName;
    @BindView(R.id.tv_personal_nick)
    TextView tvPersonalNick;
    @BindView(R.id.iv_personal_setNick)
    ImageView ivPersonalSetNick;
    @BindView(R.id.iv_personal_qrcode)
    ImageView ivPersonalQrcode;
    @BindView(R.id.ll_avatar_layout_set)
    LinearLayout llAvatarLayoutSet;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        }
        initInfo();
    }

    private void initInfo() {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivPersonalAvatar);
        tvPersonalUserName.setText(user.getMuserName());
        tvPersonalNick.setText(user.getMuserNick());
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        MFGT.finish((Activity) mContext);
    }

    @OnClick(R.id.iv_personal_setAvatar)
    public void onSetAvatar() {
        osal = new OnSetAvatarListener((Activity) mContext, R.id.ll_avatar_layout_set, user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
    }

    @OnClick(R.id.iv_personal_setNick)
    public void onSetNick() {
//        startActivityForResult(new Intent(this, RevisePersonalActivity.class), I.REQUEST_CODE_UPDATE_NICK);
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("修改昵称")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (user != null) {
                            String nick = editText.getText().toString();
                            if (nick.equals(user.getMuserNick())) {
                                CommonUtils.showLongToast("昵称与原昵称一致");
                            } else if (TextUtils.isEmpty(nick)) {
                                CommonUtils.showLongToast("昵称为空");
                            } else {
                                updateNick(user.getMuserName(), nick);
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void updateNick(String userName, String nick) {
        NetDao.updateNick(mContext, userName, nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    if (result.isRetMsg()) {
//                        L.e(TAG,"result="+result);
                        User user = (User) result.getRetData();
//                        L.e(TAG,"user="+user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.updateUser(user);
                        if (isSuccess) {
                            FuLiCenterApplication.setUser(user);
                            initData();
//                            setResult(RESULT_OK);
//                            MFGT.finish((Activity) mContext);
                        }
                        CommonUtils.showLongToast("昵称更新成功");
                    } else if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                        CommonUtils.showLongToast("昵称未修改");
                    } else {
                        CommonUtils.showLongToast("昵称修改失败");
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK&&requestCode == I.REQUEST_CODE_UPDATE_NICK){
        if (resultCode != RESULT_OK) {
            return;
        }
        /*if (requestCode == I.REQUEST_CODE_UPDATE_NICK) {
            initData();
        }*/
        osal.setAvatar(requestCode, data, ivPersonalAvatar);
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
//        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarFile((Activity) mContext, user.getMavatarPath() + "/" + user.getMuserName()) + I.AVATAR_SUFFIX_JPG);
        L.e("file=" + file.exists());
        L.e("file=" + file.getAbsolutePath());
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("用户头像更新中...");
        pd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s=" + s);
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result == null) {
                    CommonUtils.showLongToast("头像更新失败");
                } else {
                    User u = (User) result.getRetData();
                    if (result.isRetMsg()) {
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, ivPersonalAvatar);
                        CommonUtils.showLongToast("头像更新成功");
                    } else {
                        CommonUtils.showLongToast("头像更新失败");
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast("头像更新失败");
                L.e("error=" + error);
            }
        });
    }

    @OnClick(R.id.iv_personal_setUserName)
    public void onSetUserName() {
        CommonUtils.showLongToast("不能修改用户名!!!");
    }

    @OnClick(R.id.btn_exit)
    public void onExit() {
        if (user != null) {
            SharePrefrenceUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
        finish();
    }


}
