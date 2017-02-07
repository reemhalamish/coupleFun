package halamish.reem.couplefun.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import halamish.reem.couplefun.R;
import halamish.reem.couplefun.views.utils.DpConverter;
import halamish.reem.couplefun.views.utils.FloatPoint;
import halamish.reem.couplefun.views.utils.IntPoint;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Re'em on 2/7/2017.
 *
 * basically an imageview with a heart
 */

public class HeartView extends ImageView {
    private static final int PICTURE_SIZE_DP = 48;
    private static final String TAG = "HeartView";
    @Getter @Setter IntPoint currentPoint;
    @Getter @Setter FloatPoint velocity;
    @Getter @Setter FloatPoint acceleration;

    public HeartView(Context context) {
        super(context);
        init(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                DpConverter.toPxl(PICTURE_SIZE_DP, getContext()),
                DpConverter.toPxl(PICTURE_SIZE_DP, getContext()));
    }

    private void init(Context context) {
        velocity = new FloatPoint(0,0);
        acceleration = new FloatPoint(0,0);
        currentPoint = new IntPoint(0,0);

        setImageResource(R.drawable.heart_ballloon_256);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    public IntPoint updateNextDelta() {
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        return velocity.asInt();
    }

    public static int getSize(Context context) {
        return DpConverter.toPxl(PICTURE_SIZE_DP, context);
    }
}
