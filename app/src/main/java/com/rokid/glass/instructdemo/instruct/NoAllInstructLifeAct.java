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
 * 清除全部指令 Act
 * LifeCycle方式
 */
public class NoAllInstructLifeAct extends AppCompatActivity {

    private InstructLifeManager mLifeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_instruct);
        configInstruct();
    }

    public void configInstruct() {
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        // 清除全部系统指令
        mLifeManager.getInstructConfig().setIgnoreSystem(true);
    }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {
        @Override
        public boolean onInterceptCommand(String command) {
            return false;
        }

        @Override
        public void onTipsUiReady() {
            // 清除页面底部"显示帮助"浮层
            if (mLifeManager != null) {
                mLifeManager.hideTipsLayer();
            }
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };
}
