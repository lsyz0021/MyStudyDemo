package com.hfax.iflytek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hfax.iflytek.voice.AsrDemo;
import com.hfax.iflytek.voice.IatDemo;
import com.hfax.iflytek.voice.IseDemo;
import com.hfax.iflytek.voice.TtsDemo;
import com.hfax.iflytek.voice.UnderstanderDemo;
import com.iflytek.sunflower.FlowerCollector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView Rv_list;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private MyRecyclerViewAdpter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rv_list = (RecyclerView) findViewById(R.id.rv_list);
        Button btn_list = (Button) findViewById(R.id.btn_list);
        Button btn_grid = (Button) findViewById(R.id.btn_grid);
        Button btn_pull = (Button) findViewById(R.id.btn_pull);

        btn_list.setOnClickListener(this);
        btn_grid.setOnClickListener(this);
        btn_pull.setOnClickListener(this);


        ArrayList<String> list = new ArrayList<>();
        list.add("语音听写");
        list.add("语法识别");
        list.add("语义理解");
        list.add("语音合成");
        list.add("语音评测");
        list.add("语音唤醒");
        list.add("声纹密码");

        // 创建适配器
        adapter = new MyRecyclerViewAdpter(MainActivity.this, list);

        /********************ListView效果 默认这种效果***************/
        // 1.1、listView效果
        layoutManager = new LinearLayoutManager(this);
        // 1.2、设置方向
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        // 1.3、设置listView分割线
        Rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        // 1.4、设置布局管理器， listView或者gridView效果
        Rv_list.setLayoutManager(layoutManager);

        /**************************GridView效果**********************/
        // 2.1、gridView效果 spanCount: 表示列数
        gridLayoutManager = new GridLayoutManager(this, 2);

        /*************************StaggeredGrid**********************/
        // 3.1、瀑布流
        // spanCount: 表示列数
        //  Orientation : 代表方向 StaggeredGridLayoutManager.VERTICAL 竖直
        // StaggeredGridLayoutManager.HORIZONTAL 水平
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);


        // 设置动画
        Rv_list.setItemAnimator(new DefaultItemAnimator());
        // 设置适配器
        Rv_list.setAdapter(adapter);

        // 设置监听事件
        adapter.setOnItemClickLisnter(new MyRecyclerViewAdpter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                changView(position);
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(MainActivity.this, "长按无动作", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * 进入不同的功能界面
     */
    private void changView(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                // 语音转写
                intent = new Intent(MainActivity.this, IatDemo.class);
                break;
            case 1:
                // 语法识别
                intent = new Intent(MainActivity.this, AsrDemo.class);
                break;
            case 2:
                // 语义理解
                intent = new Intent(MainActivity.this, UnderstanderDemo.class);
                break;
            case 3:
                // 语音合成
                intent = new Intent(MainActivity.this, TtsDemo.class);
                break;
            case 4:
                // 语音评测
                intent = new Intent(MainActivity.this, IseDemo.class);
                break;
            case 5:
                // 唤醒
                Toast.makeText(this, "请登录：http://www.xfyun.cn/ 下载体验吧！", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                // 声纹
            default:
                Toast.makeText(this, "在IsvDemo中哦，为了代码简洁，就不放在一起啦，^_^", Toast.LENGTH_SHORT).show();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list:

                // 设置listView分割线
//                Rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                // 设置布局管理器， listView或者gridView效果
                Rv_list.setLayoutManager(layoutManager);
                break;
            case R.id.btn_grid:
                // 设置gridView分割线
//                Rv_list.addItemDecoration(new DividerGridItemDecoration(this));
                // 设置布局管理器， listView或者gridView效果
                Rv_list.setLayoutManager(gridLayoutManager);
                break;
            case R.id.btn_pull:
                // 设置布局管理器， listView或者gridView效果
                Rv_list.setLayoutManager(staggeredGridLayoutManager);
                break;

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(MainActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(MainActivity.this);
        super.onPause();
    }


}
