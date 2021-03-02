package com.example.iwen.broadcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import com.example.iwen.broadcast.databinding.ActivityMainBinding;

import java.net.URISyntaxException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用ViewBinding后的方法
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        registerBatteryReceiver();
    }

    private void registerBatteryReceiver() {
        // 初始化频道
        IntentFilter intentFilter = new IntentFilter();
        // 添加我们要监听的频道
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        // 注册广播
        this.registerReceiver(new BroadcastReceiver(), intentFilter);
    }

    /**
     * 创建一个广播接收器
     */
    private class BroadcastReceiver extends android.content.BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "收到电量变化的广播 --- " + action);
            Log.d(TAG, "当前电量 --- " + intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
            mBinding.tvLever.setText("当前电量 --- " + intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
        }
    }
}