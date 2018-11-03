package com.example.sprite.custommusic.page2;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

interface DateService {
    String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
    String BILL_LIST_METHOD = "baidu.ting.billboard.billList";
    String DOWNLOAD_METHOD = "baidu.ting.song.play";
    String LRC_METHOD = "baidu.ting.song.lry";
    String FROM = "ting?from=webapp_music&";

    @GET("ting?from=webapp_music&method=baidu.ting.billboard.billList")
    Call<BillboardBeans> getBillBoardBeansUrl(@Query("type") int type, @Query("size") int size,
                                              @Query("offset") int offset);

    @GET("ting?from=webapp_music&method=baidu.ting.song.play")
    Call<SongBean> downLoadMusic(@Query("songid") String songId);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
