package halamish.reem.couplefun.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import halamish.reem.couplefun.LocalStore;
import halamish.reem.couplefun.R;
import halamish.reem.couplefun.locale.LocaleUtils;
import halamish.reem.couplefun.views.HeartsFatherView;
import halamish.reem.couplefun.views.PyramidView;
import halamish.reem.couplefun.views.StarView;
import halamish.reem.couplefun.views.animation.AnimationManager;
import halamish.reem.couplefun.views.utils.IntPoint;

public class SplashActivity extends Activity {
    private static final long SPLASH_TIME_MS = 500;
    private static final float STAR_POS_Y_START_PRCTG = 0.0f;
    private static final float STAR_POS_Y_END_PRCTG = 0.3f;
    private static final float PYRAMID_POS_Y_START_PRCTG = STAR_POS_Y_END_PRCTG - 0.1f;
    private static final float PYRAMID_POS_Y_START_BIGGER_PRCTG = PYRAMID_POS_Y_START_PRCTG + 0.1f;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final long UPDATE_STARS_DELAY_MS = 100;
    private static final int STARS_AMOUNT = 100;
    private static final int TRYS_AMOUNT_MAKE_NEW_STAR = 100;
    private static final int STAR_COLLISION_PIXELS = (int) (StarView.getSIZE() * 1.5);
    private static final int PYRAMID_ON_SCREEN_AMNT = 50;

    public List<Float> getPyramidTopmostPoints() {
        ArrayList<Float> allPoints = new ArrayList<>();
        float start = 0f;
        float end = 3f;
        Random random = new Random();
        while (start <= end) {
            allPoints.add(start);
            float jump = (float) ((random.nextFloat() * 0.2) + 0.1);
            start += jump;
        }
        return allPoints;


    }


    View btnHe, btnEn;
    RelativeLayout rlBg;
    HeartsFatherView hfvHearts;
    Map<StarView, IntPoint> mViewPointMap;
    Set<PyramidView> mPyramidViewSet;
    boolean mActivityFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalStore.getManager().putStringSyncWait(LocalStore.SETTINGS_LANGUAGE, LocalStore.DEFAULT);

        super.onCreate(savedInstanceState);

