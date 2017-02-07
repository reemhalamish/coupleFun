package halamish.reem.couplefun.locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import halamish.reem.couplefun.LocalStore;

/**
 * Created by Re'em on 2/4/2017.
 *
 * is used to master the relevant locale
 */

public class LocaleUtils {
    public static String LOC_HEB = "iw";
    public static String LOC_ENG = "en";

    public static String getLocaleLanguage(){
        return LocalStore.getManager().getStoredString(LocalStore.SETTINGS_LANGUAGE);
    }

    public static void setLocaleLanguage(String newLocale) {
        LocalStore.getManager().putStringSyncWait(LocalStore.SETTINGS_LANGUAGE, newLocale);
    }
}
