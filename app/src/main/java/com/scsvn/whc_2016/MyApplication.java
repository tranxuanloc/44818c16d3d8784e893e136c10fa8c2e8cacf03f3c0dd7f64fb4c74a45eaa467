package com.scsvn.whc_2016;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by xuanloc on 11/9/2016.
 */
@ReportsCrashes(
        mailTo = "tranxuanloc.py@gmail.com",
        customReportContent = {ReportField.ANDROID_VERSION, ReportField.APP_VERSION_NAME,
                ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.STACK_TRACE}
)
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
