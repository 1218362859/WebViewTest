package com.example.a.Tool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/5/2.
 */

public class MyApplication extends Application {

    private static  Context context;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate() {
        context = getApplicationContext();
    }
    public  static Context getContext(){
        return  context;
    }
}
