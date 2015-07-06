package com.seguetech.zippy.logging;


import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.seguetech.zippy.BuildConfig;

import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class CrashlyticsTree extends Timber.DebugTree {

    private static CrashlyticsCore CORE;

    private static final int MAX_LOG_LENGTH = 4000;
    private static final int CALL_STACK_INDEX = 5;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    public CrashlyticsTree(Application application) {
        Fabric.with(application, new Crashlytics());
        CORE = Crashlytics.getInstance().core;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // only send items other than exceptions when in a debug build.
        if (BuildConfig.DEBUG) {
            if (message != null) {
                CORE.log(priority, tag, message);
                if (t != null) {
                    CORE.logException(t);
                }
            }
        } else {
            switch (priority) {
                case Log.INFO:
                case Log.DEBUG:
                    break;
                case Log.WARN:
                case Log.ERROR:
                case Log.ASSERT:
                    if (message != null) {
                        CORE.log(priority, tag, message);
                    }
                    if (t != null) {
                        CORE.logException(t);
                    }
            }
        }
    }


}