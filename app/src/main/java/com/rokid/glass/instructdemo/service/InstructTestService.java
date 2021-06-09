package com.rokid.glass.instructdemo.service;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.rokid.glass.instruct.InstructLifeManager;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instructdemo.ITestService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : liuweiming
 * @date : 2021/6/9
 * @description :
 * @parameter :
 */
public class InstructTestService extends Service implements LifecycleOwner {

    private static final String TAG = "AudioAi " + InstructTestService.class.getSimpleName();

    private LifecycleRegistry mLifecycleRegistry;
    private InstructLifeManager mLifeManager;
    private List<InstructEntity> mInstructEntityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mLifecycleRegistry = new LifecycleRegistry(this);
        mInstructEntityList = new ArrayList<>();
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        Log.d(TAG, "InstructTestService onCreate()");
    }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {
        @Override
        public boolean onInterceptCommand(String command) {
            return false;
        }

        @Override
        public void onTipsUiReady() {
            // no UI, can not be call back
        }

        @Override
        public void onHelpLayerShow(boolean show) {
            // no UI, can not be call back
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "InstructTestService onBind()");
        return testService.asBinder();
    }

    private ITestService testService = new ITestService.Stub() {

        @Override
        public void addInstruct(String type, String item) throws RemoteException {
            Log.d(TAG, "ITestService addInstruct item = " + item);
            if (TextUtils.isEmpty(type) || TextUtils.isEmpty(item)) {
                return;
            }
            InstructEntity entity = new InstructEntity();
            entity.addEntityKey(new EntityKey(
                    (EntityKey.Language.zh.name().equals(type.toLowerCase()) ? EntityKey.Language.zh
                            : EntityKey.Language.en), item));
            entity.setCallback(new IInstructReceiver() {
                @Override
                public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                    //  act is null
                    Log.d(TAG, "tool instruct call back: --> " + key);
                }
            });
            if (mInstructEntityList != null) {
                mInstructEntityList.add(entity);
            }
        }

        @Override
        public void sendInstruct() throws RemoteException {
            Log.d(TAG, "ITestService sendInstruct ");
            if (mLifeManager != null) {
                mLifeManager.clearUserInstruct();
                mLifeManager.addInstructList(mInstructEntityList);
                mLifeManager.sendWtWords();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "InstructTestService onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "InstructTestService onDestroy()");
        if (mLifecycleRegistry != null) {
            mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        }

        if (mLifeManager != null) {
            mLifeManager.onDestroy();
            mLifeManager = null;
        }
        super.onDestroy();
    }
}
