package cn.ucai.fulicenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.footer_layout)
    public
    TextView footerLayout;

    public FooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
