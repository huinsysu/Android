package com.study.android.ahu.experimentone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signup = (Button) findViewById(R.id.button_signup);
        Button signin = (Button) findViewById(R.id.button_signin);
        final RadioGroup group = (RadioGroup) findViewById(R.id.group);
        final EditText username = (EditText) findViewById(R.id.editForUsername);
        final EditText password = (EditText) findViewById(R.id.editForPassword);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //事件处理逻辑
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    builder.setTitle("提示");

                    if (TextUtils.equals(username.getText().toString(), "Android") &&
                            TextUtils.equals(password.getText().toString(), "123456")) {
                        builder.setMessage("登录成功！");
                    } else {
                        builder.setMessage("登录失败！");
                    }

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "对话框“取消”按钮被点击",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "对话框“确定”按钮被点击",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.create().show();
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int id = group.getCheckedRadioButtonId();
                RadioButton selected_button = (RadioButton) findViewById(id);
                String selected_string = selected_button.getText().toString();
                Toast.makeText(MainActivity.this, selected_string + "身份注册功能尚未开启",
                        Toast.LENGTH_SHORT).show();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton current_selected = (RadioButton) findViewById(checkedId);
                String current_string = current_selected.getText().toString();
                Toast.makeText(MainActivity.this, current_string + "身份被选中",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
