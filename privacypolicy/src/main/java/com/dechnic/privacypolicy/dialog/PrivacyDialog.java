package com.dechnic.privacypolicy.dialog;

import android.app.Dialog;
import android.content.Context;

import com.dechnic.privacypolicy.R;

public class PrivacyDialog extends Dialog {

    public PrivacyDialog(Context context) {
        super(context, R.style.PrivacyThemeDialog);

        setContentView(R.layout.dialog_privacy);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
