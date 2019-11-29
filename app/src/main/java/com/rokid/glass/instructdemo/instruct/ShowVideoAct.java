package com.rokid.glass.instructdemo.instruct;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instruct.type.NumberTypeControler;
import com.rokid.glass.instructdemo.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class ShowVideoAct extends InstructionActivity {

    private static final String TAG = ShowVideoAct.class.getSimpleName();

    public static final String PARAM_VIDEO_NAME = "video_name";
    public static final String PARAM_VIDEO_URL = "video_url";

    private JzvdStd mJzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String url = getIntent().getStringExtra(PARAM_VIDEO_URL);
        String name = getIntent().getStringExtra(PARAM_VIDEO_NAME);

        mJzvdStd = (JzvdStd) findViewById(R.id.jz_video);
        mJzvdStd.setUp(url, name);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        config.setActionKey(ShowVideoAct.class.getName() + InstructConfig.ACTION_SUFFIX)
                .addInstructEntity(
                        new InstructEntity()
                                .setName("播放")
                                .setPinYin("bo fang")
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        play();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .setName("暂停")
                                .setPinYin("zan ting")
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        pause();
                                    }
                                })
                ).addInstructList(NumberTypeControler.doTypeControl("第", 3, 20, "页", "第3/4.../19/20页", new NumberTypeControler.NumberTypeCallBack() {
            @Override
            public void onInstructReceive(Activity act, String key, int number, InstructEntity instruct) {
                Log.d(TAG, "AudioAi Number onInstructReceive command = " + key + ", number = " + number);
            }
        }));
        return config;
    }

    @Override
    public boolean doReceiveCommand(String command) {
        Log.d(TAG, "doReceiveCommand command = " + command);
        if ("返回".equals(command)) {
            exit();
        }
        return false;
    }

    private void play() {
        if (mJzvdStd != null && mJzvdStd.startButton != null) {
            if (mJzvdStd.state != JzvdStd.STATE_PLAYING) {
                mJzvdStd.startButton.performClick();
            }
        }
    }

    private void pause() {
        if (mJzvdStd != null && mJzvdStd.startButton != null) {
            if (mJzvdStd.state == JzvdStd.STATE_PLAYING) {
                mJzvdStd.startButton.performClick();
            }
        }
    }

    private void exit() {
        if (mJzvdStd != null) {
            mJzvdStd.changeUiToComplete();
        }
    }

}
