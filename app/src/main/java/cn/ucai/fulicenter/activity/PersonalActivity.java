package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.I;

public class PersonalActivity extends BaseActivity {

    @BindView(R.id.iv_personal_avatar)
    ImageView ivPersonalAvatar;
    @BindView(R.id.tv_personal_userName)
    TextView tvPersonalUserName;
    @BindView(R.id.tv_personal_nick)
    TextView tvPersonalNick;
    @BindView(R.id.iv_personal_nick)
    ImageView ivPersonalNick;
    @BindView(R.id.iv_personal_qrcode)
    ImageView ivPersonalQrcode;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        String userName = getIntent().getStringExtra(I.User.USER_NAME);
        String nick = getIntent().getStringExtra(I.User.NICK);
        tvPersonalUserName.setText(userName);
        tvPersonalNick.setText(nick);
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
}
