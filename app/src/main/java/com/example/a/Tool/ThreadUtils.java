package com.example.a.Tool;

import android.os.Looper;
import android.util.Log;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ThreadUtils {
    public static final String TAG = "ThreadUtils";

    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i(TAG, "isInMainThread myLooper=" + myLooper + ";mainLooper=" + mainLooper);
        return myLooper == mainLooper;
    }
}
