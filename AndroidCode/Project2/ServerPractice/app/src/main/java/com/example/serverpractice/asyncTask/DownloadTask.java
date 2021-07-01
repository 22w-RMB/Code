package com.example.serverpractice.asyncTask;

// AsyncTask 在 android 11 中 被正是废弃
import android.os.AsyncTask;
import android.os.Environment;

import com.example.serverpractice.listener.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 *  AsyncTask<Void,Integer,Integer>
 *      1、第一个参数 ： 在执行 AsyncTask 时需要传入的参数，也就是执行 execute() 需要传递的参数
 *          downloadTask.execute(downloadUrl);
 *      2、第二个参数 ： publishProgress()  和  onProgressUpdate() 的参数类型
 *      3、第三个参数：  doInBackground()  返回的结果类型
 *
 */
public class DownloadTask extends AsyncTask<String,Integer,Integer> {

    /**
     *
     *  doInBackground(Params...) ： 应该在这个方法去处理所有耗时任务 ，
     *          任务一旦完成就可以通过 return 语句来将任务的执行结果返回，
     *          如果要反馈当前任务的进度 ，可以调用 publishProgress(Progress..) 方法完成
     *
     *
     *  onProgressUpdate(Progress..) ： 如果后台任务中调用了 publishProgress(Progress..) 方法 后，
     *          onProgressUpdate(Progress..) 很快会被调用，该方法的参数就是在后台任务中传递过来的
     *
     *  onPostExecute(Result) ： 当后台任务执行完毕并通过return 语句返回结果时，这个方法很快会被调用
     *          后台任务返回的参数会作为参数传递到此方法中
     */


    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        //csasa
        InputStream is = null;
        RandomAccessFile savedFile = null;

        File file = null;

        try {
            long downloadedLength = 0; // 记录已下载的文件长度

            String downloadUrl = strings[0];
            String fileName =downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath();
            file = new File(directory + fileName);
            if (file.exists()){
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);

            if (contentLength == 0) return TYPE_FAILED;
            if (contentLength == downloadedLength) return TYPE_SUCCESS;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadedLength+"-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();

            if (response!=null){

                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file,"rw");
                savedFile.seek(downloadedLength);  // 跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while((len=is.read(b))!= -1){
                    if (isCanceled) return TYPE_CANCELED;
                    if (isPaused) return TYPE_PAUSED;
                    total += len;
                    savedFile.write(b,0,len);
                    // 计算已下载的百分比
                    int progress = (int)((total + downloadedLength) * 100 / contentLength);
                    publishProgress(progress);
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is!=null){
                    is.close();
                }
                if (savedFile!=null){
                    savedFile.close();
                }
                if (isCanceled && file !=null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return TYPE_FAILED;
    }

    private long getContentLength(String downloadUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response!=null&& response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;

    }

    public void pausedDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }


    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_PAUSED:
                listener.onPaused(lastProgress);
                break;

        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }
}
