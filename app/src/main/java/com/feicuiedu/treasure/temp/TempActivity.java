package com.feicuiedu.treasure.temp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.feicuiedu.treasure.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

// 页面： 一个lISTvIEW来展示信息
// 通过web接口获取数据

// 业务 和 视图  要分离
// 一个地方写视图
// 一个地方写业务(视图业务和用例,在小的项目中很多时候混到一起了)
//      做部分业务,及控制视图(调度视图)
// 业务中要持有视图对象

public class TempActivity extends AppCompatActivity implements TempView{

    @Bind(R.id.listView) ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        new TempPresenter(this).loadData();
    }

    private ProgressDialog progressDialog = null;

    @Override public void showProgess() {
        progressDialog = ProgressDialog.show(this,"","loading");
    }

    @Override public void hideProgess() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override public void setData(List<String> datas) {
        adapter.addAll(datas);
        adapter.notifyDataSetChanged();
    }
}


