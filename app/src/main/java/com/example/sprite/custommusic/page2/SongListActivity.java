package com.example.sprite.custommusic.page2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sprite.custommusic.R;
import com.example.sprite.custommusic.page1.utils.Tools;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends Activity {

    private List<BillboardBeans.SongListBean> songListBeans;
    private ImageView ivIcon;
    private TextView tvTypeName;
    private TextView tvUpdateTime;
    private TextView tvComment;
    private ListView lvSongList;
    private LinearLayout llNavBg;
    private int size = 10;
    private int offset = 0;
    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        initView();
        final int type = getIntent().getIntExtra("type", 0);
        final HttpTool httpTool = HttpTool.getInstance();

        httpTool.getBillBoardData(type, size, offset);
        songListBeans = new ArrayList<>();

        httpTool.setNetCallBack(new NetCallBack<BillboardBeans>() {

            private MySongListAdapter mySongListAdapter;

            @Override
            public void onSuccess(final BillboardBeans beans) {
                isLoading = false;
                if (beans.getSong_list() == null) {
                    Tools.toast(SongListActivity.this, "这是最后一首歌了！");
                    return;
                }

                songListBeans.addAll(beans.getSong_list());
                if (songListBeans.size() <= 10) {

                    if ("".equals(beans.getBillboard().getPic_s192())) {
                        ivIcon.setImageResource(R.mipmap.ic_launcher);

                    } else {

                        Glide.with(SongListActivity.this).load(beans.getBillboard().getPic_s192()
                        ).into(ivIcon);
                    }

                    tvTypeName.setText(beans.getBillboard().getName());
                    tvComment.setText(beans.getBillboard().getComment());
                    tvUpdateTime.setText(beans.getBillboard().getUpdate_date());
                    new Thread() {
                        @Override
                        public void run() {
                            final Drawable drawable = com.example.sprite.custommusic.page2.Tools
                                    .getDrawable(SongListActivity.this, beans.getBillboard()
                                            .getPic_s640());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    llNavBg.setBackgroundDrawable(drawable);
                                }
                            });
                        }
                    }.start();

                    if ("".equals(beans.getBillboard().getName())) {
                        tvTypeName.setText("无法定位类型");
                    }
                }

                if (mySongListAdapter == null) {
                    mySongListAdapter = new MySongListAdapter(SongListActivity.this, songListBeans);
                    lvSongList.setAdapter(mySongListAdapter);
                } else {
                    mySongListAdapter.setSongListBeans(songListBeans);
                    mySongListAdapter.notifyDataSetChanged();
                }

                lvSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                            id) {
                        httpTool.downLoadMusic(Integer.parseInt(songListBeans.get(position)
                                .getSong_id()));
                        httpTool.setSongBeanNetCallBack(new NetCallBack<SongBean>() {

                            @Override
                            public void onSuccess(SongBean songBean) {

                                try {
                                    MediaPlayer mediaPlayer = new MediaPlayer();
                                    mediaPlayer.reset();

                                    mediaPlayer.setDataSource(songBean.getBitrate().getFile_link());
                                    mediaPlayer.prepareAsync();
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer
                                            .OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.start();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });

        lvSongList.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int lastVisibleItem;
            private int allCount;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastIndex = allCount - 1;
                if (scrollState == SCROLL_STATE_IDLE && lastVisibleItem == lastIndex) {
                    if (!isLoading) {

                        offset += size;
                        HttpTool.getInstance().getBillBoardData(type, size, offset);
                        isLoading = true;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                allCount = totalItemCount;
            }
        });

        lvSongList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
                                           long
                                                   id) {

                final PopupWindow popupWindow = new PopupWindow(SongListActivity.this);
                View popView = View.inflate(getApplicationContext(), R.layout.item_pw, null);
                final TextView tvDownload = popView.findViewById(R.id.tv_download);
                popupWindow.setContentView(popView);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.showAsDropDown(view, view.getWidth() - com.example.sprite.custommusic
                        .page2.Tools.dpTopx(getApplicationContext(), 50), -view.getHeight() + com
                        .example.sprite.custommusic.page2.Tools.dpTopx(getApplicationContext(),
                                10));
                tvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        HttpTool.getInstance().downLoadMusic(Integer.parseInt(songListBeans.get
                                (position).getSong_id()));
                        HttpTool.getInstance().setSongBeanNetCallBack(new NetCallBack<SongBean>() {
                            @Override
                            public void onSuccess(final SongBean songBean) {
                                String filePath = new File(Environment
                                        .getExternalStorageDirectory(), songBean
                                        .getSonginfo().getTitle() + ".mp3").getAbsolutePath();
                                download(songBean.getBitrate().getFile_link(), filePath);
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
                    }
                });
                return true;
            }
        });
    }

    private void download(String path, String filePath) {
        DownloadUtils downloadUtils = new DownloadUtils(new DownloadListenr() {
            @Override
            public void onstartDownload() {

            }

            @Override
            public void onProgress(int progress) {
                System.out.println("::" + progress);
            }

            @Override
            public void onFinishDownload() {

            }

            @Override
            public void onFail() {

            }
        });
        downloadUtils.download(path, filePath);
    }

    //尝试，不用
    private void downloadFromHttp(final TextView tvDownload, int position) {

        HttpTool.getInstance().downLoadMusic(Integer.parseInt(songListBeans.get(position)
                .getSong_id()));
        HttpTool.getInstance().setSongBeanNetCallBack(new NetCallBack<SongBean>() {
            @Override
            public void onSuccess(final SongBean songBean) {
                new Thread() {
                    @Override
                    public void run() {
                        HttpTool.getInstance().downloadForHttp(songBean.getBitrate().getFile_link(),
                                tvDownload);
                    }
                }.start();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void initView() {
        ivIcon = findViewById(R.id.iv_icon);
        tvTypeName = findViewById(R.id.tv_typeName);
        tvUpdateTime = findViewById(R.id.tv_update_time);
        tvComment = findViewById(R.id.tv_comment);
        lvSongList = findViewById(R.id.lv_songList);
        llNavBg = findViewById(R.id.ll_navBg);
    }
}
