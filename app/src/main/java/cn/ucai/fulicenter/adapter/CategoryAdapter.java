package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.MFGT;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.ImageLoader;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    static final String TAG = CategoryAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;
    @BindView(R.id.category_child_cardview)
    CardView categoryChildCardview;


    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.mContext = mContext;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList.get(groupPosition) != null ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.category_item, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = groupList.get(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.ivCategoryAvatar, group.getImageUrl());
            holder.tvCategoryName.setText(group.getName());
            holder.ivCategoryArrow.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }
        final CategoryChildBean child = childList.get(groupPosition).get(childPosition);
        if (child != null) {
//            final String title = groupList.get(groupPosition).getName();
            ImageLoader.downloadImg(mContext, holder.ivChildImage, child.getImageUrl());
            holder.tvChildName.setText(child.getName());
            holder.categoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<CategoryChildBean> list = childList.get(groupPosition);
                    int id = child.getId();
                    String title = groupList.get(groupPosition).getName();
                    L.e(TAG,list.get(0).toString());
                    MFGT.gotoCategoryChildActivity(mContext,id,title,list);
                }
            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    class GroupViewHolder {
        @BindView(R.id.iv_category_avatar)
        ImageView ivCategoryAvatar;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.iv_category_arrow)
        ImageView ivCategoryArrow;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_child_image)
        ImageView ivChildImage;
        @BindView(R.id.tv_child_name)
        TextView tvChildName;
        @BindView(R.id.category_child)
        RelativeLayout categoryChild;
        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
