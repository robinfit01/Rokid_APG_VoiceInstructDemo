package com.rokid.glass.instructdemo.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instructdemo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Func: 测试应用指令测试页面
 *
 * @author: liuweiming
 * @version: 1.0
 * Create Time: 2019-07-29
 */
public class OrderItemAct extends InstructionActivity {

    private static final String TAG = "AudioAi " + OrderItemAct.class.getSimpleName();
    public static final String PARAM_ITEM_FILE = "item_file";

    private TextView mInfoView;
    private List<Pair<String, String>> mOrderList;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mInfoView = findViewById(R.id.show_text);
        mInfoView.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (mOrderList != null && mOrderList.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("设置测试指令：[" );
            for (Pair<String, String> pair : mOrderList) {
                if (TextUtils.isEmpty(pair.second)) {
                    buffer.append(pair.first + ", " );
                } else {
                    buffer.append("{" + pair.first + ", " + pair.second + "}, " );
                }
            }
            buffer.append("]\n\n\n"+ "开始测试：\n");
            mInfoView.setText(buffer.toString());
        } else {
            mInfoView.setText("无设置测试指令\n\n\n" + "开始测试：\n");
        }
    }

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        config.setActionKey(OrderItemAct.class.getName() + InstructConfig.ACTION_SUFFIX);
        config.setIgnoreSystem(true);
        mOrderList = getOrderByFile();
        String language = Locale.getDefault().getLanguage();
        if (mOrderList != null) {
            for (Pair<String, String> order: mOrderList) {
                if (!TextUtils.isEmpty(order.first)) {
                    EntityKey key = "en".equals(language) ? new EntityKey(EntityKey.Language.en, order.first) : new EntityKey(order.first, order.second);
                    config.addInstructEntity(
                            new InstructEntity()
                                    .addEntityKey(key)
                                    .setCallback(new IInstructReceiver() {
                                        @Override
                                        public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                            showOrder(key);
                                        }
                                    })
                    );
                }
            }
        }

        return config;
    }

    @Override
    public void onInstrucUiReady() {
        super.onInstrucUiReady();
        if (mInstructionManager != null) {
            mInstructionManager.hideTipsLayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
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
        return false;
    }

    private List<Pair<String, String>> getOrderByFile(){
        String fileName = getIntent().getStringExtra(PARAM_ITEM_FILE);
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                final List<Pair<String, String>> tempList = new ArrayList<>();
                BufferedReader br = null;
                String readLine = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    while ((readLine = br.readLine()) != null) {
                        String[] outArray = readLine.split(",");
                        if (outArray.length == 2) {
                            tempList.add(new Pair<>(outArray[0], outArray[1]));
                            Log.d(TAG, "getOrderByFile name = " + outArray[0] + ", pinyin = " + outArray[1]);
                        } else {
                            tempList.add(new Pair<>(readLine, null));
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return tempList;
            }
        }

        return null;
    }

    private void showOrder(String order) {
        if (order != null && mInfoView != null) {
            Log.d(TAG,"Order_Active show : " + order);
            String info = getTimeText() + ", 激活：" + order + "\n";
            mInfoView.append(info);
            scrollTextEndLine();
        }
    }

    private String getTimeText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private void scrollTextEndLine() {
        if (mInfoView != null) {
            int scrollAmount = mInfoView.getLayout().getLineTop(mInfoView.getLineCount()) - mInfoView.getHeight();
            if (scrollAmount > 0) {
                mInfoView.scrollTo(0, scrollAmount);
            } else {
                mInfoView.scrollTo(0, 0);
            }
        }
    }

}
