package com.rokid.glass.instructdemo.speech;


import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rokid.glass.instruct.InstructLifeManager;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instructdemo.R;
import com.rokid.glass.speech.AsrCallBack;
import com.rokid.glass.speech.SpeechUserManager;
import com.rokid.glass.speech.TtsCallBack;

/**
 * Func:
 *
 * @author: liuweiming
 * @version: 1.0
 * Create Time: 2019-07-29
 */
public class SpeechTestAct extends AppCompatActivity {

    private static final String TAG = "AudioAi " + SpeechTestAct.class.getSimpleName();
    private InstructLifeManager mLifeManager;
    private ImageView mImageView;

    private SpeechUserManager mSpeechManager;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_test);
        configInstruct();

        mImageView = findViewById(R.id.space_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpeechTestAct.this, "点击测试", Toast.LENGTH_SHORT).show();
            }
        });

        mSpeechManager = new SpeechUserManager(this, false);
    }

    public void configInstruct() {
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        mLifeManager.getInstructConfig().setIgnoreSystem(true);
        mLifeManager.addInstructEntity(
                new InstructEntity()
                        .addEntityKey(new EntityKey("在线测试", "zai xian ce shi"))
                        .addEntityKey(new EntityKey(EntityKey.Language.en, "online test"))
                        .setShowTips(true)
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                if (mSpeechManager != null) {
                                    mSpeechManager.doSpeechAsr(mAsrCallBack);
                                }
                            }
                        })
                ).addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("声音测试", "sheng yin ce shi"))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "tts test"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        if (mSpeechManager != null) {
                                            mSpeechManager.doSpeechTts("乌龙茶饮料很好喝", mTtsCallBack);
                                        }
                                    }
                                })
                );
    }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {
        @Override
        public boolean onInterceptCommand(String command) {
            return false;
        }

        @Override
        public void onTipsUiReady() {
            Log.d("AudioAi", "onTipsUiReady Call ");
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };

    private AsrCallBack mAsrCallBack = new AsrCallBack() {
        @Override
        public void onStart(int id) throws RemoteException {
            Log.d(TAG, "AsrCallBack onStart id = " + id);
        }

        @Override
        public void onIntermediateResult(int id, String asr, String extra) throws RemoteException {
            Log.d(TAG, "AsrCallBack onIntermediateResult id = " + id+ ", asr = " + asr + ", extra = " + extra);
        }

        @Override
        public void onAsrComplete(int id, String asr) throws RemoteException {
            Log.d(TAG, "AsrCallBack onAsrComplete id = " + id + ", asr = " + asr);
        }

        @Override
        public void onComplete(int id, String nlp, String action) throws RemoteException {
            Log.d(TAG, "AsrCallBack onComplete id = " + id + ", nlp = " + nlp + ", action = " + action);
        }

        @Override
        public void onCancel(int id) throws RemoteException {
            Log.d(TAG, "AsrCallBack onCancel id = " + id);
        }

        @Override
        public void onError(int id, int err) throws RemoteException {
            Log.d(TAG, "AsrCallBack onError id = " + id);
        }
    };

    private TtsCallBack mTtsCallBack = new TtsCallBack() {
        @Override
        public void onStart(int id) throws RemoteException {
            Log.d(TAG, "TtsCallBack onStart id = " + id);
        }

        @Override
        public void onVoicePlay(int id, String text) throws RemoteException {
            Log.d(TAG, "TtsCallBack onVoicePlay id = " + id);
        }

        @Override
        public void onCancel(int id) throws RemoteException {
            Log.d(TAG, "TtsCallBack onCancel id = " + id);
        }

        @Override
        public void onComplete(int id) throws RemoteException {
            Log.d(TAG, "TtsCallBack onComplete id = " + id);
        }

        @Override
        public void onError(int id, int err) throws RemoteException {
            Log.d(TAG, "TtsCallBack onError id = " + id);
        }
    };

    @Override
    protected void onDestroy() {
        if (mSpeechManager != null) {
            mSpeechManager.onDestroy();
            mSpeechManager = null;
        }

        super.onDestroy();
    }
}
