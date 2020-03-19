package com.rokid.glass.instructdemo.application;

import android.app.Application;

import com.rokid.glass.instruct.VoiceInstruction;


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
    }
}
