package com.dechnic.omsdcapp.base;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.dechnic.omsdcapp.commons.AppManager;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Base2Activity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
