package com.rokid.glass.instructdemo.application;

import android.app.Activity;
import android.app.Application;

import com.rokid.glass.instruct.VoiceInstruction;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;


/**
 * Created by k.liang on 2018/6/22 16:36
 */
public class InstructionApplication extends Application {

    /**
     * 设置ManagerBasicSkill的context
     */
    @Override
    public void onCreate() {
        super.onCreate();

        VoiceInstruction.init(this);
        VoiceInstruction.getInstance().addGlobalInstruct(
                new InstructEntity()
                        .setGlobal(true)
                        .setName("返回")
                        .setPinYin("fan hui")
                        .setMargins(100.0f)
                        .setHelpInfo("退出当前页面")
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                try {
                                    if (act != null) {
                                        act.finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
        );
    }
}
