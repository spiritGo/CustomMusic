package com.example.sprite.custommusic.page2;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadResposeBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadListenr listenr;

    private BufferedSource bufferedSource;

    DownloadResposeBody(ResponseBody responseBody, DownloadListenr listenr) {
        this.responseBody = responseBody;
        this.listenr = listenr;
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
            long totalbyteRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                totalbyteRead += byteRead != -1 ? byteRead : 0;

                if (null != listenr) {
                    if (byteRead != -1) {
                        listenr.onProgress((int) (totalbyteRead * 100 / contentLength()));
                    }
                }

                return byteRead;
            }
        };
    }
}
