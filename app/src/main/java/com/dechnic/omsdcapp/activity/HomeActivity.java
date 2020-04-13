package com.dechnic.omsdcapp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppManager;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.entity.BottomBarEntity;
import com.dechnic.omsdcapp.entity.UserInfo;
import com.dechnic.omsdcapp.fragment.CentralAirFragment;
import com.dechnic.omsdcapp.fragment.DeviceFragment;
import com.dechnic.omsdcapp.fragment.ElectricFragment;
import com.dechnic.omsdcapp.fragment.EnergyFragment;
import com.dechnic.omsdcapp.fragment.SceneFragment;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.BottomTabBar;
import com.tencent.bugly.beta.Beta;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页，主activity
 */

public class HomeActivity extends BaseActivity implements BottomTabBar.OnSelectListener {

    @Bind(R.id.personalMessLay)
    LinearLayout personalMessLay;
    @Bind(R.id.myMessageRel)
    RelativeLayout myMessageRel;
    @Bind(R.id.passwordRel)
    RelativeLayout passwordRel;
    @Bind(R.id.shareRel)
    RelativeLayout shareRel;
    @Bind(R.id.helpRel)
    RelativeLayout helpRel;
    @Bind(R.id.answerRel)
    RelativeLayout answerRel;
    @Bind(R.id.updateRel)
    RelativeLayout updateRel;
    @Bind(R.id.aboutRel)
    RelativeLayout aboutRel;
    @Bind(R.id.exitTv)
    TextView exitTv;
    @Bind(R.id.exitRel)
    RelativeLayout exitRel;
    @Bind(R.id.personalIv)
    ImageView personalIv;
    @Bind(R.id.personalNameTv)
    TextView personalNameTv;
    @Bind(R.id.titile_titleTv)
    TextView titileTitleTv;
    @Bind(R.id.messageNumTv)
    TextView messageNumTv;
    @Bind(R.id.messageNumRel)
    RelativeLayout messageNumRel;


    private ImageView openDrawerLayout;
    private DrawerLayout drawer_layout;

    private RelativeLayout drawers_layout;

    private BottomTabBar tb;
    private DeviceFragment deviceFragment;
    private ElectricFragment electricFragment;
    private EnergyFragment energyFragment;
    private SceneFragment sceneFragment;
    private CentralAirFragment centralAirFragment;
    private List<BottomBarEntity> bars;

    private FragmentManager manager;

    MyMessageActivity messageActivity;
    AlertPasswordActivity passwordActivity;
    AboutActivity aboutActivity;
    AnswerActivity answerActivity;
    UseHelpfulActivity helpfulActivity;
    UserLoginActivity loginActivity;

    Bundle bundle = null;

    private PopupWindow popupWindow;
    int[] location = new int[2];
    private RelativeLayout share_wechat;
    private RelativeLayout share_wefriend;
    private RelativeLayout share_wb;
    private RelativeLayout share_qq;
    private RelativeLayout share_qzon;
    private RelativeLayout share_layout;
    private Button button;

    UserInfo userInfo;
    int tag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userInfo = AppUtils.getUserInfo(this);
        ButterKnife.bind(this);
        titileTitleTv.setText(R.string.actionbar_title);
//        Beta.checkUpgrade();
        bundle = getIntent().getExtras();
        openDrawerLayout = (ImageView) findViewById(R.id.openDrawerLayout);
        initDrawer();
        initViews();
        initPopupWindow();
    }

    //侧边栏显示
    private void initDrawer() {
        getMessageNumber();
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        drawers_layout = (RelativeLayout) findViewById(R.id.drawers_layout);
        DrawerLayout.LayoutParams l = (DrawerLayout.LayoutParams) drawers_layout.getLayoutParams();
        l.width = (d.getWidth() / 5) * 4;
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openDrawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });
        personalNameTv.setText(userInfo.getUserName());
    }


    private void getMessageNumber() {
        tag = 1;
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.UNREADMES);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("===unread", result);
                int nums = Integer.valueOf(result);
                if (nums == 0) {
                    messageNumRel.setVisibility(View.GONE);
                } else if (0 < nums && nums < 100) {
                    messageNumRel.setVisibility(View.VISIBLE);
                    messageNumTv.setText(result);
                } else if (nums >= 100) {
                    messageNumRel.setVisibility(View.VISIBLE);
                    messageNumTv.setText("99+");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("=====exmessage", ex.getMessage());
                Toast.makeText(HomeActivity.this, "服务器连接失败，请检查IP地址并重新登录", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeActivity.this, ServerAdressActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initViews() {
        manager = getSupportFragmentManager();
        tb = (BottomTabBar) findViewById(R.id.tb);
        tb.setManager(manager);
        tb.setOnSelectListener(this);
        bars = new ArrayList<>();
        bars.add(new BottomBarEntity("开关控制", R.mipmap.dechnic_press_icon, R.mipmap.dechnic_norpress_icon));
        bars.add(new BottomBarEntity("应用场景", R.mipmap.changjiang_press, R.mipmap.changjing_normal));
        bars.add(new BottomBarEntity("设备电量", R.mipmap.dianliang_press, R.mipmap.dianliang_normal));
        bars.add(new BottomBarEntity("节能监测", R.mipmap.nenghao_press, R.mipmap.nenghao_normal));
        bars.add(new BottomBarEntity("中央空调", R.mipmap.dechnic_press_icon, R.mipmap.dechnic_norpress_icon));
        tb.setBars(bars);
    }


    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_appshare, null);
        share_layout = (RelativeLayout) view.findViewById(R.id.share_layout);
        share_wechat = (RelativeLayout) view.findViewById(R.id.share_wechat_Rel);
        share_wefriend = (RelativeLayout) view.findViewById(R.id.share_wefriend_Rel);
        share_wb = (RelativeLayout) view.findViewById(R.id.share_wb_Rel);
        share_qq = (RelativeLayout) view.findViewById(R.id.share_qq_Rel);
        share_qzon = (RelativeLayout) view.findViewById(R.id.share_qzon_Rel);
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        popupWindow = new PopupWindow(view, (d.getWidth() / 5) * 4, DrawerLayout.LayoutParams.MATCH_PARENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.ins_popup_anim);

        button = (Button) view.findViewById(R.id.cacle_Btn);
        setListener();


    }

    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                if (deviceFragment != null) {
                    deviceFragment.onDestroyView();
                }
                deviceFragment = new DeviceFragment();
                titileTitleTv.setText(R.string.actionbar_title);
                tb.switchContent(deviceFragment);
                break;
            case 1:
