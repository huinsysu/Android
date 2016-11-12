package com.study.android.ahu.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    private EditText editToFile;
    private Button save;
    private Button load;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editToFile = (EditText) findViewById(R.id.editToFile);
        save = (Button) findViewById(R.id.save);
        load = (Button) findViewById(R.id.load);
        clear = (Button) findViewById(R.id.clearText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (FileOutputStream fileOutputStream = openFileOutput("MY_FILE", MODE_PRIVATE)) {
                    fileOutputStream.write(editToFile.getText().toString().getBytes());
                    Toast.makeText(EditActivity.this, "Save successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Log.e("TAG", "Fail to save file.");
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (FileInputStream fileInputSteam = openFileInput("MY_FILE")) {
                    byte[] contents = new byte[fileInputSteam.available()];
                    fileInputSteam.read(contents);
                    editToFile.setText(new String(contents));
                    Toast.makeText(EditActivity.this, "Load successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Toast.makeText(EditActivity.this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editToFile.setText("");
            }
        });
    }
}
