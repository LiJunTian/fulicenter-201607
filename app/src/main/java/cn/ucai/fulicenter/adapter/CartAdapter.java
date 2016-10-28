package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.ImageLoader;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.MFGT;

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
//        list.addAll(boutiqueList);
        list = boutiqueList;
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
        final CartBean cartBean = list.get(position);
        final GoodsDetailsBean goods = cartBean.getGoods();
        if (goods != null) {
            ImageLoader.downloadImg(context, holder.ivCartImg, goods.getGoodsThumb());
            holder.tvCartGoodName.setText(goods.getGoodsName());
            holder.tvCartPrice.setText(goods.getCurrencyPrice());
            holder.llCartItem.setTag(goods);
           /* holder.llCartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoGoodsDetailActivity(context, goods.getGoodsId());
                }
            });*/
        }
        int CartCount = cartBean.getCount();
        holder.tvCartCount.setText("(" + CartCount + ")");
        holder.checkbox.setChecked(cartBean.isChecked());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cartBean.setChecked(b);
                context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
            }
        });

        holder.ivCartAdd.setTag(position);
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
        @BindView(R.id.ll_cart_item)
        LinearLayout llCartItem;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.iv_cart_img,R.id.tv_cart_goodName,R.id.tv_cart_price})
        public void gotoDetail(){
            GoodsDetailsBean details = (GoodsDetailsBean) llCartItem.getTag();
            MFGT.gotoGoodsDetailActivity(context, details.getGoodsId());
        }

        @OnClick(R.id.iv_cart_add)
        public void onCartAdd() {
            final int position = (int) ivCartAdd.getTag();
            CartBean cart = list.get(position);
            NetDao.updateCart(context, cart.getId(), cart.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        list.get(position).setCount(list.get(position).getCount() + 1);
                        context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                        tvCartCount.setText("(" + (list.get(position).getCount()) + ")");
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @OnClick(R.id.iv_cart_del)
        public void onCartDel() {
            final int position = (int) ivCartAdd.getTag();
            CartBean cart = list.get(position);
            if (cart.getCount() > 1) {
                NetDao.updateCart(context, cart.getId(), cart.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            list.get(position).setCount(list.get(position).getCount() - 1);
                            context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                            tvCartCount.setText("(" + (list.get(position).getCount()) + ")");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
//                 tvCartCount.setText("("+0+")");
                NetDao.deleteCart(context, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            if(list.size()==1){
                                context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART).putExtra(I.ACTION_CART_IS_CLEAN, true));
                            }
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
}
