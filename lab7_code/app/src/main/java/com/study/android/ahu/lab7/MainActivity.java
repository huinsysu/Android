package com.study.android.ahu.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button ok;
    private Button clear;
    private EditText password;
    private EditText confirmPassword;
    private SharedPreferences sharePref;
    private String fromPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ok = (Button) findViewById(R.id.ok);
        clear = (Button) findViewById(R.id.clear);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        sharePref = getSharedPreferences("MY_PREFERENCE",
                Context.MODE_PRIVATE);
        fromPref = sharePref.getString("password", "not exit");

        if (!fromPref.equals("not exit")) {
            password.setHint("Password");
            confirmPassword.setVisibility(View.GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromPref = sharePref.getString("password", "not exit");
                if (fromPref.equals("not exit")) {
                    if (TextUtils.isEmpty(password.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty.",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password Mismatch.",
                                Toast.LENGTH_SHORT).show();
                    } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password Mismatch.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharePref.edit();
                        editor.putString("password", password.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (password.getText().toString().equals(fromPref)) {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setText("");
                confirmPassword.setText("");
            }
        });

    }

}

