package com.gerenvip.banner;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author wangwei on 16/9/14
 *         wangwei@jiandaola.com
 */
public class LogUtil {

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message)) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message)) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message)) {
            Log.e(tag, message);
        }
    }

}
