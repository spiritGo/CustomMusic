package com.example.sprite.custommusic.page1.interfaces;

import com.example.sprite.custommusic.page1.beans.BillboardBeans;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("ting?from=webapp_music&method=baidu.ting.billboard.billList")
    Call<BillboardBeans> getBillList(@Query("type") int type, @Query("size") int size, @Query("offset") int offset);
}
