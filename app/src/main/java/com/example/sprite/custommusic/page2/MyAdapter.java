package com.example.sprite.custommusic.page2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sprite.custommusic.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<BillboardBeans> billboardBeans;
    private Context context;
    private ListView listView;
    private ImageView ivIcon;
    private TextView tvSong1;
    private TextView tvSong2;
    private TextView tvSong3;
    private View vLine;

    MyAdapter(List<BillboardBeans> billboardBeans, Context context, ListView listView) {
        this.billboardBeans = billboardBeans;
        this.context = context;
        this.listView = listView;
    }

    void setBillboardBeans(List<BillboardBeans> billboardBeans) {
        this.billboardBeans = billboardBeans;
    }

    @Override
    public int getCount() {
        return billboardBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return billboardBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.bill_item, null);
            initView(convertView);
            holder = new ViewHolder();
            holder.tvSong1 = tvSong1;
            holder.tvSong2 = tvSong2;
            holder.tvSong3 = tvSong3;
            holder.iv_icon = ivIcon;
            holder.vLine = vLine;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (billboardBeans.get(position) != null) {

            BillboardBeans.BillboardBean billboard = billboardBeans.get(position).getBillboard();
            List<BillboardBeans.SongListBean> song_list = billboardBeans.get(position)
                    .getSong_list();

            if (!"".equals(billboard.getPic_s192())) {
                Glide.with(context).load(billboard.getPic_s192()).into(holder.iv_icon);

            } else {
                holder.iv_icon.setImageResource(R.mipmap.ic_launcher);

            }

            holder.tvSong1.setText(song_list.get(0).getTitle());
            holder.tvSong2.setText(song_list.get(1).getTitle());
            holder.tvSong3.setText(song_list.get(2).getTitle());
        } else {
            holder.iv_icon.setImageResource(R.mipmap.ic_launcher);
        }
        return convertView;
    }

    private void initView(View view) {
        ivIcon = view.findViewById(R.id.iv_icon);
        tvSong1 = view.findViewById(R.id.tv_song_1);
        tvSong2 = view.findViewById(R.id.tv_song_2);
        tvSong3 = view.findViewById(R.id.tv_song_3);
        vLine = view.findViewById(R.id.v_line);
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tvSong1;
        TextView tvSong2;
        TextView tvSong3;
        View vLine;
    }
}
