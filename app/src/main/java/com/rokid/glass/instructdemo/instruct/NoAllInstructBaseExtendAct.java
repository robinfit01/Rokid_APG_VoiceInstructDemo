package com.rokid.glass.instructdemo.instruct;

import android.os.Bundle;
import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instructdemo.R;

/**
 * 清除全部指令 Act
 * 继承或仿照InstructionActivity方式
 */
public class NoAllInstructBaseExtendAct extends InstructionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_instruct);
    }

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        // 设置ActionKey，需要保证不为空且唯一
        config.setActionKey(getClass().getName() + InstructConfig.ACTION_SUFFIX);
        // 清除全部系统指令
        config.setIgnoreSystem(true);
        return config;
    }

    @Override
    public void onInstrucUiReady() {
        super.onInstrucUiReady();
        // 清除页面底部"显示帮助"浮层
        if (mInstructionManager != null) {
            mInstructionManager.hideTipsLayer();
        }
    }

    /**
     * 是否拦截处理当前语音指令，拦截后之前配置的指令闭包不会被调用
     * （可以用来提前处理一些指令，然后返回false）
     * @param command
     * @return true：拦截事件 false：不进行拦截
     */
    @Override
    public boolean doReceiveCommand(String command) {
        return false;
    }
}
