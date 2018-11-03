package com.example.sprite.custommusic.page2;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadUtils {
    private static final String TAG = "DownloadUtils";
    private static final int DEFULT_TIMEOUT = 15;
    private Retrofit retrofit;
    private DownloadListenr listenr;

    DownloadUtils(DownloadListenr listenr) {
        this.listenr = listenr;

        DownloadInterceptor interceptor = new DownloadInterceptor(listenr);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://zhangmenshiting.qianqian.com/data2/music/")
                .build();
    }

    public void download(@NonNull String url, final String filePath) {
        listenr.onstartDownload();

        retrofit.create(DateService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream, filePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void writeFile(InputStream inputStream, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            inputStream.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
