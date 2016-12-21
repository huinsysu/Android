package com.example.administrator.ourschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import java.util.ArrayList;


public class CurrentActivity extends AppCompatActivity {
    private String[] dir = {"东", "南", "北", "珠海"};
    private String di0, di1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        ImageView bac = (ImageView) findViewById(R.id.bac);

        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentActivity.this
                        , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout tal = (LinearLayout) findViewById(R.id.tal);
        TextView can0 = (TextView) findViewById(R.id.cna0);
        TextView can1 = (TextView) findViewById(R.id.cna1);
        TextView cti0 = (TextView) findViewById(R.id.cti0);
        TextView cti1 = (TextView) findViewById(R.id.cti1);
        TextView csi0 = (TextView) findViewById(R.id.csi0);
        TextView csi1 = (TextView) findViewById(R.id.csi1);
        ImageView map0 = (ImageView) findViewById(R.id.map0);
        ImageView map1 = (ImageView) findViewById(R.id.map1);

        Mytool.Course ct0 = Mytool.getncCo(0);
        Mytool.Course ct1 = Mytool.getncCo(1);

        if (ct0 == null)
            tal.setVisibility(View.GONE);
        else {
            ArrayList<Integer> tia = ct0._datat;
            int[] tsa0 = Mytool.gettime(tia.get(1), tia.get(2));
            can0.setText(ct0._dataS.get(0));
            di0 = ct0._dataS.get(4);
            csi0.setText(ct0._dataS.get(4) + "校区——" + ct0._dataS.get(6));
            cti0.setText(Mytool.InttoWeek(tia.get(0)) + "" + tia.get(1) + "-" + tia.get(2) + "节##"
                    + Mytool.getTS(tsa0[0], tsa0[1]) + "--" + Mytool.getTS(tsa0[2], tsa0[3]));

            map0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int dt = 0;
                    for (int i = 0; i < 4; i++) {
                        if (dir[i].equals(di0))
                            dt = i;
                    }
                    Intent intent = new Intent(CurrentActivity.this
                            , MapActivity.class);
                    intent.putExtra("dir", dt);
                    startActivity(intent);
                }
            });
        }
        ArrayList<Integer> tia = ct1._datat;
        int[] tsa0 = Mytool.gettime(tia.get(1), tia.get(2));
        can1.setText(ct1._dataS.get(0));
        di1 = ct1._dataS.get(4);
        csi1.setText(ct1._dataS.get(4) + "校区——" + ct1._dataS.get(6));
        cti1.setText(Mytool.InttoWeek(tia.get(0)) + "" + tia.get(1) + "-" + tia.get(2) + "节##"
                + Mytool.getTS(tsa0[0], tsa0[1]) + "--" + Mytool.getTS(tsa0[2], tsa0[3]));

        map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dt = 0;
                for (int i = 0; i < 4; i++) {
                    if (dir[i].equals(di1))
                        dt = i;
                }
                Intent intent = new Intent(CurrentActivity.this
                        , MapActivity.class);
                intent.putExtra("dir", dt);
                startActivity(intent);
            }
        });
    }
}
