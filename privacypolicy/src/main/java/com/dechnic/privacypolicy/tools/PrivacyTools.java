package com.dechnic.privacypolicy.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dechnic.privacypolicy.R;
import com.dechnic.privacypolicy.activity.PrivacyPolicyActivity;
import com.dechnic.privacypolicy.activity.TermsActivity;
import com.dechnic.privacypolicy.dialog.PrivacyDialog;
import com.dechnic.privacypolicy.utils.AppUtils;
import com.dechnic.privacypolicy.utils.SPUtil;

/**
 * Time:2020/4/20
 * Author:houqida
 * Description:
 */
public class PrivacyTools {
    private String SP_PRIVACY = "sp_privacy";
    private String SP_VERSION_CODE = "sp_version_code";
    private boolean isCheckPrivacy = false;
    private long versionCode;
    private long currentVersionCode;
    private IPrivacyPolicyListener privacyPolicyListener = null;
    private static final PrivacyTools privacyToolsInstance = new PrivacyTools();
    private PrivacyTools() {

    }
    public static PrivacyTools getInstance(){
        return privacyToolsInstance;
    }
    /**
     * 显示隐私政策或跳转到其他界面
     */
    public  void  check(Activity activity,IPrivacyPolicyListener  privacyPolicyListener) {
        this.privacyPolicyListener = privacyPolicyListener;
        //先判断是否显示了隐私政策
        currentVersionCode = AppUtils.getAppVersionCode(activity);
        versionCode = (long) SPUtil.get(activity, SP_VERSION_CODE, 0L);
        isCheckPrivacy = (boolean) SPUtil.get(activity, SP_PRIVACY, false);

        if (!isCheckPrivacy || versionCode != currentVersionCode) {
            showPrivacy(activity);
        } else {
            privacyPolicyListener.privacyCallBack();
        }
    }
    /**
     * 显示用户协议和隐私政策
     */
    private void showPrivacy(final Activity activity) {

        final PrivacyDialog dialog = new PrivacyDialog(activity);
        TextView tv_privacy_tips = dialog.findViewById(R.id.tv_privacy_tips);
        TextView btn_exit = dialog.findViewById(R.id.btn_exit);
        TextView btn_enter = dialog.findViewById(R.id.btn_enter);
        dialog.show();

        String string = activity.getResources().getString(R.string.privacy_tips);
        String key1 = activity.getResources().getString(R.string.privacy_tips_key1);
        String key2 = activity.getResources().getString(R.string.privacy_tips_key2);
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(activity.getResources().getColor(R.color.blue));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(activity.getResources().getColor(R.color.blue));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(18, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(18, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TermsActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PrivacyPolicyActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击后的颜色为透明，否则会一直出现高亮
        tv_privacy_tips.setHighlightColor(Color.TRANSPARENT);
        //开始响应点击事件
        tv_privacy_tips.setMovementMethod(LinkMovementMethod.getInstance());

        tv_privacy_tips.setText(spannedString);

        //设置弹框宽度占屏幕的80%
        WindowManager m = activity.getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (defaultDisplay.getWidth() * 0.80);
        dialog.getWindow().setAttributes(params);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SPUtil.put(activity, SP_VERSION_CODE, currentVersionCode);
                SPUtil.put(activity, SP_PRIVACY, false);
                activity.finish();
            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SPUtil.put(activity, SP_VERSION_CODE, currentVersionCode);
                SPUtil.put(activity, SP_PRIVACY, true);
//                Toast.makeText(activity, getString(R.string.confirmed), Toast.LENGTH_SHORT).show();
                privacyPolicyListener.privacyCallBack();
            }
        });

    }

    public interface IPrivacyPolicyListener{
         void privacyCallBack();
    };
}