        changeStatusbarColor();
//        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        //Remove notification bar
//        this.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//        );

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_splash);
        initMembers();

        // if first time user sees the splash screen, display buttons for hebrew and english
        if (LocalStore.getManager().getStoredString(LocalStore.SETTINGS_LANGUAGE)
                .equals(LocalStore.DEFAULT)) {
            // haven't been here before
            setButtonListeners();
            setButtonTiltingAnimation();

        } else {
            for (View btn: new View[]{btnEn, btnHe}) {
                btn.setVisibility(View.GONE);
            }
            // just continue to next activity
            new Handler().postDelayed(startNextActivity(), SPLASH_TIME_MS);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && mViewPointMap.size() == 0) {
            createShineyLittleStars();
//            createPyramids();
            bringButtonsToFront();
            hfvHearts.startHearts();
        } else {
            for (StarView view: mViewPointMap.keySet())
                rlBg.removeView(view);
            for (PyramidView view: mPyramidViewSet)
                rlBg.removeView(view);
        }
    }

    private void bringButtonsToFront() {
        btnEn.bringToFront();
        btnHe.bringToFront();
    }

    private void createPyramids() {
        Rect rect = new Rect();
        rlBg.getLocalVisibleRect(rect);
        if (rect.width() <= 0) throw new AssertionError();
        if (rect.height() <= 0) throw new AssertionError();
        Random random = new Random();
        int lowestYStart = (int) (rect.height() * PYRAMID_POS_Y_START_PRCTG);
        int highestYStart = (int) (rect.height() * PYRAMID_POS_Y_START_BIGGER_PRCTG);
        int deltaYStart = highestYStart - lowestYStart;
        int heightRect = rect.height();
        int widthRect = rect.width();


        for (float topmostPoint: getPyramidTopmostPoints()){
            int pyramidStartY = random.nextInt(deltaYStart) + lowestYStart;
            PyramidView pyramidView = new PyramidView(this);
            pyramidView.setStartingPointX(topmostPoint);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(widthRect,heightRect-pyramidStartY);
            lp.topMargin = pyramidStartY;
            rlBg.addView(pyramidView, lp);
//            pyramidView.setAlpha(PyramidView.ALPHA_NEEDED);
            mPyramidViewSet.add(pyramidView);
        }
    }

    private void createShineyLittleStars() {
        Rect rect = new Rect();
        rlBg.getLocalVisibleRect(rect);
        if (rect.width() <= 0) throw new AssertionError();
        if (rect.height() <= 0) throw new AssertionError();

        int starViewSize = StarView.getSIZE();

        for (IntPoint point: calculatePointsForStars(rect)) {

            StarView starView = new StarView(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(starViewSize, starViewSize);
            lp.topMargin = point.y;
            lp.leftMargin = point.x;

            mViewPointMap.put(starView, point);
            rlBg.addView(starView, lp);
            starView.updateAlphaRandomly();
        }

        final Handler handler = new Handler();
        Runnable updateStars = new Runnable() {
            @Override
            public void run() {
                if (mActivityFinished) return;
                for (Map.Entry<StarView, IntPoint> entry: mViewPointMap.entrySet()) {
                    StarView starView = entry.getKey();

                    starView.updateAlphaRandomly();

//                    float dx, dy;
//                    int range = random.nextInt(16);
//                    dx = random.nextFloat() * 2 - 1; // [-1, 1]
//                    dy = random.nextFloat() * 2 - 1; // [-1, 1]
//                    dx = random.nextFloat() * range - (range/2); // [-range/2, range/2]
//                    dy = random.nextFloat() * range - (range/2); // [-range/2, range/2]


//                    starView.setX(starView.getX() + dx);
//                    starView.setY(starView.getY() + dy);

                }

                handler.postDelayed(this, UPDATE_STARS_DELAY_MS);
            }
        };
        handler.post(updateStars);

    }

    private List<IntPoint> calculatePointsForStars(Rect rect) {
        List<IntPoint> pointsAddedSoFar = new ArrayList<>();

        int starViewSize = StarView.getSIZE();
        Random random = new Random();
        int relevantHeight = (int) (rect.height() * STAR_POS_Y_END_PRCTG);

        for (int i=0; i < STARS_AMOUNT; i++) {
            boolean foundStarPoint = false;

            for (int j=0; j < TRYS_AMOUNT_MAKE_NEW_STAR; j++) {

                // calculate place for new point
                int pointX = random.nextInt(rect.width());
                if (pointX < starViewSize)
                    pointX += starViewSize;
                if (rect.width() - pointX < starViewSize)
                    pointX -= starViewSize;

                int pointY = random.nextInt(relevantHeight);
                if (pointY < starViewSize)
                    pointY += starViewSize;
                if (relevantHeight - pointY < starViewSize)
                    pointY -= starViewSize;

                // check it doesn't collide with other points
                boolean intersecting = false;
                for (IntPoint existingPoint: pointsAddedSoFar) {
                    int dy, dx;
                    dy = Math.abs(existingPoint.y - pointY);
                    dx = Math.abs(existingPoint.x - pointX);
                    float delta = (float) Math.sqrt(dy*dy + dx*dx);
                    if (delta < STAR_COLLISION_PIXELS) {
                        intersecting = true;
                        break;
                    }
                }
                if (!intersecting) {
                    foundStarPoint = true; // as the for loop went all
                    pointsAddedSoFar.add(new IntPoint(pointX, pointY));
                    break; // the for loop on TRYS_AMOUNT_MAKE_NEW_STAR
                }
            }

            // here finished for loop on TRYS_AMOUNT_MAKE_NEW_STAR
            if (!foundStarPoint)
                // after all of those tries? return only stars you DID found place for
                return pointsAddedSoFar;
        } // finished for on STARS_AMOUNT
        return pointsAddedSoFar;
    }

    private void changeStatusbarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
//            window.setStatusBarColor(ContextCompat.getColor(this, R.color.splash_top_pink_material_statusbar));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.splash_top_pink));
        }
    }

    private void setButtonTiltingAnimation() {
        AnimationManager.makeViewTilt(btnEn);
        AnimationManager.makeViewTilt(btnHe);
    }

    private void setButtonListeners() {
        btnHe.setOnClickListener(view -> {
            LocaleUtils.setLocaleLanguage(LocaleUtils.LOC_HEB);
            new Handler().post(startNextActivity());
        });

        btnEn.setOnClickListener(view -> {
            LocaleUtils.setLocaleLanguage(LocaleUtils.LOC_ENG);
            new Handler().post(startNextActivity());
        });
    }

    private void initMembers() {
        btnHe = findViewById(R.id.tv_splash_he);
        btnEn = findViewById(R.id.tv_splash_en);
        rlBg = (RelativeLayout) findViewById(R.id.rl_splash_bg);
        hfvHearts = (HeartsFatherView) findViewById(R.id.hfv_splash);
        mViewPointMap = new HashMap<>();
        mPyramidViewSet = new HashSet<>();
        mActivityFinished = false;
    }

    private Runnable startNextActivity() {
        return () -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityFinished = true;
        hfvHearts.setShouldContinue(false);
    }
}
