package com.example.administrator.ourschedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private String st0 = "2016-2017", st1 = "1", cookie = "", sid, resp;
    private Bitmap bm;
    private MyDB mydb;
    private boolean emp = false;
    private Button lb;
    private EditText jce, lna, pa;
    private AlertDialog.Builder bui;
    private static final String url0 =
            "http://uems.sysu.edu.cn/elect";
    private static final String url1 =
            "http://uems.sysu.edu.cn/elect/s/courseAll?xnd=";

    private void URLConnection0() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url0)).openConnection());
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.connect();

                    cookie = connection.getHeaderField("Set-Cookie").split(";")[0];

                    Message mess = new Message();
                    mess.what = 0;
                    handler.sendMessage(mess);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private void URLConnection1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url0 + "/login/code"))
                            .openConnection());
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setRequestProperty("Cookie", cookie);
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(in);
                    in.close();

                    Message mess = new Message();
                    mess.what = 1;
                    handler.sendMessage(mess);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private void URLConnection2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url0 + "/login")).openConnection());
                    connection.setRequestProperty("Cookie", cookie);
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.connect();

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String ust1 = lna.getText().toString();
                    String ust2 = Mytool.getMD5(pa.getText().toString());
                    String ust3 = jce.getText().toString();
                    out.writeBytes("username=" + ust1 + "&password=" + ust2 + "&j_code=" + ust3 +
                            "&lt=&_eventId=submit&gateway=true");
                    out.close();

                    InputStream in = null;
                    Message mess = new Message();
                    if (connection.getResponseCode() != 200) {
                        mess.what = 7;
                        handler.sendMessage(mess);
                    } else {
                        in = connection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder respon = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            respon.append(line);
                        }
                        in.close();

                        sid = respon.toString();
                        sid = sid.split("xnd=2016-2017\\&xq=1")[1];
                        sid = sid.split("\"")[0];

                        mess.what = 2;
                        handler.sendMessage(mess);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private void URLConnection3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    if (st0.equals("2015-2016") && st1.equals("1"))
                        st1 = "2";
                    else if (st0.equals("2015-2016"))
                        st1 = "3";
                    connection = (HttpURLConnection) ((new URL(url1 + st0 + "&xq=" + st1 + sid)).openConnection());
                    connection.setRequestProperty("Cookie", cookie);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.connect();

                    InputStream in = null;
                    if (connection.getResponseCode() == 200)
                        in = connection.getInputStream();
                    else
                        in = connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder respon = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        respon.append(line);
                    }
                    in.close();

                    resp = respon.toString();
                    Message mess = new Message();
                    mess.what = 3;
                    handler.sendMessage(mess);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                URLConnection1();
            } else if (msg.what == 1) {
                LayoutInflater fac = LayoutInflater.from(LoginActivity.this);
                View vi = fac.inflate(R.layout.dialog, null);
                bui.setView(vi);
                ImageView jc = (ImageView) vi.findViewById(R.id.jc);
                jce = (EditText) vi.findViewById(R.id.jce);
                jc.setImageBitmap(bm);
                bui.setTitle("验证码");
                bui.create().show();
            } else if (msg.what == 2) {
                mydb._deleteall();
                URLConnection3();
            } else if (msg.what == 7) {
                Mytool.displayToast("获取失败，请检查输入", LoginActivity.this);
            } else if (msg.what == 3) {
                Mytool.Cdata = parseHTML(resp);
                if (emp == true) {
                    Mytool.displayToast("课程为空", LoginActivity.this);
                }
                else {
                    Intent intent = new Intent(LoginActivity.this
                            , MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mydb = new MyDB(this, "MY_DB", null, 1);
        lb = (Button) findViewById(R.id.lb);
        lna = (EditText) findViewById(R.id.lna);
        pa = (EditText) findViewById(R.id.pa);
        final RadioGroup rg0 = (RadioGroup) findViewById(R.id.rg0);
        final RadioGroup rg1 = (RadioGroup) findViewById(R.id.rg1);
        bui = new AlertDialog.Builder(LoginActivity.this);

        rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int cb = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(cb);
                st0 = rb.getText().toString().split("学年")[0];

            }
        });
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int cb = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(cb);
                if (rb.getText().toString().equals("上学期"))
                    st1 = "1";
                else
                    st1 = "2";
            }
        });

        bui.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (jce.getText().toString().equals(""))
                    Mytool.displayToast("请输入验证码", LoginActivity.this);
                else
                    URLConnection2();
            }
        });

        bui.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lna.getText().toString().equals("") || lna.getText().toString().equals(""))
                    Mytool.displayToast("请输入用户名和密码", LoginActivity.this);
                else {
                    if (!Mytool.isNetworkAvailable(LoginActivity.this))
                        Mytool.displayToast("当前没有可用网络！", LoginActivity.this);
                    else
                        URLConnection0();
                }
            }
        });
    }

    private ArrayList<Mytool.Course> parseHTML(String str) {
        emp = false;
        ArrayList<Mytool.Course> HCl = new ArrayList<>();
        try {
            String Hst = str.split("table")[5];
            String[] Hsa = Hst.split("tr");
            String[] Hsa1 = null;
            for (int i = 3; i < Hsa.length; i += 2) {
                Hst = Hsa[i];
                ArrayList<String> Hsl = new ArrayList<>();
                Hsl.add(Hst.split("td")[7].split(">")[2].split("<")[0]);
                for (int j = 9; j <= 19; j += 2) {
                    if (j != 15)
                        Hsl.add(Hst.split("td")[j].split(">")[1].split("<")[0]
                                .replaceAll("\\s*", ""));
                    else
                        Hsa1 = Hst.split("td")[j].split(">")[1].split("<")[0].split(",");

                }
                ArrayList<Integer> Hiback = null;
                for (int j = 0; j < Hsa1.length; j++) {
                    ArrayList<String> Hsl1 = new ArrayList<>(Hsl);
                    ArrayList<Integer> Hil = new ArrayList<>();
                    try {
                        String[] Hsa2 = Hsa1[j].split(" ");
                        Hil.add(Mytool.WeektoInt(Hsa2[0]));
                        Hil.add(Integer.parseInt(Hsa2[1].split("-")[0]));
                        Hil.add(Integer.parseInt(Hsa2[1].split("-")[1].split("节")[0]));
                        Hsl1.add(Hsa2[2].split("/")[1].split("（")[0]);
//                        Hil.add(Integer.parseInt(Hsa2[2].split("/")[1].split("（")[0].
//                                split("-")[0]));
//                        Hil.add(Integer.parseInt(Hsa2[2].split("/")[1].split("（")[0].
//                                split("-")[1]));
                        Hsl1.add(Hsa2[2].split("/")[1].split("（")[1]);
                    } catch (Exception e) {
                        for (int x = Hsl1.size(); x < 8; x++)
                            Hsl1.add("");
                    }
                    if (j == 0) {
                        Hiback = new ArrayList<>(Hil);
                    }
                    if (Hil.size() == 3) {
                        if (j != 0 && Hiback.get(0) == Hil.get(0) && Hiback.get(1) == Hil.get(1) &&
                                Hiback.get(2) == Hil.get(2)) {
                        } else {
                            int ttt = mydb._insert(new Mytool.Course(Hsl1, Hil));
                            Hil.add(ttt);
                            HCl.add(new Mytool.Course(Hsl1, Hil));
                        }
                    }
                }
            }
        } catch (Exception e) {
            emp = true;
        }
        if (HCl.size() == 0)
            emp = true;
        return HCl;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Mytool.isRunning) {
                Intent intent = new Intent(LoginActivity.this
                        , MainActivity.class);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
