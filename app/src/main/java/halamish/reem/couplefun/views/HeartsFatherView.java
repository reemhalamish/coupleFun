package halamish.reem.couplefun.views;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import halamish.reem.couplefun.views.utils.FloatPoint;
import halamish.reem.couplefun.views.utils.IntPoint;
import lombok.Setter;

/**
 * Created by Re'em on 2/7/2017.
 *
 * basically an imageView with a heart and a position
 */

public class HeartsFatherView extends RelativeLayout {
    private static final int HEARTS_AMNT = 25;
    private static final long UPDATE_TIME_MS = 10;
    @Setter boolean shouldContinue = true;

    public HeartsFatherView(Context context) {
        super(context);
    }

    public HeartsFatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartsFatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeartsFatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void startHearts() {
        bringToFront();
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        final Map<HeartView, LayoutParams> heartsLp = new HashMap<>();
        Random random = new Random();
        final int heartSize = HeartView.getSize(getContext());
        for (int i=0; i< HEARTS_AMNT; i++) {
            HeartView heartView = new HeartView(getContext());
            IntPoint heartPosition = heartView.getCurrentPoint();
            heartPosition.y = rect.height() + rect.top;
            heartPosition.x = random.nextInt(rect.width() - heartSize) + rect.left;
            FloatPoint accle = heartView.getAcceleration();
            accle.x = (random.nextFloat() * 5) - 1;
            accle.y = (random.nextFloat() * -15) -5;


            RelativeLayout.LayoutParams lp = new LayoutParams(heartSize, heartSize);
            lp.topMargin = heartPosition.y;
            lp.leftMargin = heartPosition.x;
            addView(heartView, lp);
            heartsLp.put(heartView, lp);
        }

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!shouldContinue) return;
                handler.postDelayed(this, UPDATE_TIME_MS);


                Set<HeartView> heartsToDelete = new HashSet<>();

                for (Map.Entry<HeartView, LayoutParams> entry: heartsLp.entrySet()) {
                    HeartView heartView = entry.getKey();
                    LayoutParams oldParams = entry.getValue();
                    LayoutParams newParams = new LayoutParams(oldParams.width, oldParams.height);
                    IntPoint deltaPosition = heartView.updateNextDelta();
                    newParams.leftMargin = deltaPosition.x + oldParams.leftMargin;
                    newParams.topMargin = deltaPosition.y + oldParams.topMargin;
                    heartView.setLayoutParams(newParams);

                    if (newParams.topMargin + heartSize < 0)
                        heartsToDelete.add(heartView);

                    if (newParams.leftMargin + heartSize < 0)
                        heartsToDelete.add(heartView);

                    if (newParams.leftMargin > rect.width())
                        heartsToDelete.add(heartView);

                }
                for (HeartView heartToDelete: heartsToDelete) {
                    removeView(heartToDelete);
                    heartsLp.remove(heartToDelete);
                }
            }
        };

        handler.post(runnable);
    }


}
