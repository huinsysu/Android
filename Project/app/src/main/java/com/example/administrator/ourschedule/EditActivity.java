package com.example.administrator.ourschedule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.ourschedule.views.ArrayWheelAdapter;
import com.example.administrator.ourschedule.views.OnWheelChangedListener;
import com.example.administrator.ourschedule.views.WheelView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    private MyDB mydb;
    private String Est = "东";
    private String[] fiena = {"ena", "ety", "ecr", "etea", "esi", "ewn", "eti"};
    private String[] court = new String[15];
    private String[] week = new String[22];
    private int[] Ett = new int[]{1, 1, 1};
    private int[] Wtt = new int[]{1, 1};
    private int itc;
    private int itcc;

    private TextView e_title;
    private ImageView cancel;
    private ImageView confirm;
    private LinearLayout edit;
    private LinearLayout delete;
    private RadioGroup r_group;
    private RadioButton[] rbs = new RadioButton[4];
    private Button lb, eb;
    private EditText[] eta = new EditText[7];
    private android.support.v7.app.AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        itcc = this.getIntent().getIntExtra("itc", -1);

        for (int i = 0; i < Mytool.Cdata.size(); i++) {
            if (Mytool.Cdata.get(i)._datat.get(3) == itcc) {
                itc = i;
                break;
            }
        }

        mydb = new MyDB(this, "MY_DB", null, 1);

        cancel = (ImageView) findViewById(R.id.cancel);
        confirm = (ImageView) findViewById(R.id.confirm);
        edit = (LinearLayout) findViewById(R.id.edit);
        delete = (LinearLayout) findViewById(R.id.delete);
        r_group = (RadioGroup) findViewById(R.id.r_group);
        e_title = (TextView) findViewById(R.id.e_title);

        for (int i = 0; i < 15; i++) {
            court[i] = "第" + (i + 1) + "节";
        }
        for (int i = 1; i <= 22; i++) {
            week[i-1] = "第" + i + "周";
        }

        for (int i = 0; i < 4; i++) {
            try {
                Field fie = R.id.class.getDeclaredField("rbtn" + i);
                rbs[i] = (RadioButton) findViewById(fie.getInt(fie));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 8; i++) {
            try {
                Field fie = R.id.class.getDeclaredField(fiena[i]);
                eta[i] = (EditText) findViewById(fie.getInt(fie));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (itcc == -1) {
            e_title.setText("添加课程");
            eta[5].setText("1-18周");
            eta[6].setText(Mytool.WSa[Ett[0] - 1] + " " + court[Ett[1] - 1] + "到" +
                    court[Ett[2] - 1]);
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < 8; i++) {
                int index = i;
                if (i == 3 || i == 4)
                    continue;
                if (i >= 5)
                    index = i - 2;
                eta[index].setText(Mytool.Cdata.get(itc)._dataS.get(i));
                eta[index].setEnabled(false);
            }
            for (int i = 0; i < 3; i++) {
                Ett[i] = Mytool.Cdata.get(itc)._datat.get(i);
            }
            eta[6].setText(Mytool.WSa[Ett[0] - 1] + " " + court[Ett[1] - 1] + "到" +
                    court[Ett[2] - 1]);
            eta[6].setEnabled(false);
            for (int i = 0; i < 4; i++) {
                if (rbs[i].getText().toString().equals(Mytool.Cdata.get(itc)._dataS.get(4)))
                    rbs[i].setChecked(true);
                rbs[i].setClickable(false);
            }
//            cancel.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);
        }

        eta[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectTimeDialog(EditActivity.this);
            }
        });

//        eta[5].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSelectWeekDialog(EditActivity.this);
//            }
//        });

        r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int cb = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(cb);
                Est = rb.getText().toString();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_title.setText("编辑课程");
                for (int i = 0; i < 7; i++)
                    eta[i].setEnabled(true);
                for (int i = 0; i < 4; i++) {
                    rbs[i].setClickable(true);
                }
