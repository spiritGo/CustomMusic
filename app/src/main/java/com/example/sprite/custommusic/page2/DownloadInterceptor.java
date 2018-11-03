package com.example.sprite.custommusic.page2;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    private DownloadListenr listenr;

    DownloadInterceptor(DownloadListenr listenr) {
        this.listenr = listenr;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response.newBuilder().body(new DownloadResposeBody(response.body(), listenr))
                .build();
    }
}
