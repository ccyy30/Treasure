package com.feicuiedu.treasure.treasure.home.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.treasure.components.TreasureView;
import com.feicuiedu.treasure.treasure.Treasure;
import com.feicuiedu.treasure.treasure.home.detail.TreasureDetailActivity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 作者：yuanchao on 2016/7/22 0022 10:21
 * 邮箱：yuanchao@feicuiedu.com
 */
public class TreasureListAdapter extends RecyclerView.Adapter<TreasureListAdapter.MyViewHolder> {

    private ArrayList<Treasure> datas = new ArrayList<Treasure>();

    public final void addItems(Collection<Treasure> items) {
        if (items != null) {
            datas.addAll(items);
            notifyItemRangeChanged(0,datas.size());
        }
    }

    // 创建ViewHolder对象
    @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TreasureView treasureView = new TreasureView(parent.getContext());
        return new MyViewHolder(treasureView);
    }

    // (将数据)绑定到ViewHolder上
    @Override public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure treasure = datas.get(position);
        holder.treasureView.bindTreasure(treasure);
        holder.treasureView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                TreasureDetailActivity.open(v.getContext(), treasure);
            }
        });
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    static final class MyViewHolder extends RecyclerView.ViewHolder {
        private TreasureView treasureView;

        public MyViewHolder(TreasureView itemView) {
            super(itemView);
            this.treasureView = itemView;
        }
    }
}
