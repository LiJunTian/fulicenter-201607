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
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.ImageLoader;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class CollectAdapter extends RecyclerView.Adapter {
    User user = FuLiCenterApplication.getUser();
    ArrayList<CollectBean> goodsList;
    Context context;
    boolean isMore;


    public CollectAdapter(Context context, ArrayList<CollectBean> goodsList) {
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
                layout = inflater.inflate(R.layout.collect_item, parent, false);
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
        final CollectBean goods = goodsList.get(position);
        goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
        ImageLoader.downloadImg(context, goodsViewHolder.ivGoodsAvatar, goods.getGoodsThumb(), true);
//        goodsViewHolder.ivDelete.setImageResource(R.mipmap.delete);
        goodsViewHolder.goodsLayout.setTag(goods);

    }

    public int getText() {
        return isMore() ? R.string.now_refresh : R.string.no_more;
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

    public void initData(ArrayList<CollectBean> list) {
        if (goodsList != null) {
            goodsList.clear();
        }
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> list) {
        this.goodsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData(int i){
        this.goodsList.remove(i);
        notifyDataSetChanged();
    }
    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.ivGoodsAvatar)
        ImageView ivGoodsAvatar;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.goods_layout)
        RelativeLayout goodsLayout;
        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.goods_layout)
        public void onGoodsItemClick() {
            CollectBean collect = (CollectBean) goodsLayout.getTag();
            MFGT.gotoGoodsDetailActivity(context, collect.getGoodsId());
        }

        @OnClick(R.id.iv_delete)
        public void delete(){
            final CollectBean collect = (CollectBean) goodsLayout.getTag();
            NetDao.deleteCollect(context,collect.getGoodsId(),user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        goodsList.remove(collect);
                        notifyDataSetChanged();
                        CommonUtils.showLongToast("商品删除成功");
                    }else{
                        CommonUtils.showLongToast(result!=null?result.getMsg():"删除失败");
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("error="+error);
                    CommonUtils.showLongToast("删除失败");
                }
            });
        }
    }
}
