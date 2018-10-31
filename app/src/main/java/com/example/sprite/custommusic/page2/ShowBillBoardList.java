package com.example.sprite.custommusic.page2;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

class ShowBillBoardList {
    private Context context;
    private ListView listView;
    private int[] types = {
            1,//新歌榜
            2,//热歌榜
            11,//摇滚榜
            12,//爵士榜
            16,//流行榜
            21,//欧美金曲榜
            22,//经典老歌榜
            23,//情歌对唱榜
            24,//影视金曲榜
            25//网络歌曲榜
    };
    private List<BillboardBeans> billboardBeans;

    ShowBillBoardList(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        billboardBeans = new ArrayList<>();
    }

    void showList() {
        HttpTool httpTool = HttpTool.getInstance();

        for (int type : types) {
            billboardBeans.add(null);
            httpTool.getBillBoardData(type, 4, 0);
        }

        httpTool.setNetCallBack(new NetCallBack<BillboardBeans>() {

            private MyAdapter myAdapter;

            @Override
            public void onSuccess(BillboardBeans beans) {

                int index = findIndex(beans.getBillboard().getBillboard_type());

                System.out.println("index" + index + beans);

                if (index == -1) return;
                billboardBeans.set(index, beans);

                if (myAdapter == null) {
                    myAdapter = new MyAdapter(billboardBeans, context, listView);
                    listView.setAdapter(myAdapter);
                } else {
                    myAdapter.setBillboardBeans(billboardBeans);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message) {
                System.out.println(billboardBeans.indexOf(null));
                HttpTool.getInstance().getBillBoardData(types[billboardBeans.indexOf(null)], 4, 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SongListActivity.class);
                intent.putExtra("type", types[position]);
                context.startActivity(intent);
            }
        });
    }

    private int findIndex(String type) {
        for (int i = 0; i < types.length; i++) {
            if (types[i] == Integer.parseInt(type)) return i;
        }

        return -1;
    }


}
