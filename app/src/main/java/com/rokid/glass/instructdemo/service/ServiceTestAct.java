package com.rokid.glass.instructdemo.service;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instructdemo.ITestService;
import com.rokid.glass.instructdemo.R;

/**
 * @author : liuweiming
 * @date : 2021/6/9
 * @description :
 * @parameter :
 */
public class ServiceTestAct extends AppCompatActivity {

    private static final String TAG = "AudioAi " + ServiceTestAct.class.getSimpleName();
    private String[] enInstrucArray = new String[] {"move front", "move back", "move left", "move right", "move fast"};
    private Intent serviceIntent;
    private ITestService mTestService;

    private TextView mTextView;
    private Button mButton;
    private int index;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        initViews();
        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName(getPackageName(), "com.rokid.glass.instructdemo.service.InstructTestService"));
        bindService(serviceIntent, mConnection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mTestService = ITestService.Stub.asInterface(service);
                Log.d(TAG, "server ServiceConnection onServiceConnected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mTestService = null;
            Log.d(TAG, "server ServiceConnection onServiceDisconnected");
        }
    };

    private void initViews() {
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mTestService != null) {
                        int position = index % enInstrucArray.length;
                        mTestService.addInstruct(EntityKey.Language.zh.name(), enInstrucArray[position]);
                        index = index + 1;
                        mTestService.sendInstruct();
                        mTextView.setText("add instruct : ---> " + enInstrucArray[position]);
                        Log.d(TAG, "Button click");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        mTestService = null;
        super.onDestroy();
    }
}
