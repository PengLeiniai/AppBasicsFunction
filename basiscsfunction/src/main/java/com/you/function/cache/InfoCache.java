package com.you.function.cache;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.you.function.Function;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.ItemNotFoundException;

/**
 * Created by you on 2019-09-24
 */

public class InfoCache {

    private AppPreferences mAppPreferences;
    private static InfoCache mAccountUtil = null;

    public static InfoCache getInstance() {
        if (mAccountUtil == null) {
            mAccountUtil = new InfoCache();
        }
        return mAccountUtil;
    }

    public InfoCache() {
        mAppPreferences = new AppPreferences(Function.appLicationContext);
    }

    /**
     * 保存登录用户信息
     *
     * @param data String 需要保存的数据
     * @param key  String需要保存的数据
     */
    public void saveData(String key, String data) {
        mAppPreferences.put(key, data);
    }

    public String getCacheData(final String key) {
        try {
            return mAppPreferences.getString(key);
        } catch (ItemNotFoundException e) {
        }
        return "";
    }
}
