package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.net.ImageLoader;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.ItemViewHolder> {
    Context context;
    ArrayList<BoutiqueBean> list;

    public void initData(ArrayList<BoutiqueBean> boutiqueList) {
        if (list != null) {
            list.clear();
        }
        list.addAll(boutiqueList);
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.boutique_layout, parent, false);
        ItemViewHolder holder = new ItemViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BoutiqueBean boutiqueBean = list.get(position);
        ImageLoader.downloadImg(context, holder.ivBoutiqueImage, boutiqueBean.getImageurl(), true);
        holder.tvBoutiqueTitle.setText(boutiqueBean.getTitle());
        holder.tvBoutiqueName.setText(boutiqueBean.getName());
        holder.tvBoutiqueDescription.setText(boutiqueBean.getDescription());
        holder.boutique.setTag(boutiqueBean);
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_boutiqueImage)
        ImageView ivBoutiqueImage;
        @BindView(R.id.tv_boutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tv_boutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tv_boutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.boutique)
        RelativeLayout boutique;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.boutique)
        public void OnBoutiqueItemClick(){
            BoutiqueBean boutiqueBean = (BoutiqueBean) boutique.getTag();
            int boutiqueId = boutiqueBean.getId();
            String title = boutiqueBean.getTitle();
            MFGT.gotoBoutiqueDetailActivity(context,boutiqueId,title);
        }
    }
}
