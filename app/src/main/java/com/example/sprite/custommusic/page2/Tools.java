package com.example.sprite.custommusic.page2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.sprite.custommusic.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Tools {

    static Drawable getDrawable(Context context, String path) {

        BitmapDrawable drawable = null;
        if ("".equals(path)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, options);

            int outWidth = options.outWidth;
            int outHeight = options.outHeight;

            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap
                    .ic_launcher, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();

            try {
                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(bytes, 0, bytes
                        .length, false);
                Bitmap region = decoder.decodeRegion(new Rect(outWidth / 4, 3 * outHeight / 4,
                        3 * outWidth / 4, outHeight), options);
                return new BitmapDrawable(region);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", HttpTool.getAgent());
            connection.connect();
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                byte[] bytes = baos.toByteArray();
                is.close();
                connection.disconnect();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                int outWidth = options.outWidth;
                int outHeight = options.outHeight;

                options.inJustDecodeBounds = false;

                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(bytes, 0, bytes
                        .length, false);
                Bitmap bitmap = decoder.decodeRegion(new Rect(outWidth / 4, outHeight / 2,
                        3 * outWidth / 4, outHeight), options);

                drawable = new BitmapDrawable(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawable;
    }
}
