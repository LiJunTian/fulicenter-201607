package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.net.ImageLoader;
import cn.ucai.fulicenter.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    String textFooter;
    Context context;
    ArrayList<BoutiqueBean> list;
    boolean isMore;

    public String getTextFooter() {
        return textFooter;
    }

    public void setTextFooter(String textFooter) {
        this.textFooter = textFooter;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void initData(ArrayList<BoutiqueBean> boutiqueList){
        if(list!=null){
            list.clear();
        }
        list.addAll(boutiqueList);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<BoutiqueBean> boutiqueList){
        list.addAll(boutiqueList);
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.boutique_layout, parent, false);
                holder = new ItemViewHolder(layout);
                break;
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==getItemCount()-1){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footerLayout.setText(textFooter);
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        BoutiqueBean boutiqueBean = list.get(position);
        ImageLoader.downloadImg(context,itemViewHolder.ivBoutiqueImage,boutiqueBean.getImageurl(),true);
        itemViewHolder.tvBoutiqueTitle.setText(boutiqueBean.getTitle());
        itemViewHolder.tvBoutiqueName.setText(boutiqueBean.getName());
        itemViewHolder.tvBoutiqueDescription.setText(boutiqueBean.getDescription());

    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_boutiqueImage)
        ImageView ivBoutiqueImage;
        @BindView(R.id.tv_boutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tv_boutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tv_boutiqueDescription)
        TextView tvBoutiqueDescription;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
