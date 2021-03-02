package com.example.iwen.broadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化频道
        IntentFilter intentFilter = new IntentFilter();
        // 添加我们要监听的频道
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        // 注册广播
        this.registerReceiver(new BroadcastReceiver(),intentFilter);
    }

    /**
     * 创建一个广播接收器
     */
    private class BroadcastReceiver extends android.content.BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"收到电量变化的广播 --- "+action);
        }
    }
}