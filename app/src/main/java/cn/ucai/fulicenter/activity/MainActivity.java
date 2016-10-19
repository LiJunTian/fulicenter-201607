package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.Fragment_Boutique;
import cn.ucai.fulicenter.fragments.Fragment_Cart;
import cn.ucai.fulicenter.fragments.Fragment_Category;
import cn.ucai.fulicenter.fragments.Fragment_NewGoods;
import cn.ucai.fulicenter.fragments.Fragment_PersonCenter;

public class MainActivity extends AppCompatActivity {
    int index;
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
    @BindView(R.id.main_tvCount)
    TextView mainTvCount;
    @BindView(R.id.main_rbPersonalCenter)
    RadioButton mainRbPersonalCenter;
    @BindView(R.id.main_RadioGroup)
    LinearLayout mainRadioGroup;

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
        L.i("main","初始化fragment");
        mFragment_newGoods = new Fragment_NewGoods();
        mFragment_Boutique = new Fragment_Boutique();
        mFragment_Category = new Fragment_Category();
        mFragment_Cart = new Fragment_Cart();
        mFragment_PersonCenter = new Fragment_PersonCenter();
        /*getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_FragmentLayout,mFragment_newGoods)
                .show(mFragment_newGoods)
                .commit();*/
    }

    private void addFragment() {
        mFragments = new Fragment[5];
        mFragments[0] = mFragment_newGoods;
        mFragments[1] = mFragment_Boutique;
        mFragments[2] = mFragment_Category;
        mFragments[3] = mFragment_Cart;
        mFragments[4] = mFragment_PersonCenter;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_FragmentLayout,mFragments[0]);
        transaction.add(R.id.main_FragmentLayout,mFragments[1]).hide(mFragments[1]);
        transaction.add(R.id.main_FragmentLayout,mFragments[2]).hide(mFragments[2]);
        transaction.add(R.id.main_FragmentLayout,mFragments[3]).hide(mFragments[3]);
        transaction.add(R.id.main_FragmentLayout,mFragments[4]).hide(mFragments[4]);
        transaction.commit();
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
        L.i("onCheckedChange...");
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
                index = 3;
                break;
            case R.id.main_rbPersonalCenter:
                index = 4;
                break;
        }
        switchFragment(index);
        currentIndex = index;
        setRadioButtonStatus();
    }

    private void switchFragment(int index) {
        if(currentIndex==index){
            return;
        }
        L.i("main","index="+index+",currentIndex="+currentIndex);
        getSupportFragmentManager().beginTransaction().show(mFragments[index]).hide(mFragments[currentIndex]).commit();
    }

    private void setRadioButtonStatus() {
        for(int i = 0;i<rbs.length;i++){
            if(i == index){
                rbs[i].setChecked(true);
            }else{
                rbs[i].setChecked(false);
            }
        }
    }
}
