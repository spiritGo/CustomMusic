package com.example.sprite.custommusic.page2;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HttpTool {

    private static HttpTool httpTool;
    private final DateService dateService;

    private HttpTool() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient.Builder interceptor = builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent", getUserAgent())
                        .build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(interceptor.build())
                .baseUrl("http://tingapi.ting.baidu.com/v1/restserver/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dateService = retrofit.create(DateService.class);
    }

    private NetCallBack<BillboardBeans> netCallBack;
    private NetCallBack<SongBean> songBeanNetCallBack;

    static HttpTool getInstance() {
        if (httpTool == null) {
            synchronized (HttpTool.class) {
                if (httpTool == null) {
                    httpTool = new HttpTool();
                }
            }
        }
        return httpTool;
    }

    void setNetCallBack(NetCallBack<BillboardBeans> netCallBack) {
        this.netCallBack = netCallBack;
    }

    void setSongBeanNetCallBack(NetCallBack<SongBean> songBeanNetCallBack) {
        this.songBeanNetCallBack = songBeanNetCallBack;
    }

    void getBillBoardData(int type, int size, int offset) {

        Call<BillboardBeans> call = dateService.getBillBoardBeansUrl(type, size, offset);
        call.enqueue(new Callback<BillboardBeans>() {
            @Override
            public void onResponse(Call<BillboardBeans> call, Response<BillboardBeans> response) {
                if (netCallBack != null) netCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<BillboardBeans> call, Throwable t) {
                if (netCallBack != null) netCallBack.onFailure(t.getMessage());
            }
        });
    }

    private String getUserAgent() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/70.0.3538.67 Safari/537.36";
    }

    static String getAgent() {
        return httpTool.getUserAgent();
    }

    void downLoadMusic(int songId) {

        Call<SongBean> call = dateService.downLoadMusic(String.valueOf(songId));
        call.enqueue(new Callback<SongBean>() {
            @Override
            public void onResponse(Call<SongBean> call, Response<SongBean> response) {
                if (songBeanNetCallBack != null) songBeanNetCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<SongBean> call, Throwable t) {
                if (songBeanNetCallBack != null) songBeanNetCallBack.onFailure(t.getMessage());
            }
        });
    }
}
