package com.study.android.ahu.experimentfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button staticButton = (Button) findViewById(R.id.staticButton);
        Button dynamicButton = (Button) findViewById(R.id.dynamicButton);

        staticButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, StaticActivity.class);
                startActivity(myIntent);
            }
        });

        dynamicButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, DynamicActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
