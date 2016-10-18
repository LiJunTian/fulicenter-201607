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
    List<Fragment> list = new ArrayList<>();

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
    }

    private void initFragment() {
        L.i("main","初始化fragment");
        mFragments = new Fragment[5];
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

        list.add(mFragment_newGoods);
        list.add(mFragment_Boutique);
        list.add(mFragment_Category);
        list.add(mFragment_Cart);
        list.add(mFragment_PersonCenter);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_FragmentLayout,mFragment_newGoods);
        transaction.add(R.id.main_FragmentLayout,mFragment_Boutique).hide(mFragment_Boutique);
        transaction.add(R.id.main_FragmentLayout,mFragment_Category).hide(mFragment_Category);
        transaction.add(R.id.main_FragmentLayout,mFragment_Cart).hide(mFragment_Cart);
        transaction.add(R.id.main_FragmentLayout,mFragment_PersonCenter).hide(mFragment_PersonCenter);
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
        L.i("onCheckedChange---");
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
        getSupportFragmentManager().beginTransaction().show(list.get(index)).hide(list.get(currentIndex)).commit();
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
