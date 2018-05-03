package com.example.a.webviewtest;

/**
 * Created by Administrator on 2018/5/3.
 */

public interface DownloadListener {
    //    更新进度条
    void onPregress(int progress);
    //    下载成功
    void onSuccess();
    //    下载失败
    void onFaild();
    //    暂停下载
    void onPaused();
    //    取消下载
    void onCanceled();
}
