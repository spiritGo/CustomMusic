package com.example.sprite.custommusic.page2;

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.os.RecoverySystem;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownLoadResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final DownloadProgressListener progressListener;
    private BufferedSource bufferedSource;

    public DownLoadResponseBody(ResponseBody responseBody, DownloadProgressListener
            progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }

        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {

                long read = super.read(sink, byteCount);

                totalBytesRead += read != -1 ? read : 0;

                if (null != progressListener) {
                    progressListener.progress(totalBytesRead, responseBody.contentLength(), read ==
                            -1);
                }

                return read;
            }
        };
    }
}