//                cancel.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 0;
                for (Mytool.Course crt : Mytool.Cdata) {
                    if (crt._datat.get(0) == Ett[0] && crt._datat.get(1) <= Ett[2] &&
                            crt._datat.get(2) >= Ett[1] && x != itc) {
                        Mytool.displayToast("课程时间与《"+crt._dataS.get(0)+"》重复", EditActivity.this);
                        return;
                    }
                    x++;
                }
                if (TextUtils.isEmpty(eta[0].getText().toString())) {
                    Mytool.displayToast("课程名称不能为空", EditActivity.this);
                    return;
                }
                ArrayList<String> ECSt = new ArrayList<>();
                ArrayList<Integer> ECIt = new ArrayList<>();

                for (int i = 0; i < 7; i++) {
                    if (i == 3) {
                        ECSt.add("");
                        ECSt.add(Est);
                    }
                    ECSt.add(eta[i].getText().toString());
                }
                for (int i = 0; i < 3; i++)
                    ECIt.add(Ett[i]);
                if (itcc == -1) {
                    int ttt = mydb._insert(new Mytool.Course(ECSt, ECIt));
                    ECIt.add(ttt);
                    Mytool.Cdata.add(new Mytool.Course(ECSt, ECIt));
                    Intent intent = new Intent(EditActivity.this
                            , MainActivity.class);
                    setResult(-1, intent);
                    finish();
                } else {
                    mydb._update(new Mytool.Course(ECSt, ECIt), Mytool.Cdata.get(itc)._datat.get(3));
                    ECIt.add(Mytool.Cdata.get(itc)._datat.get(3));
                    Mytool.Cdata.set(itc, new Mytool.Course(ECSt, ECIt));
                    Intent intent = new Intent(EditActivity.this
                            , MainActivity.class);
                    intent.putExtra("id", itcc);
                    setResult(itc, intent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this
                        , MainActivity.class);
                if (itcc == -1) {
                    setResult(-2, intent);
                } else {
                    intent.putExtra("id", itcc);
                    setResult(-2, intent);
                }
                finish();
            }
        });

        adb = new android.support.v7.app.AlertDialog.Builder(this);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydb._delete(""+itcc);
                Mytool.Cdata.remove(itc);
                Intent intent = new Intent(EditActivity.this
                        , MainActivity.class);
                intent.putExtra("id", itcc);
                setResult(itc, intent);
                finish();
            }
        });

        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adb.setTitle("删除课程").setMessage("确定删除联课程"
                        + eta[0].getText().toString() + "?").show();
            }
        });

    }

    private void showSelectWeekDialog(Context context) {
        LayoutInflater fac = LayoutInflater.from(context);
        View vi = fac.inflate(R.layout.week_wheel, null);
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("上课周数");

        final WheelView w_start = (WheelView) vi.findViewById(R.id.w_start);
        final WheelView w_end = (WheelView) vi.findViewById(R.id.w_end);

        w_start.setVisibleItems(5);
        w_start.setCyclic(false);
        w_start.setAdapter(new ArrayWheelAdapter<String>(week));
        w_start.setCurrentItem(Ett[0] - 1);
        w_end.setVisibleItems(5);
        w_end.setCyclic(false);
        w_end.setAdapter(new ArrayWheelAdapter<String>(week));
        w_end.setCurrentItem(Ett[1] - 1);

        w_start.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (w_end.getCurrentItem() < w_start.getCurrentItem())
                    w_end.setCurrentItem(w_start.getCurrentItem());
            }
        });
        w_end.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (w_end.getCurrentItem() < w_start.getCurrentItem())
                    w_end.setCurrentItem(w_start.getCurrentItem());
            }
        });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Wtt[0] = w_start.getCurrentItem() + 1;
                Wtt[1] = w_end.getCurrentItem() + 1;
                eta[5].setText(Wtt[0] + "-" + Wtt[1] + "周");
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setView(vi);
        dialog.show();

    }

    private void showSelectTimeDialog(Context context) {
        LayoutInflater fac = LayoutInflater.from(context);
        View vi = fac.inflate(R.layout.wheel, null);
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("上课时间");

        final WheelView wwv = (WheelView) vi.findViewById(R.id.wwv);
        final WheelView swv = (WheelView) vi.findViewById(R.id.swv);
        final WheelView ewv = (WheelView) vi.findViewById(R.id.ewv);

        wwv.setVisibleItems(5);
        wwv.setCyclic(false);
        wwv.setAdapter(new ArrayWheelAdapter<String>(Mytool.WSa));
        wwv.setCurrentItem(Ett[0] - 1);
        swv.setVisibleItems(5);
        swv.setCyclic(false);
        swv.setAdapter(new ArrayWheelAdapter<String>(court));
        swv.setCurrentItem(Ett[1] - 1);
        ewv.setVisibleItems(5);
        ewv.setCyclic(false);
        ewv.setAdapter(new ArrayWheelAdapter<String>(court));
        ewv.setCurrentItem(Ett[2] - 1);

        swv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (ewv.getCurrentItem() < swv.getCurrentItem())
                    ewv.setCurrentItem(swv.getCurrentItem());
            }
        });
        ewv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (ewv.getCurrentItem() < swv.getCurrentItem())
                    ewv.setCurrentItem(swv.getCurrentItem());
            }
        });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ett[0] = wwv.getCurrentItem() + 1;
                Ett[1] = swv.getCurrentItem() + 1;
                Ett[2] = ewv.getCurrentItem() + 1;
                eta[6].setText(Mytool.WSa[Ett[0] - 1] + " " + court[Ett[1] - 1] + "到" +
                        court[Ett[2] - 1]);
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setView(vi);
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(EditActivity.this
                    , MainActivity.class);
            intent.putExtra("id", itcc);
            setResult(-2, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(EditActivity.this
                , MainActivity.class);
        intent.putExtra("id", itcc);
        setResult(-2, intent);
        finish();
    }
}
