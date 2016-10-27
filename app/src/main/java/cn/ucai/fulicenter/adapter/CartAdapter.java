package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.net.ImageLoader;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {
    Context context;
    ArrayList<CartBean> list;

    public void initData(ArrayList<CartBean> boutiqueList) {
        if (list != null) {
            list.clear();
        }
        list.addAll(boutiqueList);
        notifyDataSetChanged();
    }

    public CartAdapter(Context context, ArrayList<CartBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.cart_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CartBean cartBean = list.get(position);
        holder.tvCartGoodName.setText(cartBean.getGoods().getGoodsName());
        holder.tvCartPrice.setText(cartBean.getGoods().getCurrencyPrice());
        holder.checkbox.setChecked(cartBean.isChecked());
        ImageLoader.downloadImg(context,holder.ivCartImg,cartBean.getGoods().getGoodsThumb());
//        holder.boutique.setTag(boutiqueBean);
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size();
    }

     class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.iv_cart_img)
        ImageView ivCartImg;
        @BindView(R.id.tv_cart_goodName)
        TextView tvCartGoodName;
        @BindView(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @BindView(R.id.tv_cart_count)
        TextView tvCartCount;
        @BindView(R.id.iv_cart_del)
        ImageView ivCartDel;
        @BindView(R.id.tv_cart_price)
        TextView tvCartPrice;

         ItemViewHolder(View view) {
             super(view);
            ButterKnife.bind(this, view);
        }
    }
}
