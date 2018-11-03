package com.example.sprite.custommusic.page2;

public interface DownloadListenr {
    void onstartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail();
}
