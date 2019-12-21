package com.rokid.glass.instructdemo.instruct;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.VoiceInstruction;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                                .setName("下一个")
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
                                .setName("进入视频")
                                .setShowTips(true)
                                .setIgnoreHelp(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        openVideo();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .setName("确认")
                                .setShowTips(true)
                                .setIgnoreHelp(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                openDialog();
                                            }
                                        });
                                    }
                                })
                );

        return config;
    }

    @Override
    public void onInstrucUiReady() {
        super.onInstrucUiReady();

        setMenuShowing(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KEYCODE_DPAD_LEFT:
                doLast();
//                restartVoiceServer(true, false);
                return true;
            case KEYCODE_DPAD_RIGHT:
                doNext();
//                restartVoiceServer(false, false);
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

    private void restartVoiceServer(boolean mustRestart, boolean configAllUseSolution) {
        VoiceInstruction.restartVoiceServer(HomeTestAct.this, getPackageName(), mustRestart, configAllUseSolution,false, mInstructionManager);
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

        if ("测试".equals(command)) {
            return true;
        }
        return false;
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

    private void openDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(HomeTestAct.this) {
                    @Override
                    public AlertDialog show() {
                        AlertDialog dialog = super.show();
                        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                        Log.e("yuyi", "AlertDialog window type is " + attrs.type);
                        return dialog;
                    }
                };
        normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}
