package halamish.reem.couplefun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import lombok.Getter;

/**
 * Created by Re'em on 2/4/2017.
 *
 * is used to communicate with the shared preferences
 */

public class LocalStore {
    private static final String PREF_FILE_NAME = "couplefun_application";

    public static final String SETTINGS_LANGUAGE = "couplefun_settings_language";
    public static final String DEFAULT = "";

    @Getter private static LocalStore manager;
    private SharedPreferences sp;

    static void init(CoupleFunApp app) {
        manager = new LocalStore(app);
    }

    private LocalStore(CoupleFunApp app) {
        this.sp = app.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getStoredString(String key) {
        return sp.getString(key, DEFAULT);
    }

    public void putStringAsync(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void putStringSyncWait(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
}
