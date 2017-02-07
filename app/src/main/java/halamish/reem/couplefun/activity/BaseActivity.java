package halamish.reem.couplefun.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import halamish.reem.couplefun.locale.ContextLocaleWrapper;
import halamish.reem.couplefun.LocalStore;

/**
 * Created by Re'em on 2/4/2017.
 *
 * is used as a base activity
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    /**
     * is used to display the right language in activities
     */
    @Override
    protected void attachBaseContext(Context context) {
        String language = LocalStore.getManager().getStoredString(LocalStore.SETTINGS_LANGUAGE);
        super.attachBaseContext(ContextLocaleWrapper.wrap(context,language));
    }
}
