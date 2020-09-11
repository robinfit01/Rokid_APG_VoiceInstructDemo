package com.rokid.glass.instructdemo.instruct;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.rokid.glass.instruct.InstructLifeManager;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instruct.type.NumberKey;
import com.rokid.glass.instruct.type.NumberTypeControler;
import com.rokid.glass.instructdemo.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Video InstructLifeManager 方式测试
 */
public class ShowVideoAct extends AppCompatActivity {

    private static final String TAG = ShowVideoAct.class.getSimpleName();

    public static final String PARAM_VIDEO_NAME = "video_name";
    public static final String PARAM_VIDEO_URL = "video_url";

    private InstructLifeManager mLifeManager;

    private JzvdStd mJzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        configInstruct();

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

    public void configInstruct() {
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        mLifeManager.addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("播放", "bo fang"))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "play video"))
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
                                .addEntityKey(new EntityKey("暂停", "zan ting"))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "stop video"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        pause();
                                    }
                                })
                )
                .addInstructList(NumberTypeControler.doTypeControl(3, 20,
                        new NumberTypeControler.NumberTypeCallBack() {
                            @Override
                            public void onInstructReceive(Activity act, String key, int number, InstructEntity instruct) {
                                Log.d(TAG, "AudioAi Number onInstructReceive command = " + key + ", number = " + number);
                            }
                        },
                        new NumberKey(EntityKey.Language.zh, "第", "页", "可以说第3/4.../20页"),
                        new NumberKey(EntityKey.Language.en, "the", "page", "the 3/4.../20 page")
                        )
                );
    }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {
        @Override
        public boolean onInterceptCommand(String command) {
            Log.d(TAG, "doReceiveCommand command = " + command);
            if ("返回".equals(command)) {
                exit();
            }
            return false;
        }

        @Override
        public void onTipsUiReady() {
            Log.d("AudioAi", "onTipsUiReady Call ");
            if (mLifeManager != null) {
                mLifeManager.setMenuShowing(false);
                mLifeManager.setLeftBackShowing(true);
            }
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };

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
