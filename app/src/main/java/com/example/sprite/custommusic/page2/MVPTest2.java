package com.example.sprite.custommusic.page2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.sprite.custommusic.R;

public class MVPTest2 extends AppCompatActivity {

    private ListView lvBillboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvptest2);
        initView();
        showBillBoardList();
    }

    private void showBillBoardList() {
        new ShowBillBoardList(this, lvBillboard).showList();
    }

    private void initView() {
        lvBillboard = findViewById(R.id.lv_billboard);
    }
}