//                if (sceneFragment == null) {
//                    sceneFragment = new SceneFragment();
//                }
                sceneFragment = new SceneFragment();
                titileTitleTv.setText(R.string.actionbar_title_scan);
                tb.switchContent(sceneFragment);
                break;
            case 2:
//                if (electricFragment == null) {
//                    electricFragment = new ElectricFragment();
//                }
                electricFragment = new ElectricFragment();
                titileTitleTv.setText(R.string.actionbar_title_elc);
                tb.switchContent(electricFragment);
                break;
            case 3:
//                if (energyFragment == null) {
//                    energyFragment = new EnergyFragment();
//                }
                energyFragment = new EnergyFragment();
                titileTitleTv.setText(R.string.actionbar_title_en);
                tb.switchContent(energyFragment);
                break;
            case 4://新增中央空调 add by houqida 20191231
                centralAirFragment = new CentralAirFragment();
                titileTitleTv.setText(R.string.actionbar_title_central_air);
                tb.switchContent(centralAirFragment);
                break;
            default:
                break;
        }

    }

    private long endTime;
    int twice = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();// 从1970年开始到现在过了多少毫秒
            if (currentTime - endTime > 2000) {
                Toast.makeText(this, "再按一次将退出 "+ Utils.getAppName(this), Toast.LENGTH_SHORT).show();
                endTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
                finish();
            }
        }
        return false;

    }

    @OnClick({R.id.personalMessLay, R.id.myMessageRel, R.id.passwordRel, R.id.shareRel,
            R.id.helpRel, R.id.answerRel, R.id.updateRel, R.id.aboutRel, R.id.exitRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personalMessLay:
                break;
            case R.id.myMessageRel://进入信息中心
                messageNumRel.setVisibility(View.GONE);
                goActivity(1);
                break;
            case R.id.passwordRel://进入修改密码界面
                goActivity(2);
                break;
            case R.id.shareRel:
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
                break;
            case R.id.helpRel://进入使用帮助界面
                goActivity(3);
                break;
            case R.id.answerRel://进入问题反馈界面
                goActivity(4);
                break;
            case R.id.updateRel://软件更新
                Beta.checkUpgrade();
                break;
            case R.id.aboutRel://关于
                goActivity(5);
                break;
            case R.id.exitRel:
                if (loginActivity == null) {
                    loginActivity = new UserLoginActivity();
                }
                //清除缓存里的服务器地址
                AppUtils.setServerAdress(getApplicationContext(),"");
                Intent intent = new Intent(this, loginActivity.getClass());
                startActivity(intent);
                finish();
                break;
        }
    }

    private void goActivity(int i) {
        Intent intent = null;
        switch (i) {
            case 1:
                if (messageActivity == null) {
                    messageActivity = new MyMessageActivity();
                }
                intent = new Intent(this, messageActivity.getClass());
                break;
            case 2:
                if (passwordActivity == null) {
                    passwordActivity = new AlertPasswordActivity();
                }
                intent = new Intent(this, passwordActivity.getClass());
                break;
            case 3:
                if (helpfulActivity == null) {
                    helpfulActivity = new UseHelpfulActivity();
                }
                intent = new Intent(this, helpfulActivity.getClass());
                break;
            case 4:
                if (answerActivity == null) {
                    answerActivity = new AnswerActivity();
                }
                intent = new Intent(this, answerActivity.getClass());
                break;
            case 5:
                if (aboutActivity == null) {
                    aboutActivity = new AboutActivity();
                }
                intent = new Intent(this, aboutActivity.getClass());
                break;

            default:
                break;
        }
        startActivity(intent);
    }

    private void setListener() {
        final UMWeb  web = new UMWeb(HttpURL.Url(this)+HttpURL.SHAREAPP);
        web.setTitle("公共建筑节能神器一枚，速速围观！");//标题
        web.setDescription("公共建筑能源浪费严重？下班忘记关空调？担心饮水机晚上反复加热上班第一杯水不健康？这些问题德诚能源管家都能帮您解决。");//描述

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "微信好友分享", Toast.LENGTH_SHORT).show();
                new ShareAction(HomeActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
                popupWindow.dismiss();
            }
        });
        share_wefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "朋友圈分享", Toast.LENGTH_SHORT).show();
                new ShareAction(HomeActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
                popupWindow.dismiss();
            }
        });
        share_wb.setOnClickListener(new View.OnClickListener() {//取消该分享
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "新浪微博分享", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(HomeActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
                popupWindow.dismiss();
            }
        });

        share_qzon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(HomeActivity.this).setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener)
                        .withMedia(web)
                        .share();
                popupWindow.dismiss();
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(HomeActivity.this, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(HomeActivity.this,share_media + " 分享失败", Toast.LENGTH_SHORT).show();
            if(throwable!=null){
                Log.d("throw","throw:"+throwable.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(HomeActivity.this,"取消"+share_media+"分享", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
