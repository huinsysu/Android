package com.study.android.ahu.experimentthree;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class detailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final List<Map<String, Object>> actionsList = new ArrayList<>();

        String[] action = new String[] {"编辑联系人", "分享联系人", "加入黑名单", "删除联系人"};

        for (int i = 0; i< 4; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("action", action[i]);
            actionsList.add(temp);
        }

        ListView editList = (ListView) findViewById(R.id.edit_list);
        SimpleAdapter simpleadapter = new SimpleAdapter(this, actionsList, R.layout.edititem,
                new String[]{"action"}, new int[]{R.id.action});
        editList.setAdapter(simpleadapter);

        RelativeLayout topPart = (RelativeLayout) findViewById(R.id.topPart);
        ImageView backImage = (ImageView) findViewById(R.id.backImage);
        final ImageView star = (ImageView) findViewById(R.id.star);
        final ImageView fullStar = (ImageView) findViewById(R.id.fullStar);
        TextView detailName = (TextView) findViewById(R.id.detailName);
        TextView detailPhone = (TextView) findViewById(R.id.detailPhone);
        TextView detailType = (TextView) findViewById(R.id.detailType);
        TextView detailAddress = (TextView) findViewById(R.id.detailAddress);

        serializedItem item = (serializedItem)getIntent().getSerializableExtra("object");
        topPart.setBackgroundColor(Color.parseColor(item.getColor()));

        detailName.setText(item.getName());
        detailType.setText(item.getType());
        detailPhone.setText(item.getphone());
        detailAddress.setText(item.getAddress());

        backImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fullStar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fullStar.setVisibility(View.INVISIBLE);
            }
        });

        star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fullStar.setVisibility(View.VISIBLE);
            }
        });
    }
}
