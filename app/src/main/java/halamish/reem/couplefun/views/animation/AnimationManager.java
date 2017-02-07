package halamish.reem.couplefun.views.animation;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by Re'em on 2/4/2017.
 *
 * is used to make some awesome animations
 */

public class AnimationManager {
    private static final float DEG_TILT_MIDDLE = 0f;
    private static final float DEG_TILT_LEFT = 30f;
    private static final float DEG_TILT_RIGHT = -DEG_TILT_LEFT;
    private static final long DUR_TILT_MS = 150;
    private static final long DUR_WAIT_BETWEEN_TILTS_MS = 10000;


    public static void makeViewTilt(final View view) {
        RotateAnimation tiltMidToLeft = new RotateAnimation(DEG_TILT_MIDDLE, DEG_TILT_LEFT, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation tiltLeftToRight = new RotateAnimation(DEG_TILT_LEFT, DEG_TILT_RIGHT, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation tiltRightToMid = new RotateAnimation(DEG_TILT_RIGHT, DEG_TILT_MIDDLE, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        tiltMidToLeft.setDuration(DUR_TILT_MS);
        tiltLeftToRight.setDuration(DUR_TILT_MS * 2);
        tiltRightToMid.setDuration(DUR_TILT_MS);

        tiltMidToLeft.setAnimationListener(
                new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.startAnimation(tiltLeftToRight);
                    }
                });
        tiltLeftToRight.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(tiltRightToMid);
            }
        });

        tiltRightToMid.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(
                        () -> view.startAnimation(tiltMidToLeft),
                        DUR_WAIT_BETWEEN_TILTS_MS
                );
            }
        });

        // start the initial animation in a while
        new Handler().postDelayed(
                () -> view.startAnimation(tiltMidToLeft),
                DUR_WAIT_BETWEEN_TILTS_MS
        );
    }
}
