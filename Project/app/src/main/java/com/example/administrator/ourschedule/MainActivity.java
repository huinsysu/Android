package com.example.administrator.ourschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyDB mydb;

    private RelativeLayout parentRl;
    private CustomListView listView;
    private LinearLayout courseTable;
    private LinearLayout allButton;
    private TextView addButton;
    private Button lin;
    private Button cre;
    private Button cur;

    private List<Map<String, Object>> data;
    private int wScreen;
    private int start;
    private int end;
    private int week;
    private String courseName;
    private String classRoom;
    private int itemHeight;
    private boolean visiable;

    private String [] period = {"8:00", "8:55", "9:50", "10:45", "11:40", "12:35", "13:30", "14:25",
            "15:20", "16:15", "17:10", "18:05", "19:00", "19:55", "20:50"};
    private int [] itemColor = {R.color.colorPinkPurple, R.color.colorPinkRed, R.color.colorMudYellow,
            R.color.colorBlueGreen, R.color.colorLightBlue ,R.color.colorDarkRed,
            R.color.colorGrassGreen, R.color.colordarkBule, R.color.colorOrange,
            R.color.colorLightPurple, R.color.colorGreenBlue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mytool.isRunning = true;

        mydb = new MyDB(this, "MY_DB", null, 1);
        lin = (Button) findViewById(R.id.lin);
        cre = (Button) findViewById(R.id.cre);
        cur = (Button) findViewById(R.id.cur);
        listView = (CustomListView) findViewById(R.id.listView);
        parentRl = (RelativeLayout) findViewById(R.id.relativeLayout);
        courseTable = (LinearLayout) findViewById(R.id.courseTable);
        allButton = (LinearLayout) findViewById(R.id.allButton);
        addButton = (TextView) findViewById(R.id.addButton);

        data = new ArrayList<>();
        visiable = false;

        if (Mytool.Cdata == null)
            Mytool.Cdata = mydb.query_all();
        if (Mytool.Cdata.size() == 0) {
            Mytool.isRunning = false;
            Intent intent = new Intent(MainActivity.this
                    , LoginActivity.class);
            startActivity(intent);
            finish();
        }


        for (int i = 0; i < 15; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("period", period[i]);
            temp.put("lessonNum", i+1);
            data.add(temp);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.period,
                new String[]{"period", "lessonNum"}, new int[]{R.id.period, R.id.lesson_num});
        listView.setAdapter(simpleAdapter);

        View listItem = simpleAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        itemHeight = listItem.getMeasuredHeight();

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        wScreen = outMetrics.widthPixels;

        for (int i = 0; i < Mytool.Cdata.size(); i++) {
            week = Mytool.Cdata.get(i)._datat.get(0);
            start = Mytool.Cdata.get(i)._datat.get(1);
            end = Mytool.Cdata.get(i)._datat.get(2);
            courseName = Mytool.Cdata.get(i)._dataS.get(0);
            classRoom = Mytool.Cdata.get(i)._dataS.get(6);
            addCourse(MainActivity.this, i);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visiable == false) {
                    visiable = true;
                } else {
                    visiable = false;
                }
                lin.setClickable(visiable);
                cre.setClickable(visiable);
                cur.setClickable(visiable);
                myStartAnimation(visiable);
            }
        });

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiable = false;
                lin.setClickable(visiable);
                cre.setClickable(visiable);
                cur.setClickable(visiable);
                myStartAnimation(visiable);

                Intent intent = new Intent(MainActivity.this
                        , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiable = false;
                lin.setClickable(visiable);
                cre.setClickable(visiable);
                cur.setClickable(visiable);
                myStartAnimation(visiable);

                Intent intent = new Intent(MainActivity.this
                        , EditActivity.class);
                intent.putExtra("itc", -1);
                startActivityForResult(intent, 0);
            }
        });

        cur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiable = false;
                lin.setClickable(visiable);
                cre.setClickable(visiable);
                cur.setClickable(visiable);
                myStartAnimation(visiable);

                Intent intent = new Intent(MainActivity.this
                        , CurrentActivity.class);
                startActivity(intent);
            }
        });

        Intent inten = new Intent(this, MService.class);
        startService(inten);

        Mytool.updateWidget(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        visiable = false;
        lin.setClickable(visiable);
        cre.setClickable(visiable);
        cur.setClickable(visiable);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(10);
        alphaAnimation.setFillAfter(true);
        allButton.startAnimation(alphaAnimation);

        if (resultCode == -2) {
        } else if (resultCode == -1) {
            int temp = Mytool.Cdata.size() - 1;
            week = Mytool.Cdata.get(temp)._datat.get(0);
            start = Mytool.Cdata.get(temp)._datat.get(1);
            end = Mytool.Cdata.get(temp)._datat.get(2);
            courseName = Mytool.Cdata.get(temp)._dataS.get(0);
            classRoom = Mytool.Cdata.get(temp)._dataS.get(6);
            addCourse(MainActivity.this, temp);
        } else {
            int id = data.getIntExtra("id", 0);
            int i;
            for (i = 0; i < Mytool.Cdata.size(); i++) {
                if (Mytool.Cdata.get(i)._datat.get(3) == id) {
                    break;
                }
            }
            parentRl.removeViewAt(resultCode + 1);

            if (i < Mytool.Cdata.size()) {
                week = Mytool.Cdata.get(resultCode)._datat.get(0);
                start = Mytool.Cdata.get(resultCode)._datat.get(1);
                end = Mytool.Cdata.get(resultCode)._datat.get(2);
                courseName = Mytool.Cdata.get(resultCode)._dataS.get(0);
                classRoom = Mytool.Cdata.get(resultCode)._dataS.get(6);
                addCourse(MainActivity.this, resultCode);
            }
        }
    }

    private void addCourse(Context context, int i) {
        final int index = i;
        RelativeLayout courseParent = new RelativeLayout(context);

        int margin1px = getResources().getDimensionPixelSize(R.dimen.fragment_class_course_margin1dp);
        int marginLeftMostPx = getResources().getDimensionPixelSize(R.dimen.left_most_width);
        int lessonWidth = (wScreen - marginLeftMostPx) / 7;

        int lessonHeight = itemHeight;
        int courseHeight = (lessonHeight + end - start - 1) * (end - start + 1);

        RelativeLayout.LayoutParams layoutParams;
        layoutParams = new RelativeLayout.LayoutParams(lessonWidth, courseHeight);

        layoutParams.setMargins(marginLeftMostPx + lessonWidth * (week - 1),
                (start - 1) * (lessonHeight + margin1px), 0, 0);
        courseParent.setLayoutParams(layoutParams);
        courseParent.setBackgroundColor(ContextCompat.getColor(context, itemColor[i % 11]));

        LinearLayout courseLayout = new LinearLayout(context);
        courseLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        courseLayout.setOrientation(LinearLayout.VERTICAL);

        TextView courseText = new TextView(context);
        courseText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        courseText.setText(courseName);
        float textSize = getResources().getDimensionPixelSize(R.dimen.course_text_size);
        courseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        courseText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        courseText.setPadding(12, 12, 12, 0);
        courseLayout.addView(courseText);

        TextView classText = new TextView(context);
        classText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        classText.setText("@" + classRoom);
        textSize = getResources().getDimensionPixelSize(R.dimen.course_text_size);
        classText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        classText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        classText.setPadding(12, 0, 12, 12);
        courseLayout.addView(classText);

        courseParent.addView(courseLayout);
        parentRl.addView(courseParent, index + 1);

        final int itcc = Mytool.Cdata.get(index)._datat.get(3);
        courseParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("index", ""+index);
                Log.v("id", ""+itcc);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("itc", itcc);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void myStartAnimation(boolean isVisiable) {

        float fromState;
        float toState;

        if (isVisiable) {
            fromState = 0.0f;
            toState = 1.0f;
        } else {
            fromState = 1.0f;
            toState = 0.0f;
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(fromState, toState);
        alphaAnimation.setDuration(800);
        alphaAnimation.setFillAfter(true);

        allButton.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent inten = new Intent(this, MService.class);
        stopService(inten);
    }
}

