package com.study.android.ahu.lab8;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ListView birthdayList;

    private myDB myDbHelper;
    private List<Map<String, Object>> data;


    @Override
    protected  void onStart() {
        super.onStart();
        birthdayList = (ListView) findViewById(R.id.birthday_list);
        myDbHelper = new myDB(this);
        data = new ArrayList<>();

        BirthdayItem[] items;
        items = myDbHelper.getALLItems();

        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("name", items[i].name);
                temp.put("birthday", items[i].birthday);
                temp.put("gift", items[i].gift);
                data.add(temp);
            }
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.birthday_item,
                new String[]{"name", "birthday", "gift"}, new int[]{R.id.item_name,
                R.id.item_birthday, R.id.item_gift});
        birthdayList.setAdapter(simpleAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.create_button);
        birthdayList = (ListView) findViewById(R.id.birthday_list);
        myDbHelper = new myDB(this);

        birthdayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View dialog_view = factory.inflate(R.layout.dialoglayout, null);

                final TextView dialog_name = (TextView) dialog_view.findViewById(R.id.dialog_name);
                final EditText dailog_birthday = (EditText) dialog_view.findViewById(R.id.dialog_birthday) ;
                final EditText dialog_gift = (EditText) dialog_view.findViewById(R.id.dialog_gift);
                final TextView dialog_phone = (TextView) dialog_view.findViewById(R.id.dialog_phone);

                Iterator<Map<String, Object>> it = data.listIterator(i);
                Map<String, Object> map = it.next();

                String phonesNumber = "";
                Cursor contactPhone = getContentResolver().query(ContactsContract.CommonDataKinds
                        .Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone
                        .DISPLAY_NAME + "=" + '"' + map.get("name").toString() + '"', null, null);
                int counts = contactPhone.getCount();
                if (counts != 0 && contactPhone.moveToFirst()) {
                    for (int j = 0; j < counts; j++){
                        String phoneNumber = contactPhone.getString(contactPhone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phonesNumber = phonesNumber + " " + phoneNumber;
                        contactPhone.moveToNext();
                    }
                }

                if (phonesNumber != "") {
                    dialog_phone.setText(phonesNumber);
                } else {
                    dialog_phone.setText("无");
                }

                dialog_name.setText(map.get("name").toString());
                dailog_birthday.setText(map.get("birthday").toString());
                dialog_gift.setText(map.get("gift").toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("O(* _ *)O")
                        .setView(dialog_view)
                        .setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BirthdayItem update_item = new BirthdayItem();
                                update_item.name = dialog_name.getText().toString();
                                update_item.birthday = dailog_birthday.getText().toString();
                                update_item.gift = dialog_gift.getText().toString();
                                myDbHelper.updateOneDate(update_item);

                                data = new ArrayList<>();
                                BirthdayItem[] items;
                                items = myDbHelper.getALLItems();

                                if (items != null) {
                                    for (int i = 0; i < items.length; i++) {
                                        Map<String, Object> temp = new LinkedHashMap<>();
                                        temp.put("name", items[i].name);
                                        temp.put("birthday", items[i].birthday);
                                        temp.put("gift", items[i].gift);
                                        data.add(temp);
                                    }
                                }

                                SimpleAdapter newAdapter = new SimpleAdapter(MainActivity.this,
                                        data, R.layout.birthday_item,
                                        new String[]{"name", "birthday", "gift"},
                                        new int[]{R.id.item_name, R.id.item_birthday, R.id.item_gift});
                                birthdayList.setAdapter(newAdapter);
                            }
                        })
                        .create().show();
            }
        });

        birthdayList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int index = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否删除？")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Iterator<Map<String, Object>> it = data.listIterator(index);
                                Map<String, Object> map = it.next();
                                myDbHelper.deleteOneData(map.get("name").toString());

                                data = new ArrayList<>();
                                BirthdayItem[] items;
                                items = myDbHelper.getALLItems();

                                if (items != null) {
                                    for (int i = 0; i < items.length; i++) {
                                        Map<String, Object> temp = new LinkedHashMap<>();
                                        temp.put("name", items[i].name);
                                        temp.put("birthday", items[i].birthday);
                                        temp.put("gift", items[i].gift);
                                        data.add(temp);
                                    }
                                }

                                SimpleAdapter newAdapter = new SimpleAdapter(MainActivity.this,
                                        data, R.layout.birthday_item,
                                        new String[]{"name", "birthday", "gift"},
                                        new int[]{R.id.item_name, R.id.item_birthday, R.id.item_gift});
                                birthdayList.setAdapter(newAdapter);

                            }
                        });
                builder.create().show();
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

    }
}
