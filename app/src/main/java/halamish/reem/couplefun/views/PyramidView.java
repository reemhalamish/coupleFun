package halamish.reem.couplefun.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.content.ContentValues.TAG;

/**
 * Created by reem on 06/02/17.
 *
 * is used to create a pyramid view.
 */

@Accessors(prefix = "m")
public class PyramidView extends View {
    private static final int DEFAULT_COLOR = Color.BLUE;
//    private static final int DEFAULT_COLOR = Color.parseColor("#00796B"); // primaryDark
    private static final float MINIMUM_APLHA_POSSIBLE = 0.2f;
    private static final float MAXIMUM_APLHA_POSSIBLE = 0.8f;
    private static final float MATH_PYRAMID_INCLINE = 1.1f; // שיפוע
    public static final float ALPHA_NEEDED = 0.2f;

    private Paint mPaint = null;
    private int mColor = DEFAULT_COLOR;
    @Getter @Setter float mStartingPointX; // [0, 3)



    public PyramidView(Context context) {
        super(context);
        init(context);
    }

    public PyramidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PyramidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PyramidView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mStartingPointX = createRandomStartingPoint();
    }

    public void setColor(int color)
    {
        mColor = color;
        mPaint = null;
        invalidate();
    }

    public int getColor()
    {
        return mColor;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); // if some background was applied etc

        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(mColor);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setShader(
                    new LinearGradient(
                            pl,
                            pt,
                            pl,
                            h - pb,
                            new int[]{mColor, Color.TRANSPARENT},
                            new float[]{0f, 1f},
                            Shader.TileMode.CLAMP
                    )
            );
        }

        int startingPointX = convertStartingPoint(pl, w-pr);
//        float alpha = measureNeededAlpha(pl, w-pr, mStartingPointX);
//        setAlpha(alpha);

        Path path = getPath(pl, w-pr, pt, h-pb, startingPointX);

        canvas.drawPath(path, mPaint);


//        canvas.drawCircle(cx, cy, radius, mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "measure: width - " + widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * convert the [0,3) random number to be between [start - delta, end + delta)
     * where delta = end - start
     * @param xStart @yStartAxis
     * @param xEnd @yStartAxis
     * @return int
     */
    private int convertStartingPoint(int xStart, int xEnd) {
        int delta = xEnd - xStart;
        return (int) ((delta * mStartingPointX) - delta + xStart);
    }

    /**
     * calculate a path s.t. it will create a pyramid with m=1 and m=-1
     * and with cutoff in the bottom corners
     *
     * a.k.a a shape like that:
     *
     *     ^
     *    / \
     *   /   \
     *  /     \
     *  |      \
     *  |       \
     *  |        \
     *  |         \
     *  |          \
     *  |           \
     *  -------------
     * @param xStart
     * @param xEnd
     * @param yStart
     * @param yEnd
     * @param topmostPointX
     * @return
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private Path getPath(int xStart, int xEnd, int yStart, int yEnd, int topmostPointX) {
        float m = MATH_PYRAMID_INCLINE; // the constant describing the Incline ( שיפוע )
        float deltaY = yEnd - yStart;
        Path path = new Path();


        if (topmostPointX >= xStart && topmostPointX <= xEnd) {
            path.moveTo(xStart, yEnd);

            float collisionOnStartRect = yStart + (topmostPointX - xStart) * m;
            float collisionOnEndRect = yStart + (xEnd - topmostPointX) * m;

            if (collisionOnStartRect > yEnd) {
                // then the path should look different
                // firstly, find the collistion on yEnd axis:
                float collisionOnYendAxis = topmostPointX - deltaY/m;
                path.lineTo(collisionOnYendAxis, yEnd);
            } else {
                // just go straghit to collision than to topmost
                path.lineTo(xStart, collisionOnStartRect);
            }
            path.lineTo(topmostPointX, yStart);

            if (collisionOnEndRect > yEnd) {
                // then create a speciel path
                // firstly, find the collistion on yEnd axis:
                float collisionOnYendAxis = topmostPointX + deltaY/m;
                path.lineTo(collisionOnYendAxis, yEnd);
            } else {
                // just go straghit to collision than to topmost
                path.lineTo(xEnd, collisionOnEndRect);
                path.lineTo(xEnd, yEnd);
            }

            path.lineTo(xStart, yEnd);

            return path;
        }
        else if (topmostPointX < xStart) {
            path.moveTo(xStart, yEnd);

            // only half pyramid:
            float collisionOnXStartAxis = yStart + m * (xStart - topmostPointX);
            path.lineTo(xStart, collisionOnXStartAxis);
            float collisionOnYEndAxis = topmostPointX + deltaY/m;
            path.lineTo(collisionOnYEndAxis, yEnd);
            path.lineTo(xStart, yEnd);

            return path;
        } else {
            // topmostPointX > xEnd,
            // i.e. only half pyramid:
            float collisionOnXEndAxis = yStart + m * (topmostPointX - xEnd);
            float collisionOnYEndAxis = topmostPointX - deltaY/m;

            path.moveTo(xEnd, yEnd);
            path.lineTo(xEnd, collisionOnXEndAxis);
            path.lineTo(collisionOnYEndAxis, yEnd);
            path.lineTo(xEnd, yEnd);
            return path;
        }
    }

    /**
     * measure the needed alpha for the pyramid. the closer the pyramid is to the middle of the x axis, the less alpha it will receive
     * @param start @ x axis
     * @param end @ x axis
     * @param topmostPoint  @ x axis
     * @return a float in range [0, 1]
     */
    private float measureNeededAlpha(int start, int end, int topmostPoint) {
        double dist1 = (topmostPoint - start) * 1.0;
        double dist2 = (end - topmostPoint) * 1.0;
        double maxDist = Math.max(dist1, dist2);
        double maxDistPrctg = maxDist / (end - start); // [50% to 100%] or [.5, 1]
        double maxDistPrctgFixed01 = ((maxDistPrctg - 0.5)*2); // [0, 1]
        double maxDistPrctgMinMax = (maxDistPrctgFixed01 * (MAXIMUM_APLHA_POSSIBLE-MINIMUM_APLHA_POSSIBLE)) + MINIMUM_APLHA_POSSIBLE; // [MINIMUM_APLHA_POSSIBLE, MAXIMUM_APLHA_POSSIBLE]
        return (float) maxDistPrctgMinMax;
    }

    /**
     * get a random point for the topmost point of the pyramid
     * @return a random point in the range [0, 3) with heaviness in the sides
     */
    private float createRandomStartingPoint() {
        float current = (float) (new Random().nextDouble() * 5) - 1;
        if (current < 0)
            current += 1f;
        if (current >= 4)
            current -= 1f;
        return current;
    }

    /**
     * get a random point for the topmost point of the pyramid
     * @param start @ x axis
     * @param end @ x axis
     * @return a random point in the range [start-d, end+d] where d = (end - start)
     */
    private int getRandomStartingPoint(int start, int end) {
        int delta = end - start;



        return new Random().nextInt(3 * delta) + start - delta;
    }
}
