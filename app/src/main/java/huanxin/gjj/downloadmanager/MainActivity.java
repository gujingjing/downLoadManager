package huanxin.gjj.downloadmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ThinDownloadManager downloadManager;
    Button mDownload1;
    ProgressBar mProgress1;
    TextView mProgress1Txt;
    Context context;
    ProgressWheel progressWheel;

    //下载的线程数
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    int downloadId1 = 0;
    private DownloadRequest request1;
    EppNotificationControl notificationControl;
    private String
//            urlPath,
            app_address;

    private Dialog dialog;
    /**
     * 服务端apk路径
     */
    private static final String downUrl = "http://download.1yd.me/apk/1ydCoach_1.0.16.apk";
    MyDownloadListner myDownloadStatusListener = new MyDownloadListner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initViews();
//        urlPath=createSDCardDir("1yd_coach.apk");
        app_address = Environment.getExternalStorageDirectory() + "/1yd_Coach.apk";
        File file = new File(app_address);
        if(file.exists()){//删除原来的文件
            file.delete();
        }
        initDownload();
        notificationControl = new EppNotificationControl(this.app_address);


    }
    private void initDownload() {
        Uri downloadUri = Uri.parse(downUrl);
        Uri destinationUri = Uri.parse(app_address);
        Log.i("jone", app_address);
        request1 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(myDownloadStatusListener);

    }
    public void initViews(){

            mDownload1 = (Button) findViewById(R.id.button1);

            mProgress1Txt = (TextView) findViewById(R.id.progressTxt1);

            mProgress1 = (ProgressBar) findViewById(R.id.progress1);

            mProgress1.setMax(100);
            mProgress1.setProgress(0);

            progressWheel= (ProgressWheel) findViewById(R.id.progress_bar_two);
            progressWheel.resetCount();//重置

            downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
            mDownload1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (downloadManager.query(downloadId1) == DownloadManager.STATUS_NOT_FOUND) {
                    downloadId1 = downloadManager.add(request1);
                }
                mProgress1Txt.setText("Download1");
                // showProgressNotify();
                notificationControl.showProgressNotify();
                dialog=DialogMacker.getInstance(MainActivity.this).showDialog();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        downloadManager.cancel(downloadId1);
                    }
                });
                break;
        }
    }
    class MyDownloadListner implements DownloadStatusListener {

        @Override
        public void onDownloadComplete(int id) {
            Log.i("jone", "download completed");
            if (id == downloadId1) {
                mProgress1Txt.setText("Download1 id: " + id + " Completed");
                DialogMacker.getInstance(MainActivity.this).dismiss();
            }
        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            Log.i("jone", "DownloadFailed");
            if (id == downloadId1) {
                mProgress1Txt.setText("Download1 id: " + id
                        + " Failed: ErrorCode " + errorCode + ", "
                        + errorMessage);
                mProgress1.setProgress(0);
                progressWheel.setProgress(0);

                DialogMacker.getInstance(MainActivity.this).dismiss();
            }
        }

        @Override
        public void onProgress(int id, long totalBytes, long downloadedBytes,
                               int progress) {

            Log.i("jone", progress + "");
            if (id == downloadId1) {
                mProgress1Txt
                        .setText("Download1 id: " + id + ", " + progress + "%"
//                                + "  "
//                                + getBytesDownloaded(progress, totalBytes)
                        );
                mProgress1.setProgress(progress);
                //设置进度
                progressWheel.setProgressText(progress);
                // setNotify(progress);启动安装
                notificationControl.updateNotification(progress);
//                DialogMacker.getInstance(MainActivity.this).setProgress(progress);
                DialogMacker.progressWheel.setProgressText(progress);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(app_address);
        if(file.exists()){//删除原来的文件
            file.delete();
        }
    }

}
