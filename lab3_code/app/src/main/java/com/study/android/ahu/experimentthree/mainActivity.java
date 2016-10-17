package com.study.android.ahu.experimentthree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        final List<Map<String, Object>> data = new ArrayList<>();

        String[] name = new String[] {"Aaron", "Elvis", "David", "Edwin", "Frank", "Joshua", "Ivan",
                "Mark", "Joseph", "Phoebe"};
        String[] phone = new String[] {"17715523654" ,"18825653224", "15052116654", "18854211875",
                "13955188541", "13621574410", "15684122771", "17765213579", "13315466578",
                "17895466428"};
        String[] type = new String[] {"手机", "手机", "手机", "手机", "手机", "手机", "手机", "手机",
                "手机", "手机"};
        String[] address = new String[] {"江苏苏州电信", "广东揭阳移动", "江苏无锡移动", "山东青岛移动",
                "安徽合肥移动", "江苏苏州移动", "山东烟台联通", "广东珠海电信", "河北石家庄电信", "山东东营移动"};
        String[] backgroundColor = new String[] {"#BB4C3B", "#c48d30", "#4469b0", "#20A17B", "#BB4C3B",
                "#c48d30", "#4469b0", "#20A17B", "#BB4C3B", "#c48d30"};

        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            char alphabet = name[i].charAt(0);
            temp.put("alphabet", alphabet);
            temp.put("name", name[i]);
            temp.put("phone", phone[i]);
            temp.put("type", type[i]);
            temp.put("address", address[i]);
            temp.put("backgroundColor", backgroundColor[i]);
            data.add(temp);
        }

        ListView contastsList = (ListView) findViewById(R.id.contacts_list);
        final SimpleAdapter simpleadapter = new SimpleAdapter(this, data, R.layout.useritem,
                new String[]{"alphabet", "name"}, new int[]{R.id.circleItem, R.id.nameItem});
        contastsList.setAdapter(simpleadapter);

        contastsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Iterator<Map<String, Object>> it = data.listIterator(i);
                Map<String, Object> map = it.next();

                String nameValue = (String) map.get("name");
                String phoneValue = (String) map.get("phone");
                String typeValue = (String) map.get("type");
                String addressValue = (String) map.get("address");
                String colorValue = (String) map.get("backgroundColor");

                serializedItem item = new serializedItem();
                item.setName(nameValue);
                item.setPhone(phoneValue);
                item.setType(typeValue);
                item.setAddress(addressValue);
                item.setColor(colorValue);

                Intent myIntent = new Intent(mainActivity.this, detailActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putSerializable("object", item);
                myIntent.putExtras(myBundle);

                startActivity(myIntent);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        contastsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                Iterator<Map<String, Object>> it = data.listIterator(i);
                Map<String, Object> map = it.next();
                builder.setTitle("删除联系人");
                builder.setMessage("确定删除联系人" + map.get("name"));
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.remove(index);
                        simpleadapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return true;
            }
        });

    }
}
