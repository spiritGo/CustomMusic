package com.example.sprite.custommusic.page1.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sprite.custommusic.R;
import com.example.sprite.custommusic.page1.beans.BillboardBeans;
import com.example.sprite.custommusic.page1.interfaces.NetCallBack;
import com.example.sprite.custommusic.page1.utils.BillboardList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowBillboard {

    private static List<BillboardBeans> beansList;
    private static LruCache<String, Bitmap> lruCache;
    private static ListView listView;

    public static void setListView(ListView listView) {
        ShowBillboard.listView = listView;
    }

    public static void showBillList(Context context, ListView listView) {
        BillboardList billboardList = BillboardList.getInstance();
        MyCallBack myCallBack = new MyCallBack();
        myCallBack.setListView(listView);
        beansList = new ArrayList<>();
        initLruCache();
        for (int i = 0; i < 10; i++) {
            beansList.add(null);
        }
        billboardList.askData(context, myCallBack);
    }

    private static void initLruCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int maxSize = (int) (maxMemory / 8);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private static Bitmap getLruCache(String key) {
        return lruCache.get(key);
    }

    private static void setLruCache(String key, Bitmap bitmap) {
        if (getLruCache(key) == null) {
            lruCache.put(key, bitmap);
        }
    }

    static class MyCallBack implements NetCallBack {
        private ListView listView;
        private MyAdapter myAdapter;

        @Override
        public void onSuccess(BillboardBeans beans, int finalI) {
            if (beans != null) beansList.set(finalI, beans);
            if (myAdapter == null) {
                myAdapter = new MyAdapter(beansList);
                listView.setAdapter(myAdapter);
            } else {
                myAdapter.setBeansList(beansList);
                myAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(String message) {

        }

        void setListView(ListView listView) {
            this.listView = listView;
        }
    }

    static class MyAdapter extends BaseAdapter {

        private List<BillboardBeans> beansList;

        MyAdapter(List<BillboardBeans> beansList) {
            this.beansList = beansList;
        }

        void setBeansList(List<BillboardBeans> beansList) {
            this.beansList = beansList;
        }

        @Override
        public int getCount() {
            return beansList.size();
        }

        @Override
        public Object getItem(int position) {
            return beansList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.billboard_item, null);
                holder.tv_title = convertView.findViewById(R.id.tv_title);
                holder.iv_image = convertView.findViewById(R.id.iv_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (beansList.get(position) != null) {
                holder.tv_title.setText(beansList.get(position).getBillboard().getName());
                String pic_s192 = beansList.get(position).getBillboard().getPic_s192();
                holder.iv_image.setTag(pic_s192);
                if (ShowBillboard.getLruCache(pic_s192) != null) {

                    holder.iv_image.setImageBitmap(getLruCache(pic_s192));
                } else {
                    ImageTask imageTask = new ImageTask();
                    imageTask.execute(beansList.get(position).getBillboard().getPic_s192());
                }
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_title;
            ImageView iv_image;
        }
    }

    private static Bitmap getBitmap(String url, int destW) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36";
            connection.setRequestProperty("User-agent", userAgent);
            connection.connect();
            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;

                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                baos.close();
                inputStream.close();
                byte[] bytes = baos.toByteArray();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                int outWidth = options.outWidth;
                int outHeight = options.outHeight;
                int size = 1;

                if (outHeight > outWidth) {
                    size = outWidth / destW;
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = size;

                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return bitmap;
    }

    static class ImageTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... strings) {
            imageUrl = strings[0];
            Bitmap bitmap = getBitmap(imageUrl,50);
            if (bitmap != null) setLruCache(imageUrl, bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            System.out.println("bitmapisnull?" + bitmap);
            ImageView imageView = listView.findViewWithTag(imageUrl);
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
            }
        }
    }

}
