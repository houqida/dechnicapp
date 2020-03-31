package com.dechnic.omsdcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * “问题反馈”的类
 */
public class AnswerActivity extends BaseActivity {

    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.submitRel)
    RelativeLayout submitRel;
    @Bind(R.id.answerET)
    EditText answerET;
    @Bind(R.id.emailEt)
    EditText emailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        answerET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("==count",count+"");
                if (count>150) {
                    Toast.makeText(AnswerActivity.this,"问题不能超过150字",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>25) {
                    Toast.makeText(AnswerActivity.this,"联系方式不能超过25字",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.backRel, R.id.submitRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                finish();
                break;
            case R.id.submitRel:
                submit();
                break;
        }
    }

    private void submit() {
        String question = answerET.getText().toString();
        String link = emailEt.getText().toString();

        if (question.isEmpty()) {
            Toast.makeText(this,"问题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (link.isEmpty()) {
            Toast.makeText(this,"联系方式不能为空不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        SuccinctProgress.showSuccinctProgress(this,"提交中...",SuccinctProgress.THEME_ARC,true,true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        }else {
            RequestParams params = new RequestParams(HttpURL.Url(AnswerActivity.this)+HttpURL.TICKINGMESSAGE);
            params.addHeader("access_token", AppUtils.getAccessToken(this));
            params.addQueryStringParameter("contact",question);
            params.addQueryStringParameter("content",link);
            params.setConnectTimeout(5000);
            params.setReadTimeout(5000);
            x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    SuccinctProgress.dismiss();
                    json(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Toast.makeText(AnswerActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onFinished() {
                    SuccinctProgress.dismiss();
                }
            });

        }



    }

    private void json(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.optString("result");
            if ("1".equals(code)) {
                Toast.makeText(this,"反馈提交成功",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AnswerActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);

            }else {
                Toast.makeText(this,code,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
