package com.dechnic.omsdcapp.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.dechnic.omsdcapp.R;


/**
 * Created by Administrator on 2017/4/11.
 */

public class CustomDialog extends ProgressDialog {
    public CustomDialog(Context context) {
        super(context);
        init(getContext());
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        init(getContext());
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }
}
