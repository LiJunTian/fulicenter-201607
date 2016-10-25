package cn.ucai.fulicenter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Category extends Fragment {
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;
    @BindView(R.id.elv)
    ExpandableListView elv;

    public Fragment_Category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__category, container, false);
        L.e("CategoryGroupBean", "执行到onCreateView啦");
        initData();
        initView();
        setListener();
        ButterKnife.bind(this, view);
        return view;
    }

    protected void initView() {
        groupList = new ArrayList<>();
        childList = new ArrayList<>();

    }

    public void initData() {
        NetDao.downloadCategoryGroup(getContext(), new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null&&result.length>0){
                    groupList= ConvertUtils.array2List(result);
                    for( int i = 0;i<groupList.size();i++){
                        childList.add(new ArrayList<CategoryChildBean>());
                        int parentId = groupList.get(i).getId();
                        downLoadChild(parentId,i);
                    }

                    categoryAdapter = new CategoryAdapter(getContext(), groupList, childList);
                    elv.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    protected void setListener() {

    }

    public void downLoadChild(int parentId, final int index){
        NetDao.downloadCategoryChild(getContext(), parentId, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                if(result!=null&&result.length>0){
                    L.e("Category","CategoryChildBean，result="+result[0].toString());
                    ArrayList<CategoryChildBean> child = ConvertUtils.array2List(result);
                    childList.set(index,child);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
