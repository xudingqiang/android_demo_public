package com.bella.android_demo_public.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DlgUpdate extends Dialog {
    String forceUpdate;
    String packageUrl;
    String packageName;
    ProgressBar progressBar;
    TextView txtProgress;
    TextView txtInstall;
    Activity activity;
    boolean isDownload = true;
    String downPath = "";

     static  long lastProgress = 0 ;
    private boolean stopDownload = false;
    private static final OkHttpClient client = new OkHttpClient();


    public DlgUpdate(@NonNull Activity context, String packageUrl) {
        super(context);
        this.packageUrl = packageUrl;
        this.activity = context;
    }

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                progressBar.setProgress(msg.arg1);
                txtProgress.setText(msg.arg1 + "%");
            } else {
                txtInstall.setText("安 装");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        txtInstall = (TextView) findViewById(R.id.txtInstall);
        txtInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownload) {
                    return;
                } else {
                    Utils.installApk(activity, downPath);
                }
            }
        });

//        try {
//            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/isany";
//            File pathFile = new File(savePath);
//            if (!pathFile.exists()) {
//                pathFile.mkdirs();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        isDownload = true;
        new Thread(new MyThread()).start();
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
//            downloadFile(packageUrl);

            String savePath = getContext().getFilesDir().getAbsolutePath();
            String fileName = packageUrl.substring(packageUrl.lastIndexOf("/") + 1);
            downPath = savePath +"/"+fileName;
            LogTool.i("FDE","downPath:"+downPath + ",savePath "+savePath);
            File file = new File(savePath,fileName);
            try {
                if(file.exists()){
                    file.delete();
                }
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            downloadFile(packageUrl,file,activity);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }

        return false;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopDownload() {
        stopDownload = true;
    }

    private long parseTotalBytes(Response response, long downloadedBytes) {
        String contentRange = response.header("Content-Range");
        if (contentRange != null && contentRange.contains("/")) {
            try {
                return Long.parseLong(contentRange.split("/")[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return downloadedBytes + response.body().contentLength();
    }

    public void downloadFile(String url, File destinationFile, Context context) {
        long downloadedBytes = destinationFile.exists() ? destinationFile.length() : 0;
        long totalBytes = -1;

        while (!stopDownload) {
            if (!isNetworkAvailable(context)) {
                System.out.println("Network unavailable. Waiting to retry...");
                sleep(5000); // 等待 5 秒再检测网络
                continue;
            }

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .header("Range", "bytes=" + downloadedBytes + "-")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() || response.code() == 206) {
                        if (totalBytes == -1) {
                            totalBytes = parseTotalBytes(response, downloadedBytes);
                        }
                        try (InputStream inputStream = response.body().byteStream();
                             RandomAccessFile raf = new RandomAccessFile(destinationFile, "rw")) {
                            raf.seek(downloadedBytes);
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                raf.write(buffer, 0, bytesRead);
                                downloadedBytes += bytesRead;

                                int progress = (int) ((downloadedBytes * 100) / totalBytes) ;
                                lastProgress = 0;
                                LogTool.i("Download downloadedBytes "+downloadedBytes +", progress "+progress);


                                if (!isNetworkAvailable(context)) {
                                    throw new IOException("Network lost during download");
                                }
                            }
                        }
                        LogTool.i("Download completed!");
                        return; // 下载完成后退出
                    } else {
                        throw new IOException("Unexpected response code: " + response.code());
                    }
                }
            } catch (IOException e) {
                System.out.println("Download failed: " + e.getMessage());
                System.out.println("Retrying...");
                sleep(5000); // 等待 5 秒后重试
            }
        }
    }

    public void downloadFile(String url) {
        final long startTime = System.currentTimeMillis();
        LogTool.i( "startTime=" + startTime);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogTool.i( "download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = getContext().getFilesDir().getAbsolutePath();//Environment.getExternalStorageDirectory().getAbsolutePath() + "/isany/";
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
//                    downPath = savePath + url.substring(url.lastIndexOf("/") + 1);
                    String fileName = url.substring(url.lastIndexOf("/") + 1);
                    LogTool.i("fileName: "+fileName + ",savePath: "+savePath);
                    File file = new File(savePath,fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = progress;
                        mHander.sendMessage(msg);
                    }
                    fos.flush();
                    LogTool.i( "download success totalTime=" + (System.currentTimeMillis() - startTime));
                    isDownload = false;
                    Message msg = new Message();
                    msg.what = 2;
                    mHander.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogTool.e( "download failed : " + e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
