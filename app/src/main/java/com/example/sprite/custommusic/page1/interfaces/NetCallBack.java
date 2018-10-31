package com.example.sprite.custommusic.page1.interfaces;

import com.example.sprite.custommusic.page1.beans.BillboardBeans;

public interface NetCallBack {
    void onSuccess(BillboardBeans beans, int finalI);

    void onFailure(String message);
}
