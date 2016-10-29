package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.adapter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.Fragment_Boutique;
import cn.ucai.fulicenter.fragments.Fragment_Cart;
import cn.ucai.fulicenter.fragments.Fragment_Category;
import cn.ucai.fulicenter.fragments.Fragment_NewGoods;
import cn.ucai.fulicenter.fragments.Fragment_PersonCenter;

public class MainActivity extends AppCompatActivity {
    final String TAG = MainActivity.class.getSimpleName();
    int index = 0;
    int currentIndex = 0;
    Fragment[] mFragments;
    Fragment_NewGoods mFragment_newGoods;
    Fragment_Boutique mFragment_Boutique;
    Fragment_Category mFragment_Category;
    Fragment_Cart mFragment_Cart;
    Fragment_PersonCenter mFragment_PersonCenter;

    RadioButton[] rbs;
    @BindView(R.id.main_rbNewGoods)
    RadioButton mainRbNewGoods;
    @BindView(R.id.main_rbBoutique)
    RadioButton mainRbBoutique;
    @BindView(R.id.main_rbCategory)
    RadioButton mainRbCategory;
    @BindView(R.id.main_rbCart)
    RadioButton mainRbCart;
    @BindView(R.id.main_rbPersonalCenter)
    RadioButton mainRbPersonalCenter;
    @BindView(R.id.main_RadioGroup)
    LinearLayout mainRadioGroup;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment();
        addFragment();

    }

    private void initFragment() {
        L.i(TAG,"初始化fragment");
        mFragment_newGoods = new Fragment_NewGoods();
        mFragment_Boutique = new Fragment_Boutique();
        mFragment_Category = new Fragment_Category();
        mFragment_Cart = new Fragment_Cart();
        mFragment_PersonCenter = new Fragment_PersonCenter();
    }

    private void addFragment() {
        mFragments = new Fragment[5];
        mFragments[0] = mFragment_newGoods;
        mFragments[1] = mFragment_Boutique;
        mFragments[2] = mFragment_Category;
        mFragments[3] = mFragment_Cart;
        mFragments[4] = mFragment_PersonCenter;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_FragmentLayout,mFragments[0]).show(mFragments[0]).commit();
    }

    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = mainRbNewGoods;
        rbs[1] = mainRbBoutique;
        rbs[2] = mainRbCategory;
        rbs[3] = mainRbCart;
        rbs[4] = mainRbPersonalCenter;
    }
    public void onCheckedChange(View view){
        L.e(TAG,"onCheckedChange");
        switch(view.getId()){
            case R.id.main_rbNewGoods:
                index = 0;
                break;
            case R.id.main_rbBoutique:
                index = 1;
                break;
            case R.id.main_rbCategory:
                index = 2;
                break;
            case R.id.main_rbCart:
                if(FuLiCenterApplication.getUser()==null){
                    startActivityForResult(new Intent(this,LoginActivity.class),I.REQUEST_CODE_CART);
                }else{
                    index = 3;
                }
                break;
            case R.id.main_rbPersonalCenter:
                if(FuLiCenterApplication.getUser()==null){
                    startActivityForResult(new Intent(this,LoginActivity.class),I.REQUEST_CODE_LOGIN);
                }else{
                    index = 4;
                }
                break;
        }
        switchFragment(index);
    }

    private void switchFragment(int index) {
        setRadioButtonStatus(index);
        if(currentIndex==index){
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = mFragments[index];
        if(!fragment.isAdded()){
            ft.add(R.id.main_FragmentLayout,fragment);
        }
        ft.show(fragment).hide(mFragments[currentIndex]).commit();
        currentIndex = index;
    }

    private void setRadioButtonStatus(int index) {
        for(int i = 0;i<rbs.length;i++){
            if(i == index){
                rbs[i].setChecked(true);
            }else{
                rbs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG,"index="+index);
        if(index==4&&FuLiCenterApplication.getUser()==null){
            //switchFragment(0);
            index=0;
        }
        switchFragment(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_LOGIN){
            User user = (User) data.getSerializableExtra(I.User.USER_NAME);
            if(user!=null){
                index = 4;
//              switchFragment(4);
            }
        }

        if(resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_CART){
            User user = (User) data.getSerializableExtra(I.User.USER_NAME);
            if(user!=null){
                index = 3;
//                switchFragment(3);
            }
        }
    }
}
