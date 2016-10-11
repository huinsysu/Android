package com.study.android.ahu.experimentone;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        final TextInputLayout usernameText = (TextInputLayout) findViewById(R.id.username);
        final TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.password);
        final EditText username = usernameText.getEditText();
        final EditText password = passwordText.getEditText();

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                usernameText.setErrorEnabled(false);
                passwordText.setErrorEnabled(false);
                if (TextUtils.isEmpty(username.getText().toString())) {
                    usernameText.setError("用户名不能为空");
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    passwordText.setError("密码不能为空");
                } else {

                    if (TextUtils.equals(username.getText().toString(), "Android") &&
                            TextUtils.equals(password.getText().toString(), "123456")) {
                        Snackbar.make(view, "登录成功", Snackbar.LENGTH_SHORT)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this, "Sncakbar的按钮被点击了",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                .setDuration(5000)
                                .show();
                    } else {
                        Snackbar.make(view, "登录失败", Snackbar.LENGTH_SHORT)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this, "Sncakbar的按钮被点击了",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                .setDuration(5000)
                                .show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int id = group.getCheckedRadioButtonId();
                RadioButton selected_button = (RadioButton) findViewById(id);
                String selected_string = selected_button.getText().toString();
                Snackbar.make(view, selected_string + "身份注册功能尚未开启",
                        Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "Snackbar的按钮被点击了",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton current_selected = (RadioButton) findViewById(checkedId);
                String current_string = current_selected.getText().toString();
                Snackbar.make(group, current_string + "身份被选中", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "Snackbar的按钮被点击了",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();

            }
        });
    }
}
