package com.ss.giphy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


/**
 * Created by Shubham
 */
public class GiphySharedPreferences {

    /* singleton reference to access the share persistence data  */
    private static GiphySharedPreferences mGiphySharedPreferences = null;
    private static SharedPreferences mSharedPreferences;
    private static Context mCtx;

    public GiphySharedPreferences(Context _ctx) {
        this.mCtx = _ctx;
    }

    public static GiphySharedPreferences getInstance() {
        if (mGiphySharedPreferences == null) {
            mGiphySharedPreferences = new GiphySharedPreferences(mCtx);
            if (mSharedPreferences == null) {
                mSharedPreferences = mCtx.getSharedPreferences(Consts.SHARED_PREF,Context.MODE_PRIVATE);
            }

        }
        return mGiphySharedPreferences;
    }

    private SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            GiphySharedPreferences.getInstance();
        }
        return mSharedPreferences;
    }


    public String getCount() {
        String count = getSharedPreferences().getString(Consts.TOTAL_COUNT, "");
        if (TextUtils.isEmpty(count)) {
            count = getSharedPreferences().getString(Consts.TOTAL_COUNT, "");
        }
        return count;
    }

    public void setCount(String count) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(Consts.TOTAL_COUNT, count);
        editor.apply();
    }

}