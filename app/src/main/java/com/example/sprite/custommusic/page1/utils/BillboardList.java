package com.example.sprite.custommusic.page1.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;

import com.example.sprite.custommusic.page1.beans.BillboardBeans;
import com.example.sprite.custommusic.page1.constance.UrlConstance;
import com.example.sprite.custommusic.page1.interfaces.DataService;
import com.example.sprite.custommusic.page1.interfaces.NetCallBack;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillboardList {

    private static BillboardList billboardList;
    private int[] types = {
            1,//新歌榜
            2,//热歌榜
            11,//摇滚榜
            12,//爵士榜
            16,//流行榜
            21,//欧美金曲榜
            22,//经典老歌榜
            23,//情歌对唱榜
            24,//影视金曲榜
            25//网络歌曲榜
    };

    private BillboardList() {
    }

    public static BillboardList getInstance() {
        if (billboardList == null) {
            synchronized (BillboardList.class) {
                if (billboardList == null) {
                    billboardList = new BillboardList();
                }
            }
        }
        return billboardList;
    }

    public void askData(final Context context, final NetCallBack callBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstance.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(setUserAgent(context).build())
                .build();

        DataService dataService = retrofit.create(DataService.class);
        for (int i=0;i<types.length;i++) {
            Call<BillboardBeans> call = dataService.getBillList(types[i], 1, 0);
            final int finalI = i;
            call.enqueue(new Callback<BillboardBeans>() {
                @Override
                public void onResponse(Call<BillboardBeans> call, Response<BillboardBeans> response) {
                    if (callBack != null) callBack.onSuccess(response.body(), finalI);
                }

                @Override
                public void onFailure(Call<BillboardBeans> call, Throwable t) {
                    if (callBack != null) callBack.onFailure(t.getMessage());
                }

            });
        }
    }

    private static OkHttpClient.Builder setUserAgent(final Context context) {
        OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
        return httpclient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request build = request.newBuilder()
                        .addHeader("User-Agent", getUserAgent(context))
                        .method(request.method(), request.body())
                        .build();

                return chain.proceed(build);
            }
        });
    }

    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();

    }
}
