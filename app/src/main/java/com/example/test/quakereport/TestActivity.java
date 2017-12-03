package com.example.test.quakereport;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 查询结果显示
 */
public class TestActivity extends Activity {
    private ArrayList<Earthquake> earthquakes; //查询返回列表
    private ListView earthquakeListView;//显示视图
    private EarthquakeAdapter adapter;//适配器
    private String endTime;//截止时间
    private String startTime;//开始时间
    private String level;//地震等级

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);//显示布局
        earthquakeListView = (ListView) findViewById(R.id.list);//绑定视图
        initControl();
        new MyTask().execute();//后台开始异步任务
    }

    /**
     * 获取传递过来的参数
     */
    private void initControl() {
        level = getIntent().getStringExtra("level");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
    }

    /**
     * 自定义任务
     * 完成访问地址
     * 获取传回的json值
     * 通过工具类方法extractEarthquakes进行处理返回数据
     * 绑定适配器
     */
    class MyTask extends AsyncTask<Void, Void, Void> {
        //这是在主线程进行的更新
        @Override
        protected void onProgressUpdate(Void... values) {
             //视图被点击后跳转URL
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // 查找单击的当前地震
                    Earthquake currentEarthquake = adapter.getItem(position);

                    // 将字符串 URL 转换为 URI 对象（以传递至 Intent 中 constructor)
                    Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                    // 创建一个新的 Intent 以查看地震 URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // 发送 Intent 以启动新活动
                    startActivity(websiteIntent);
                }
            });
            //绑定适配器
            earthquakeListView.setAdapter(adapter);
            super.onProgressUpdate(values);
        }

        //子线程进行的任务
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                earthquakes = QueryUtils.extractEarthquakes(startTime, endTime, level);//通过工具类方法extractEarthquakes进行处理返回数据
                adapter = new EarthquakeAdapter(TestActivity.this, earthquakes);//创建适配器
                publishProgress();//传递更新
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
