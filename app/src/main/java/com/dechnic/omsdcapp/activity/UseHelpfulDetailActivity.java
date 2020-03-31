package com.dechnic.omsdcapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UseHelpfulDetailActivity extends BaseActivity {

    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.titleTv)
    TextView titleTv;
    @Bind(R.id.contentTv)
    TextView contentTv;

    Bundle bundle;
    String title;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_helpful_detail);

        ButterKnife.bind(this);

        bundle = getIntent().getExtras();
        title = bundle.getString("title");
        content = bundle.getString("content");

        titleTv.setText(title);
        if (content!=null&&!"".equals(content)) {
            contentTv.setText(content);
        }

        backRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
