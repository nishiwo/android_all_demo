package com.xt.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

import com.xt.client.activitys.JNIActivity;
import com.xt.client.activitys.PerformanceActivity;
import com.xt.client.activitys.PrepareActivity;
import com.xt.client.activitys.SaveLastActivity;
import com.xt.client.aidl.ProcessAidlInter;
import com.xt.client.fragment.AidlFragment;
import com.xt.client.fragment.ProtobuffFragment;
import com.xt.client.fragment.TryCrashFragment;
import com.xt.client.widget.MainPageItemView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author aa5279aa
 * @date 2019/6/11
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    static final String TAG = "lxltest";
    Messenger mMessager;
    ProcessAidlInter processAidlInter;
    List<Long> list = new ArrayList<>();
    FragmentManager manager;
    volatile StringBuilder printerBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lxltest", "MainActivity:onCreate");
        setContentView(R.layout.main_page);

        MessageQueue.IdleHandler mHander = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                return false;
            }
        };

        manager = getSupportFragmentManager();
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setOnClickListener(this);
        }
        Choreographer choreographer = Choreographer.getInstance();
        choreographer.postFrameCallback(frameCallback);




        MessageQueue.IdleHandler idelHandler =new  MessageQueue.IdleHandler(){


            @Override
            public boolean queueIdle() {
                return false;
            }
        };
    }


    long time = System.currentTimeMillis();


    //zhe ao ge ruo fe
    Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            Choreographer.getInstance().postFrameCallback(frameCallback);
            long nowtime = System.currentTimeMillis();
            long delay = nowtime - time;
            list.add(delay);
            time = nowtime;
        }
    };

    @Override
    public void onClick(View v) {
        if (!(v instanceof MainPageItemView)) {
            return;
        }
        MainPageItemView view = (MainPageItemView) v;
        String title = view.getTitle();
        Fragment fragment = null;
        if (getString(R.string.protobuff).equalsIgnoreCase(title)) {
            fragment = new ProtobuffFragment();
        } else if (getString(R.string.instrumentation).equalsIgnoreCase(title)) {
            fragment = new ProtobuffFragment();
        } else if (getString(R.string.catch_crash).equalsIgnoreCase(title)) {
            fragment = new TryCrashFragment();
        } else if (getString(R.string.use_aidl).equalsIgnoreCase(title)) {
            fragment = new AidlFragment();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            return;
        }

        Intent intent = new Intent();
        if (getString(R.string.get_last_activity).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, SaveLastActivity.class);
        } else if (getString(R.string.jni_use).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, JNIActivity.class);
        } else if (getString(R.string.performance_check).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, PerformanceActivity.class);
        } else if (getString(R.string.prepareloadview).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, PrepareActivity.class);
        } else {
            return;
        }


        startActivity(intent);

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i("lxltest", "------>receive response from server!" + msg.what);
        }
    }


    private void sendMessage() {
        if (mMessager == null) {
            return;
        }
        Message msg = new Message();
        msg.what = 1;
        msg.replyTo = new Messenger(new MyHandler());
        Bundle bundle = new Bundle();
        bundle.putString("key", "lxl");
        msg.setData(bundle);
        try {
            mMessager.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void startRequest() throws Exception {

//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {

        try {
            String url = "https://www.csdn.net/";
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            });
//                    call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lxltest", "MainActivity:onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lxltest", "MainActivity:onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lxltest", "MainActivity:onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lxltest", "MainActivity:onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lxltest", "MainActivity:onDestroy");
        int i = 0;
        StringBuilder builder = new StringBuilder();
//        Log.i("lxltest", "size:" + list.size());
        for (long time : list) {
            if (i == 60) {
                Log.i("lxltest", builder.toString());
                builder.setLength(0);
                i = 0;
            } else {
                i++;
                builder.append(time + ",");
            }
        }
    }


}