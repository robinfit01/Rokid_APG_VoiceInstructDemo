package com.rokid.glass.instructdemo.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rokid.glass.instruct.Integrate.InstructionActivity;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instructdemo.R;
import com.rokid.glass.instructdemo.instruct.HomeTestAct;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Func: 测试应用启动页面
 *
 * @author: liuweiming
 * @version: 1.0
 * Create Time: 2019-07-29
 */
public class OrderListAct extends InstructionActivity {

    private static final String TAG = "AudioAi " + OrderListAct.class.getSimpleName();

    private static final String ORDER_TEST_DIR = "glassOrderTest";
    private static final String FILE_SUFFIX = ".txt";

    private LinearLayout mLayoutView;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mLayoutView = findViewById(R.id.order_list);
        findViewById(R.id.jump_demo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderListAct.this, HomeTestAct.class);
                startActivity(i);
            }
        });

        initLinearBtn();

        initAutoTestBtn();
    }

    public void initAutoTestBtn() {
        findViewById(R.id.demo_btn_002).setOnClickListener(clickListener);
        findViewById(R.id.demo_btn_003).setOnClickListener(clickListener);
        findViewById(R.id.demo_btn_004).setOnClickListener(clickListener);
        findViewById(R.id.demo_btn_005).setOnClickListener(clickListener);
        findViewById(R.id.demo_btn_006).setOnClickListener(clickListener);

        findViewById(R.id.demo_btn_003).setVisibility(View.GONE);
        findViewById(R.id.demo_btn_004).setVisibility(View.GONE);
        findViewById(R.id.demo_btn_005).setEnabled(false);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                Toast.makeText(v.getContext(), ((Button) v).getText() + " Clicked", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        config.setActionKey(OrderListAct.class.getName() + InstructConfig.ACTION_SUFFIX);
        config.setIgnoreSystem(false);
        return config;
    }

    @Override
    public void onInstrucUiReady() {
        super.onInstrucUiReady();
        if (mInstructionManager != null) {
            mInstructionManager.hideTipsLayer();
        }
        setMenuShowing(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void initLinearBtn() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ORDER_TEST_DIR + File.separator;
        File dirFile = new File(filePath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            String[] fileNames = dirFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Log.d("ordertest", "检测：" + name);
                    if (name != null) {
                        if (name.toLowerCase().endsWith(FILE_SUFFIX)) {
                            return true;
                        }
                    }
                    return false;
                }
            });

            if (fileNames != null) {
                for (String fileName: fileNames) {
                    Log.d("ordertest", "文件结果：" + fileName);
                    addBtnToLinear(fileName, filePath);
                }
            }
        }
    }

    private void addBtnToLinear(String fileName, String filePath) {
        if (mLayoutView != null) {
            Button button = new Button(this);
            button.setText(fileName);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            button.setPadding(10, 10, 10, 10);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button.setTextColor(getColor(R.color.white));
            }
            button.setBackgroundResource(R.drawable.btn_bg);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 5);

            button.setTag(filePath + File.separator + fileName);

            mLayoutView.addView(button, params);

            button.setOnClickListener(mItemClick);
        }
    }

    private View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v != null) {
                String fileName = (String) v.getTag();
                if (fileName != null ) {
                    Intent i = new Intent(OrderListAct.this, OrderItemAct.class);
                    i.putExtra(OrderItemAct.PARAM_ITEM_FILE, fileName);
                    startActivity(i);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInstructionManager != null) {
            mInstructionManager.hideTipsLayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
