package com.example.sprite.custommusic.page2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sprite.custommusic.R;

import java.util.List;

public class MySongListAdapter extends BaseAdapter {

    private Context context;
    private List<BillboardBeans.SongListBean> songListBeans;
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvArtist;
    private View vLine;

    void setSongListBeans(List<BillboardBeans.SongListBean> songListBeans) {
        this.songListBeans = songListBeans;
    }

    MySongListAdapter(Context context, List<BillboardBeans.SongListBean> songListBeans) {
        this.context = context;
        this.songListBeans = songListBeans;
    }

    @Override
    public int getCount() {
        return songListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return songListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_songlist, null);
            initView(convertView);
            holder = new ViewHolder();
            holder.ivIcon = ivIcon;
            holder.tvArtist = tvArtist;
            holder.tvTitle = tvTitle;
            holder.v_line = vLine;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BillboardBeans.SongListBean songListBean = songListBeans.get(position);
        holder.tvTitle.setText(songListBean.getTitle());
        Glide.with(context).load(songListBean.getPic_small()).into(holder.ivIcon);
        holder.tvArtist.setText(songListBean.getArtist_name());

//        if (position == getCount() - 1) holder.v_line.setVisibility(View.INVISIBLE);

        return convertView;
    }

    private void initView(View view) {
        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        tvArtist = view.findViewById(R.id.tv_artist);
        vLine = view.findViewById(R.id.v_line);
    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvArtist;
        View v_line;
    }
}
