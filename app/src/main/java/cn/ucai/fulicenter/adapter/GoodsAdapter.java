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
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FooterViewHolder;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.L;
import cn.ucai.fulicenter.MFGT;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.ImageLoader;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    ArrayList<NewGoodsBean> goodsList;
    Context context;

    static ArrayList<NewGoodsBean> compareList;

    boolean isMore;
    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public boolean getFooterText() {
        return isMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.goods_layout, parent, false);
                holder = new GoodsViewHolder(layout);
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
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footerLayout.setText(getText());
            return;
        }
        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        NewGoodsBean goods = goodsList.get(position);
        goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
        goodsViewHolder.tvGoodsPrice.setText(goods.getCurrencyPrice());
        ImageLoader.downloadImg(context,goodsViewHolder.ivGoodsAvatar,goods.getGoodsThumb(),true);
        goodsViewHolder.goodsLayout.setTag(goods.getGoodsId());
    }

    public int getText(){
        return isMore()? R.string.now_refresh:R.string.no_more;
    }
    @Override
    public int getItemCount() {
        return goodsList == null ? 1 : goodsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<NewGoodsBean> list){
        if(goodsList!=null){
            goodsList.clear();
        }
        compareList = list;
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewGoodsBean> list){
        compareList = list;
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }
     class GoodsViewHolder extends RecyclerView.ViewHolder{
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
        @OnClick(R.id.goods_layout)
        public void onGoodsItemClick(){
            int goodsId = (int) goodsLayout.getTag();
            MFGT.gotoGoodsDetailActivity(context,goodsId);
        }
    }


    public void setSortBy(int sortType) {
        PriceCompare priceCompare;
        AddTimeCompare addTimeCompare;
        switch (sortType) {
            case I.SORT_BY_PRICE_ASC:
                priceCompare = new PriceCompare();
                priceCompare.sortTypeId = 0;
                Collections.sort(compareList, priceCompare);
                break;
            case I.SORT_BY_PRICE_DESC:
                priceCompare = new PriceCompare();
                priceCompare.sortTypeId = 1;
                Collections.sort(compareList,priceCompare);
                break;
            case I.SORT_BY_ADDTIME_ASC:
                addTimeCompare = new AddTimeCompare();
                addTimeCompare.sortTypeId = 2;
                Collections.sort(compareList,addTimeCompare);
                break;
            case I.SORT_BY_ADDTIME_DESC:
                addTimeCompare = new AddTimeCompare();
                addTimeCompare.sortTypeId = 3;
                Collections.sort(compareList,addTimeCompare);
                break;
        }
        initData(compareList);
    }

    private class PriceCompare implements Comparator<NewGoodsBean>{
        int sortTypeId;
        @Override
        public int compare(NewGoodsBean bean1, NewGoodsBean bean2) {
            String str1 = bean1.getCurrencyPrice();
            str1 = str1.substring(str1.indexOf("￥")+1);
            String str2 = bean2.getCurrencyPrice();
            str2 = str2.substring(str2.indexOf("￥")+1);
            if(sortTypeId==0){
                return Integer.parseInt(str1)-Integer.parseInt(str2);
            }
            return Integer.parseInt(str2)-Integer.parseInt(str1);
        }
    }

    private class AddTimeCompare implements Comparator<NewGoodsBean>{
        int sortTypeId;
        @Override
        public int compare(NewGoodsBean bean1, NewGoodsBean bean2) {
            String str1 = bean1.getAddTime();
            String str2 = bean2.getAddTime();
            if(sortTypeId==2){
                return (int) (Long.valueOf(str1)-Long.valueOf(str2));
            }
            return (int) (Long.valueOf(str2)-Long.valueOf(str1));
        }
    }
}
