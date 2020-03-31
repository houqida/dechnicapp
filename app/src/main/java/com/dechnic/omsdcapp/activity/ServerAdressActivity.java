package com.dechnic.omsdcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 填写服务器地址 类
 */
public class ServerAdressActivity extends BaseActivity {

    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.sureRel)
    RelativeLayout sureRel;
    @Bind(R.id.setAdressTv)
    EditText setAdressTv;
    String adress  = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_adress);
        ButterKnife.bind(this);
        setAdressTv.setText(AppUtils.getServerAdress(this));

    }

    @OnClick({R.id.backRel, R.id.sureRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                Intent intent0 = new Intent(this, UserLoginActivity.class);
                startActivity(intent0);
                finish();
                break;
            case R.id.sureRel:
                if (setAdressTv.getText().toString() != null && !"".equals(setAdressTv.getText().toString())) {
                    SuccinctProgress.showSuccinctProgress(ServerAdressActivity.this, "正在保存...", SuccinctProgress.THEME_ARC, true, true);
                    if (!setAdressTv.getText().toString().contains(":")) {
                        AppUtils.setServerAdress(ServerAdressActivity.this, setAdressTv.getText().toString()+":8081");
                    }else {
                        AppUtils.setServerAdress(ServerAdressActivity.this, setAdressTv.getText().toString());
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ServerAdressActivity.this, UserLoginActivity.class);
                            SuccinctProgress.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }, 500);

                } else {
                    Toast.makeText(this, "请填写IP地址", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent0 = new Intent(this, UserLoginActivity.class);
        startActivity(intent0);
        finish();
        super.onBackPressed();
    }
}
