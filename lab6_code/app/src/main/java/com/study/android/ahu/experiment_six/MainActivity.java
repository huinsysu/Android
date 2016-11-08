package com.study.android.ahu.experiment_six;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button change_btn;
    private Button stop_btn;
    private Button quit_btn;
    private SeekBar seekbar;
    private TextView cur_time;
    private TextView end_time;
    private TextView playing_state;
    private ImageView image;
    private MusicService musicService;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private int flag = 0;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (musicService.getState() == 1) {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    seekbar.setProgress(musicService.mp.getCurrentPosition());
                    Date date = new Date(musicService.mp.getCurrentPosition());
                    cur_time.setText(time.format(date));
                    image.setRotation(musicService.getRotation());
                    break;
                case 1:
                    seekbar.setProgress(musicService.mp.getCurrentPosition());
                    date = new Date(musicService.mp.getCurrentPosition());
                    cur_time.setText(time.format(date));
                    if (flag == 0) {
                        musicService.setRotation();
                    }
                    image.setRotation(musicService.getRotation());
                    mHandler.postDelayed(mRunnable, 100);
                    break;
            }
        }
    };

    private ServiceConnection sc = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.MyBinder)(binder)).getService();
            seekbar.setMax(musicService.mp.getDuration());
            seekbar.setProgress(musicService.mp.getCurrentPosition());
            Date date = new Date(musicService.mp.getCurrentPosition());
            cur_time.setText(time.format(date));
            date = new Date(musicService.mp.getDuration());
            end_time.setText(time.format(date));
            if (musicService.getState() == 0) {
                playing_state.setText("Idle");
                change_btn.setText("PLAY");
            } else if (musicService.getState() == 1) {
                flag = 1;
                playing_state.setText("Playing");
                change_btn.setText("PAUSE");
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            } else if (musicService.getState() == 2) {
                playing_state.setText("Pause");
                change_btn.setText("PLAY");
            } else if (musicService.getState() == 3) {
                playing_state.setText("Stop");
                change_btn.setText("PLAY");
            }
            image.setRotation(musicService.getRotation());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        connection();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.mp.seekTo(seekBar.getProgress());
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindService(sc);
    }

    private void bindViews() {
        change_btn = (Button) findViewById(R.id.change_btn);
        stop_btn = (Button) findViewById(R.id.stop_btn);
        quit_btn = (Button) findViewById(R.id.quit_btn);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        cur_time = (TextView) findViewById(R.id.cur_time);
        end_time = (TextView) findViewById(R.id.end_time);
        playing_state = (TextView) findViewById(R.id.playing_state);
        image = (ImageView) findViewById(R.id.image);

        change_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        quit_btn.setOnClickListener(this);
    }

    private void connection() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                if (musicService != null) {
                    Message message = new Message();
                    int state = musicService.getState();
                    if (state == 1) {
                        change_btn.setText("PLAY");
                        playing_state.setText("Pause");
                    } else if (state == 0 || state == 2 || state == 3) {
                        flag = 0;
                        change_btn.setText("PAUSE");
                        playing_state.setText("Playing");
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                    musicService.play();
                    break;
                }
            case R.id.stop_btn:
                if (musicService != null) {
                    change_btn.setText("PLAY");
                    playing_state.setText("Stop");
                    musicService.stop();
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                    musicService.initRotation();
                    break;
                }
            case R.id.quit_btn:
                if (musicService != null) {
                    musicService.onDestroy();
                    mHandler.removeCallbacks(mRunnable);
                    unbindService(sc);
                    try {
                        MainActivity.this.finish();
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }

}
