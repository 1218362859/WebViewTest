package com.example.a.webviewtest;

import android.graphics.Color;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a.Tool.Config;
import com.example.a.Tool.HttpUtil;
import com.example.a.Tool.MyApplication;
import com.example.a.Tool.ThreadUtils;
import com.example.a.domain.JsonRootBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private Button play, historyinfo;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    TextView wanfashuoming, wanfashuomingDetail, shejiangZhongjiang, shejiangZhongjiangdetail, play_rule;
    private List<JsonRootBean.DataBean> cqsscList = new ArrayList<>();
    JsonRootBean jsonRootBean = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView = null;
    private CqsscRecyAdapter cqsscRecyAdapter = null;
    private LinearLayoutManager layoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
    }

    private void InitView() {
        historyinfo = (Button) findViewById(R.id.button_historyinfo);
        play = (Button) findViewById(R.id.button_play);
        historyinfo.setOnClickListener(new MyOnClickListener(0));
        play.setOnClickListener(new MyOnClickListener(1));
        mPager = (ViewPager) findViewById(R.id.vPager);


        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.history_info, null));
        listViews.add(mInflater.inflate(R.layout.play_method, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {

            index = i;
        }

        @Override
        public void onClick(View v) {

            mPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2;// 页卡1 -> 页卡2 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View view, int arg1) {
            ((ViewPager) view).addView(mListViews.get(arg1), 0);

            if (arg1 == 1) {
//            findViewById(R.id.play_line).setVisibility(View.VISIBLE);
//            findViewById(R.id.history_line).setVisibility(View.GONE);
                findViewById(R.id.sendrequest).setOnClickListener(new MyOnClickListener(0));
                wanfashuoming = (TextView) findViewById(R.id.WanfaShuoming);
                wanfashuoming.setText("一、玩法说明");
                wanfashuoming.getPaint().setFakeBoldText(true);
                wanfashuomingDetail = (TextView) findViewById(R.id.WanfaShuomingdetail);
                wanfashuomingDetail.setText("时时彩投注区分为万位、千位、百位、十位和个位，各位号码范围为0~9。" +
                        "每期从各位上开出1个号码作为中奖号码，即开奖号码为5位数。" +
                        "时时彩玩法即是竞猜开奖号码得全部号码、部分号码或部分号码特征。\n");
                shejiangZhongjiang = (TextView) findViewById(R.id.shejiangZhongjiang);
                shejiangZhongjiang.setText("二、设奖及中奖");
                shejiangZhongjiang.getPaint().setFakeBoldText(true);
                shejiangZhongjiangdetail = (TextView) findViewById(R.id.shejiangZhongjiangdetail);
                shejiangZhongjiangdetail.setText("注:\n" +
                        "1)直选：将投注号码以唯一的排列方式进行投注\n" +
                        "2)组选：将投注号码的所有排列方式作为一注投注号码进行投注\n" +
                        "3)组三：一注组选号码中，有2个号码相同，则有3种不同的排列方式进行投注。\n" +
                        "4)组六：一注组选号码中，3个数字各不相同，则有6种不同的排列方式进行投注。\n" +
                        "5)大小单双：号码0~9中，0~4为小，5~9为大，1、3、5、7、9为单，0、2、4、6、8为双。\n" +
                        "时时彩玩法即是竞猜开奖号码得全部号码、部分号码或部分号码特征。\n");
                play_rule = (TextView) findViewById(R.id.play_rule);
                play_rule.setText("三、玩法规则");
                play_rule.getPaint().setFakeBoldText(true);

            }
            if (arg1 == 0) {
//               findViewById(R.id.history_line).setVisibility(View.VISIBLE);
//               findViewById(R.id.play_line).setVisibility(View.GONE);

                recyclerView = (RecyclerView) findViewById(R.id.history_info_recyclerview);


                swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
                refreshRecyclerView();


                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (cqsscList != null && cqsscList.size() > 0) {
                            {
                                refreshRecyclerView();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "让数据加载一会", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }


                });


            }

            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    private void refreshRecyclerView() {
        HttpUtil.sendOkHttpRequest(Config.url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "服务器忙", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i("data", "isInMainThread()=" + ThreadUtils.isInMainThread());
                Gson gson = new Gson();

                if (responseData.indexOf("{") < 0) {
                    Looper.prepare();
                    Toast.makeText(MyApplication.getContext(), "已经是最新的了", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    jsonRootBean = gson.fromJson(responseData, JsonRootBean.class);
                    Log.i("data", "isInMainThread1()=" + ThreadUtils.isInMainThread());


                    List<JsonRootBean.DataBean> data = jsonRootBean.getData();

                    cqsscList = data;


                    Log.i("data", "ssss" + cqsscList.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cqsscRecyAdapter = new CqsscRecyAdapter(cqsscList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
//                           Collections.shuffle(cqsscList);  //list随机打乱
                            recyclerView.setAdapter(cqsscRecyAdapter);
                            cqsscRecyAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

            }

        });
    }
}
