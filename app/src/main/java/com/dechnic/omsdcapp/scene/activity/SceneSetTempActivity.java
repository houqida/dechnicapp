package com.dechnic.omsdcapp.scene.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/4.
 */

public class SceneSetTempActivity extends BaseActivity {
    @Bind(R.id.modleTv)
    TextView modleTv;
    @Bind(R.id.modleRel)
    RelativeLayout modleRel;
    @Bind(R.id.gearTv)
    TextView gearTv;
    @Bind(R.id.gearRel)
    RelativeLayout gearRel;
    @Bind(R.id.wenduTv)
    TextView wenduTv;
    @Bind(R.id.wenduRel)
    RelativeLayout wenduRel;
    @Bind(R.id.backRelLay)
    RelativeLayout backRelLay;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.saveRel)
    RelativeLayout saveRel;

    private PopupWindow popupWindow;
    List<String> tempLoopViewList = new ArrayList<>();
    String temp;
    private int temperature;
    private View view;
    int[] location = new int[2];
    private View m_view;
    private PopupWindow m_popup;
    private View g_view;
    private PopupWindow g_popup;


    String pattern = "";
    String speed = "";
    String deviceId = "";
    Bundle bundle = new Bundle();

    String resourceFlag_pattern = "";
    String resourceFlag_speed = "";
    String resourceFlag_temp = "temperature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_set_temp);
        ButterKnife.bind(this);
        initViews();
        initPopupwindow();
        initModlePopupwindow();
        initGearPopupwindow();
    }

    private void initViews(){
        bundle = getIntent().getExtras();
        pattern = bundle.getString("pattern");
        speed = bundle.getString("speed");
        deviceId = bundle.getString("deviceId");
        temp = bundle.getString("temperature");

        if (pattern.equals("制热")) {
            resourceFlag_pattern = "pattern_hot";
        } else if (pattern.equals("制冷")) {
            resourceFlag_pattern = "pattern_cold";
        }

        if (speed.equals("低档")) {
            resourceFlag_speed = "speed_low";
        } else if (speed.equals("中档")) {
            resourceFlag_speed = "speed_middle";
        } else if (speed.equals("高档")) {
            resourceFlag_speed = "speed_high";
        } else if (speed.equals("自动")) {
            resourceFlag_speed = "speed_auto";
        }
        modleTv.setText(pattern);
        gearTv.setText(speed);
        wenduTv.setText(temp+"℃");
    }

    private void initModlePopupwindow() {
        m_view = LayoutInflater.from(this).inflate(R.layout.install_modle_popup, null);
        TextView cold = (TextView) m_view.findViewById(R.id.toCold);
        TextView hot = (TextView) m_view.findViewById(R.id.toHot);

        m_popup = new PopupWindow(m_view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        m_popup.setFocusable(true);
        m_popup.setOutsideTouchable(true);
        m_popup.setBackgroundDrawable(new BitmapDrawable());
        m_popup.setAnimationStyle(R.style.ins_popup_anim);

        cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modleTv.setText("制冷");
                pattern = "制冷";
                resourceFlag_pattern = "pattern_cold";
                m_popup.dismiss();
            }
        });

        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modleTv.setText("制热");
                pattern = "制热";
                resourceFlag_pattern = "pattern_hot";
                m_popup.dismiss();
            }
        });
    }

    private void initGearPopupwindow() {
        g_view = LayoutInflater.from(this).inflate(R.layout.install_gear_popup, null);
        TextView low = (TextView) g_view.findViewById(R.id.toLow);
        TextView middle = (TextView) g_view.findViewById(R.id.toMiddle);
        TextView high = (TextView) g_view.findViewById(R.id.toHigh);
        TextView auto = (TextView) g_view.findViewById(R.id.toAuto);

        g_popup = new PopupWindow(g_view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        g_popup.setFocusable(true);
        g_popup.setOutsideTouchable(true);
        g_popup.setBackgroundDrawable(new BitmapDrawable());
        g_popup.setAnimationStyle(R.style.ins_popup_anim);

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearTv.setText("低档");
                speed = "低档";
                resourceFlag_speed = "speed_low";
                g_popup.dismiss();
            }
        });

        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearTv.setText("中档");
                speed = "中档";
                resourceFlag_speed = "speed_middle";
                g_popup.dismiss();
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearTv.setText("高档");
                speed = "高档";
                resourceFlag_speed = "speed_high";
                g_popup.dismiss();
            }
        });

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearTv.setText("自动");
                speed = "自动";
                resourceFlag_speed = "speed_auto";
                g_popup.dismiss();
            }
        });
    }

    private void initPopupwindow() {
        view = LayoutInflater.from(this).inflate(R.layout.install_temp_popup, null);
        RelativeLayout cacleRel = (RelativeLayout) view.findViewById(R.id.cacleRel);
        RelativeLayout sureRel = (RelativeLayout) view.findViewById(R.id.sureRel);
        LoopView tempLoopView = (LoopView) view.findViewById(R.id.loopView);
        for (int i = 0; i < 41; i++) {
            tempLoopViewList.add(i + "");
        }
        tempLoopView.setItems(tempLoopViewList);

        if (!"".equals(temp) && temp != null) {
            temperature = Integer.valueOf(temp);
            tempLoopView.setInitPosition(temperature);
        }
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.ins_popup_anim);

        tempLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                temperature = index;
            }
        });

        cacleRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                wenduTv.setText(temp + "℃");
            }
        });
        sureRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                temp = temperature+"";
                wenduTv.setText(temp + "℃");
            }
        });
    }

    @OnClick({R.id.modleRel, R.id.gearRel, R.id.wenduRel,R.id.backRelLay, R.id.saveRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRelLay:
                Intent intent = new Intent();
                setResult(1,intent);
                finish();
                break;
            case R.id.saveRel:
                save();
                break;
            case R.id.modleRel:
                view.getLocationOnScreen(location);
                m_popup.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
            case R.id.gearRel:
                view.getLocationOnScreen(location);
                g_popup.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
            case R.id.wenduRel:
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
        }
    }

    private void save() {
        Intent intent = new Intent();
        intent.putExtra("pattern",pattern);
        intent.putExtra("speed",speed);
        intent.putExtra("deviceId",deviceId);
        intent.putExtra("temperature",temp);
        intent.putExtra("resourceFlag_pattern",resourceFlag_pattern);
        intent.putExtra("resourceFlag_speed",resourceFlag_speed);
        intent.putExtra("resourceFlag_temp",resourceFlag_temp);
        setResult(0,intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(1,intent);
        finish();
        super.onBackPressed();
    }
}
