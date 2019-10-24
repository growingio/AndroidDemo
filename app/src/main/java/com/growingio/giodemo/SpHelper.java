package com.growingio.giodemo;

/**
 * @Classname SpHelper
 * @Description TODO
 * @Date 2019-10-22 21:16
 * @Created by huangrichao
 */
import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {
    private static final String SP_NAME = "touch_demo_data";
    private static final String TOUCH_STATE = "touch_state";
    private static final String TOUCH_USER = "touch_user";
    private static final String TOUCH_HOST = "touch_host";
    private static final String IMAGE_OPEN_CNT = "image_open_cnt";
    private static final String POP_WINDOW_SOLO_IMP = "pop_window_solo_imp";
    private static final String POP_WINDOW_SOLO_CLOSE = "pop_window_solo_close";
    private static final String POP_WINDOW_SOLO_CLICK = "pop_window_solo_click";
    private static final String LATEST_HTML_SOURCE = "latest_html_source";

    private final SharedPreferences mSharedPreferences;

    private String defaultHtml = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\n" +
            "<head>\n" +
            "    <title>暂无内容</title>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "    暂无内容\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    public SpHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void saveGTouchEnableState(boolean enable) {
        mSharedPreferences.edit().putBoolean(TOUCH_STATE, enable).apply();
    }

    public boolean getGTouchEnableState() {
        return mSharedPreferences.getBoolean(TOUCH_STATE, false);
    }

    public void saveGTouchUser(String user) {
        mSharedPreferences.edit().putString(TOUCH_USER, user).apply();
    }

    public String getGTouchUser() {
        return mSharedPreferences.getString(TOUCH_USER, "");
    }


    public void saveGtouchHost(String host) {
        mSharedPreferences.edit().putString(TOUCH_HOST, host).commit();
    }

    public String getTouchHost() {
        return mSharedPreferences.getString(TOUCH_HOST, "https://messages.growingio.com");
    }

    public void saveImgOpenCnt(int cnt) {
        mSharedPreferences.edit().putInt(IMAGE_OPEN_CNT, cnt).commit();
    }

    public int getImgOpenCnt() {
        return mSharedPreferences.getInt(IMAGE_OPEN_CNT, 0);
    }

    public void savePopWindowSoloImp(int cnt) {
        mSharedPreferences.edit().putInt(POP_WINDOW_SOLO_IMP, cnt).commit();
    }

    public int getPopWindowSoloImp() {
        return mSharedPreferences.getInt(POP_WINDOW_SOLO_IMP, 0);
    }

    public void savePopWindowSoloClose(int cnt) {
        mSharedPreferences.edit().putInt(POP_WINDOW_SOLO_CLOSE, cnt).commit();
    }

    public int getPopWindowSoloClose() {
        return mSharedPreferences.getInt(POP_WINDOW_SOLO_CLOSE, 0);
    }

    public void savePopWindowSoloClick(int cnt) {
        mSharedPreferences.edit().putInt(POP_WINDOW_SOLO_CLICK, cnt).commit();
    }

    public int getPopWindowSoloClick() {
        return mSharedPreferences.getInt(POP_WINDOW_SOLO_CLICK, 0);
    }

    public void saveLatestHtmlSource(String html) {
        mSharedPreferences.edit().putString(LATEST_HTML_SOURCE, html).commit();
    }

    public String getLatestHtmlSource() {
        return mSharedPreferences.getString(LATEST_HTML_SOURCE, defaultHtml);
    }
}