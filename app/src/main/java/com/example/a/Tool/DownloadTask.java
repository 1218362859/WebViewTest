package com.example.a.Tool;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.a.webviewtest.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AsyncTask三个参数
 * 参数1:在执行AsyncTask时需要传入的参数。可用于后台任务中
 * 参数2.后台任务执行时，如需显示进度，则使用这里指定的泛型作为进度单位
 * 参数3.但任务执行完毕后，如果需要返回结果，则这里指定的泛型作为返回值类型
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAIL = 2;
    public static final int TYPE_PAUSED = 3;
    public static final int TYPE_CANCLE = 4;

    private DownloadListener listener;

    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;


    public DownloadTask(DownloadListener downloadListener) {
        listener = downloadListener;
    }

    /**
     * 在后台任务执行之前调用，用于进行界面上的初始化操作，比如显示一个进度对话框
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 在这里处理所有的耗时任务，任务一旦完成通过return返回
     * 这里不能对UI进行操作，因为此处代码都运行在子线程中。
     *
     * @param params
     * @return
     */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream inputStream = null;
        RandomAccessFile saveFile = null;
        File file = null;
        try {
//            记录已下载文件的长度
            long downLoadLenght = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
//            SD卡的download目录
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downLoadLenght = file.length();
            }
//            获取文件的总长度
            long contentLenght = getContentLenght(downloadUrl);
            if (contentLenght == 0) {
                return TYPE_FAIL;
            } else if (contentLenght == downLoadLenght) {
//                已下载的字节和文件总字节相等，说明下载已经完成
                return TYPE_SUCCESS;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
//                    断点下载，指定从哪一个节点开始下载
                    .addHeader("RANGE", "bytes=" + downLoadLenght + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downLoadLenght);//跳过已下载的字节
            }
            byte[] b = new byte[1024];
            int total = 0;
            int len;
            while ((len = inputStream.read(b)) != -1) {
                if (isCanceled) {
                    return TYPE_CANCLE;
                } else if (isPaused) {
                    return TYPE_PAUSED;
                } else {
                    total += len;
                    saveFile.write(b, 0, len);
                    int progress = (int) ((total + downLoadLenght) * 100 / contentLenght);
//                    通知进度条更新
                    publishProgress(progress);
                }
            }

            response.body().close();
            return TYPE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (saveFile != null) {
                    saveFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {

            }
        }


        return TYPE_FAIL;
    }

    /**
     * 获取文件的总长度
     *
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLenght(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLenght = response.body().contentLength();
            response.close();
            return contentLenght;
        }
        return 0;
    }


    /**
     * 这里可以更新UI了
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
//        从参数中获取到当前的下载进度，和上一次下载进度对比，有变化则通知进度条更新
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onPregress(progress);
            lastProgress = progress;
        }

    }

    /**
     * 但后台任务完成后return后这个方法马上会执行
     * 并且rentur的值可以在参数中获得。这里做收尾工作
     */
    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_CANCLE:
                listener.onCanceled();
                break;
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_FAIL:
                listener.onFaild();
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancleDownload() {
        isCanceled = true;
    }
}
    