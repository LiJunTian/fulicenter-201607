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
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class GoodsAdatper extends RecyclerView.Adapter {
    final static int TYPE_GOODS = 0;
    final static int TYPE_FOOTER = 1;
    ArrayList<NewGoodsBean> goodsList;
    Context context;

    String footerText;
    public GoodsAdatper(Context context, ArrayList<NewGoodsBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout;
        switch (viewType) {
            case TYPE_GOODS:
                layout = inflater.inflate(R.layout.goods_layout, parent, false);
                holder = new GoodsViewHolder(layout);
                break;
            case TYPE_FOOTER:
                layout = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footerLayout.setText(footerText);
            return;
        }
        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        NewGoodsBean goods = goodsList.get(position);
        goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
        goodsViewHolder.tvGoodsName.setText(goods.getCurrencyPrice());
//        goodsViewHolder.ivGoodsAvatar.setImageDrawable(R.drawable.);
    }

    @Override
    public int getItemCount() {
        return goodsList == null ? goodsList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_GOODS;
        }
    }


    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoodsAvatar)
        ImageView ivGoodsAvatar;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.goods_layout)
        RelativeLayout goodsLayout;
        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

     static class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.footer_layout)
        TextView footerLayout;

         FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
