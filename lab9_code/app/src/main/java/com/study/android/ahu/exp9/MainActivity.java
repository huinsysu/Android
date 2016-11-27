package com.study.android.ahu.exp9;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    private static final int UPDATE_CONTENT = 1;

    List<Map<String, Object>> data;

    private EditText city;
    private Button searchButton;
    private LinearLayout blockTitle;
    private LinearLayout block;

    private TextView resultCity;
    private TextView resultCurrentTime;

    private TextView resultCurrentTemperature;
    private TextView resultTemperature;
    private TextView resultShidu;
    private TextView resultAirQuality;
    private TextView resultFengli;

    private ListView detailList;
    private TextView detailkey;
    private TextView detailValue;

    private RecyclerView recyclerView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:

                    ArrayList<Weather> weather_list = new ArrayList<Weather>();
                    List rcvList = (List) msg.obj;

                    if (rcvList.get(0).toString()
                            .equals("发现错误：免费用户24小时内访问超过规定数量。http://www.webxml.com.cn/")) {
                        Toast.makeText(MainActivity.this, "免费用户24小时内访问超过规定数量。",
                                Toast.LENGTH_SHORT).show();
                    } else if (rcvList.get(0).toString().equals("查询结果为空")) {
                        Toast.makeText(MainActivity.this, "当前城市不存在，请重新输入。", Toast.LENGTH_SHORT).show();
                    } else {
                        blockTitle.setVisibility(View.VISIBLE);
                        block.setVisibility(View.VISIBLE);

                        String cityName = rcvList.get(1).toString();
                        String updateTime = rcvList.get(3).toString();
                        String info1 = rcvList.get(4).toString();
                        String info2 = rcvList.get(5).toString();
                        String infoDetail = rcvList.get(6).toString();

                        resultCity.setText(cityName);
                        resultCurrentTime.setText(updateTime.split(" ")[1] + " 更新");

                        if (info1.equals("今日天气实况：暂无实况")) {
                            resultCurrentTemperature.setText("暂无");
                            resultTemperature.setText(rcvList.get(8).toString());
                            resultFengli.setText("湿度：暂无预报");
                            resultShidu.setText("空气质量：暂无预报");
                            resultAirQuality.setText("风力：暂无预报");
                        } else {
                            String[] allTemperature = info1.split("；");
                            resultCurrentTemperature.setText((allTemperature[0].split("："))[2]);
                            resultTemperature.setText(rcvList.get(8).toString());
                            resultFengli.setText((allTemperature[1].split("："))[1]);
                            resultShidu.setText(allTemperature[2]);
                            resultAirQuality.setText((info2.split("。"))[1]);

                            data = new ArrayList<>();
                            String[] details = infoDetail.split("。");

                            for (int i = 0; i < 5; i++) {
                                Map<String, Object> tempItem = new LinkedHashMap<>();
                                tempItem.put("key", (details[i].split("："))[0]);
                                tempItem.put("value", (details[i].split("："))[1]);
                                data.add(tempItem);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.detail_item,
                                    new String[]{"key", "value"}, new int[]{R.id.detail_key, R.id.detail_value});
                            detailList.setAdapter(simpleAdapter);
                        }

                        for (int i = 0; i < 5; i++) {
                            Weather tempWeather = new Weather();
                            tempWeather.setDate((rcvList.get(7 + i * 5).toString()).split(" ")[0]);
                            tempWeather.setWeather_description(rcvList.get(9 + i * 5).toString());
                            tempWeather.setTemperature(rcvList.get(8 + i * 5).toString());
                            weather_list.add(tempWeather);
                        }

                        WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, weather_list);
                        recyclerView.setAdapter(adapter);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (EditText) findViewById(R.id.edit_city);
        searchButton = (Button) findViewById(R.id.search);
        blockTitle = (LinearLayout) findViewById(R.id.block_title);
        block = (LinearLayout) findViewById(R.id.block);

        resultCity = (TextView) findViewById(R.id.result_city);
        resultCurrentTime = (TextView) findViewById(R.id.result_current_time);
        resultCurrentTemperature = (TextView) findViewById(R.id.result_current_temperature);
        resultTemperature = (TextView) findViewById(R.id.result_temperature);
        resultShidu = (TextView) findViewById(R.id.result_shidu);
        resultAirQuality = (TextView) findViewById(R.id.result_air_quality);
        resultFengli = (TextView) findViewById(R.id.result_fengli);

        detailList = (ListView) findViewById(R.id.detail_list);
        //detailkey = (TextView) findViewById(R.id.detail_key);
        //detailValue = (TextView) findViewById(R.id.detail_value);

        recyclerView = (RecyclerView) findViewById(R.id.weather_horizontal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    sendRequestWithHttpURLConnection();
                } else {
                    Toast.makeText(MainActivity.this, "当前没有网络可用！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadWebpageText task = new DownloadWebpageText();
                task.execute(url);
            }
        }).start();
    }

    private class DownloadWebpageText extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            return downloadUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(result));
                    List entries = new ArrayList();

                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tag = parser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tag.equals("string")) {
                                    String str = parser.nextText();
                                    entries.add(str);
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = entries;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String downloadUrl(String myurl) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) ((new URL(myurl).openConnection()));
            connection.setRequestMethod("POST");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String request = city.getText().toString();
            request = URLEncoder.encode(request, "utf-8");
//            out.writeBytes("theCityCode=" + request + "&theUserID=");
            out.writeBytes("theCityCode=" + request + "&theUserID=31c7445c850847b2be51b472d6cad3b9");
//            out.writeBytes("theCityCode=" + request + "&theUserID=6d29419b4339467ea35c50f837129ff5");

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputFromServer = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputFromServer));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String result = response.toString();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
