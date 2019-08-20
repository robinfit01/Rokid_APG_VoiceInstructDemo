package com.rokid.glass.instructdemo.instruct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instructdemo.R;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 * Func:
 *
 * @author: liuweiming
 * @version: 1.0
 * Create Time: 2019-07-29
 */
public class HomeTestAct extends InstructionActivity {

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

    private ImageView mImageView;
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_test);

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

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        config.setActionKey(HomeTestAct.class.getName() + InstructConfig.ACTION_SUFFIX)
                .addInstructEntity(
                        new InstructEntity()
                                .setName("上一个")
                                .setHelpInfo("显示上一个视频图片")
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        doLast();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .setName("下一个")
                                .setHelpInfo("显示下一个视频图片")
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        doNext();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .setName("进入视频")
                                .setHelpInfo("进入当前视频详情页面")
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        openVideo();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .setName("测试")
                                .setHelpInfo("测试一下当前语音")
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        openVideo();
                                    }
                                })
                );

        return config;
    }

    @Override
    protected void onDestroy() {
        clearWtWords();
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

    /**
     * 是否拦截处理当前语音指令，拦截后之前配置的指令闭包不会被调用
     * （可以用来提前处理一些指令，然后返回false）
     * @param command
     * @return true：拦截事件 false：不进行拦截
     */
    @Override
    public boolean doReceiveCommand(String command) {
        Log.d(TAG, "doReceiveCommand command = " + command);

        showCmdToast(command);
        if ("测试".equals(command)) {
            return true;
        }
        return false;
    }

    private void showCmdToast(final String cmd) {
        setCurrentInstruction(cmd);
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
