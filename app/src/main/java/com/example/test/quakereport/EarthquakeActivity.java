/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.test.quakereport;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 查询主界面
 */
public class EarthquakeActivity extends AppCompatActivity {

    private EditText startTime;//开始时间
    private EditText endTime;//截止时间
    private EditText level;//地震等级
    private Button submit;//查询按钮
    private Calendar c = Calendar.getInstance();//获取日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        initControl();
    }

    /**
     * 初始化步骤
     * 主要是绑定视图
     */
    private void initControl() {
        startTime = (EditText) findViewById(R.id.startTime);//开始时间EditText视图绑定
        endTime=(EditText) findViewById(R.id.endTime);//截止时间EditText视图绑定
        level = (EditText) findViewById(R.id.level);//地震等级EditText视图绑定
    }

    /**
     * 开始时间
     * 点击弹出日期选择
     * @param view
     */
    public void startTime(View view ){
        //创建日期选择弹窗
        System.out.print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override//点击后事件触发
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                startTime.setText(i+"-"+(i1+1)+"-"+i2);//字符拼接 i=》年 i1=》月 i2=>日
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();


    }
    /**
     * 截止时间
     * 点击弹出日期选择
     * @param view
     */
    public void endTime(View view ){
        //创建日期选择弹窗
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override//点击后事件触发
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                endTime.setText(i+"-"+(i1+1)+"-"+i2);//字符拼接 i=》年 i1=》月 i2=>日
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 提交查询
     * 传递文本参数
     * 显式跳转视图到TestActivity
     * @param view
     */
    public void submit(View view ){
        Intent intent = new Intent(EarthquakeActivity.this, TestActivity.class);
        intent.putExtra("startTime",startTime.getText().toString());
        intent.putExtra("endTime",endTime.getText().toString());
        intent.putExtra("level",level.getText().toString());
        startActivity(intent);
    }



}
