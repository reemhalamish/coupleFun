package halamish.reem.couplefun;

import android.app.Application;

import lombok.Getter;

/**
 * Created by Re'em on 2/4/2017.
 *
 * the main app
 */

public class CoupleFunApp extends Application {
    @Getter private static CoupleFunApp app;

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        LocalStore.init(this);

    }
}
