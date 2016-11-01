package com.study.android.ahu.experimentfour;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DynamicActivity extends AppCompatActivity {

    private static final String DYNAMICACTION = "com.study.android.ahu.experimentfour.dynamicreceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        final Button btnLeft = (Button) findViewById(R.id.register);
        Button btnRight = (Button) findViewById(R.id.send);
        final EditText editText = (EditText) findViewById(R.id.editText);

        final IntentFilter dynamic_filter = new IntentFilter();
        final DynamicReceiver dynamicReceiver = new DynamicReceiver();
        dynamic_filter.addAction(DYNAMICACTION);

        btnLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (btnLeft.getText().toString().equals("Register Broadcast")) {
                    btnLeft.setText("Unregister Broadcast");
                    registerReceiver(dynamicReceiver, dynamic_filter);

                } else {
                    btnLeft.setText("Register Broadcast");
                    unregisterReceiver(dynamicReceiver);
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                Bundle myBundle = new Bundle();
                myBundle.putString("text", str);
                Intent myIntent = new Intent(DYNAMICACTION);
                myIntent.putExtras(myBundle);

                sendBroadcast(myIntent);
            }
        });
    }
}
