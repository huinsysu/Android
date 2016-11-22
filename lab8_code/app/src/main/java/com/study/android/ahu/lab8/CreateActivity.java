package com.study.android.ahu.lab8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    private EditText edit_name;
    private EditText edit_birthday;
    private EditText edit_gift;
    private Button create_button;

    private myDB myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_birthday = (EditText) findViewById(R.id.edit_birthday);
        edit_gift = (EditText) findViewById(R.id.edit_gift);
        create_button = (Button) findViewById(R.id.create_button);

        myDbHelper = new myDB(this);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_name.getText().toString())) {
                    Toast.makeText(CreateActivity.this, "名字为空，请完善。", Toast.LENGTH_SHORT).show();
                } else {
                    BirthdayItem exit_item[] = myDbHelper.getItemByName(edit_name.getText().toString());
                    if (exit_item == null) {
                        BirthdayItem item = new BirthdayItem();
                        item.name = edit_name.getText().toString();
                        item.birthday = edit_birthday.getText().toString();
                        item.gift = edit_gift.getText().toString();
                        myDbHelper.insert2DB(item);
                        CreateActivity.this.finish();
                    } else {
                        Toast.makeText(CreateActivity.this, "名字重复啦，请核查。",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
