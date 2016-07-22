package com.feicuiedu.treasure.treasure.home.list;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.treasure.treasure.TreasureRepo;

/**
 * 列表模式宝藏
 *
 * listView
 * TreasureRepo ----
 *
 * 5.0后,出的 RecylerView控件
 *
 * RecylerView控件 封装了ViewHolder,
 * LayoutManage,设定LAYOUT方式
 * 动画 (item,layout)
 * 对指定位置刷新
 *
 * 作者：yuanchao on 2016/7/22 0022 09:40
 * 邮箱：yuanchao@feicuiedu.com
 */
public class TreasureListFragment extends Fragment{

    private RecyclerView recyclerView;
    private TreasureListAdapter adapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(container.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return recyclerView;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TreasureListAdapter();
        recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                adapter.addItems(TreasureRepo.getInstance().getTreasure());
            }
        }, 50);
    }
}
