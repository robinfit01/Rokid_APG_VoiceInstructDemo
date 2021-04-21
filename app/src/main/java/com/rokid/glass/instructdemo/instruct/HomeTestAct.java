package com.rokid.glass.instructdemo.instruct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rokid.glass.instruct.InstructLifeManager;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instructdemo.R;
import com.rokid.glass.instructdemo.speech.SpeechTestAct;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 * Func: 主页Activity
 *
 * @author: liuweiming
 * @version: 1.0
 * Create Time: 2019-07-29
 */
public class HomeTestAct extends AppCompatActivity {

    private static final String TAG = "AudioAi " + HomeTestAct.class.getSimpleName();

    private int[] mImageRes = new int[]{
            R.drawable.big_buck_bunny,
            R.drawable.big_fish_image,
            R.drawable.fly_girl,
            R.drawable.music_girl
    };
    private String[] mVideoName = new String[]{
            "big buck bunny",
            "深蓝海洋",
            "漫画女孩",
            "青春纪念册"
    };
    private String[] mVideoRes = new String[]{
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "http://vjs.zencdn.net/v/oceans.mp4",
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
            "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
    };

    private InstructLifeManager mLifeManager;
    private ImageView mImageView;
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        configInstruct();

        mImageView = findViewById(R.id.show_image);
        mCurrentIndex = 0;
        mImageView.setImageResource(mImageRes[mCurrentIndex]);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo();
            }
        });
    }

    public void configInstruct() {
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        mLifeManager.addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("上一个", null))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "last one"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        doLast();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("下一个", null))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "next one"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        doNext();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("语音测试", "yu yin ce shi"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        if (act != null) {
                                            act.startActivity(new Intent(act, SpeechTestAct.class));
                                        }
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("隐藏帮助", "yin cang bang zhu"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        mLifeManager.hideHelpLayer();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("进入视频", null))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "open video"))
                                .setShowTips(true)
                                .setIgnoreHelp(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        openVideo();
                                    }
                                })
                );
    }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {

        /** 是否拦截处理当前语音指令，拦截后之前配置的指令闭包不会被调用
         * （可以用来提前处理一些指令，然后返回false）
         * @param command
         * @return true：拦截事件 false：不进行拦截
         */
        @Override
        public boolean onInterceptCommand(String command) {
            Log.d(TAG, "doReceiveCommand command = " + command);

            if ("返回".equals(command)) {
                return true;
            }
            return false;
        }

        @Override
        public void onTipsUiReady() {
            Log.d("AudioAi", "onTipsUiReady Call ");
            if (mLifeManager != null) {
                mLifeManager.setMenuShowing(false);
                mLifeManager.setLeftBackShowing(false);
            }
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KEYCODE_DPAD_LEFT:
                doLast();
                return true;
            case KEYCODE_DPAD_RIGHT:
                doNext();
                return true;
            case KEYCODE_ENTER:
            case KEYCODE_DPAD_CENTER:
                openVideo();
                return true;
            default:
                break;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void setCurrentSelect(final int current) {
        Log.d(TAG, "setCurrentSelect current = " + current);
        if (current != mCurrentIndex && current >= 0 && current < mImageRes.length) {
            mCurrentIndex = current;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mImageView != null) {
                        mImageView.setImageResource(mImageRes[mCurrentIndex]);
                    }
                }
            });
        }
    }

    private void doNext() {
        if (mCurrentIndex < (mImageRes.length - 1)) {
            setCurrentSelect(mCurrentIndex + 1);
        } else {
            setCurrentSelect(0);
        }
    }

    private void doLast() {
        if (mCurrentIndex > 0) {
            setCurrentSelect(mCurrentIndex - 1);
        } else {
            setCurrentSelect(mImageRes.length - 1);
        }
    }

    private void openVideo() {
        jumpToVideoAct(mVideoName[mCurrentIndex], mVideoRes[mCurrentIndex]);
    }

    private void jumpToVideoAct(@NonNull String name, @NonNull String url) {
        Intent intent = new Intent(this, ShowVideoAct.class);
        intent.putExtra(ShowVideoAct.PARAM_VIDEO_NAME, name);
        intent.putExtra(ShowVideoAct.PARAM_VIDEO_URL, url);
        startActivity(intent);
    }
}
