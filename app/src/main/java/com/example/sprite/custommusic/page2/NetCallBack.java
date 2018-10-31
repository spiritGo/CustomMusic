package com.example.sprite.custommusic.page2;

public interface NetCallBack<T> {
    void onSuccess(T t);

    void onFailure(String message);

}
