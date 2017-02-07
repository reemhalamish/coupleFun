package halamish.reem.couplefun.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import lombok.Getter;

/**
 * Created by reem on 05/02/17.
 *
 * is used to display a simple star
 */

public class StarView extends View {
    private static final int DEFAULT_CIRCLE_COLOR = Color.WHITE;
    private static final float ALPHA_CHANGE_STEPS = 0.04f;
    @Getter private static final int SIZE = 20;

    private int circleColor = DEFAULT_CIRCLE_COLOR;
    private float alpha;
    private Paint mPaint = null;

    public StarView(Context context)
    {
        super(context);
        init(context, null);
    }

    public StarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        alpha = new Random().nextFloat();
    }

    public void setCircleColor(int circleColor)
    {
        this.circleColor = circleColor;
        this.mPaint = null;
        invalidate();
    }

    public int getCircleColor()
    {
        return circleColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(SIZE, SIZE);
    }

    protected void onDraw(Canvas canvas)
    {
        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(circleColor);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setShader(new RadialGradient(cx, cy,
                    radius, circleColor, Color.TRANSPARENT, Shader.TileMode.CLAMP));
        }


        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    public void updateAlphaRandomly() {
        setAlpha(alpha);

        // make the alpha changed a little bit
        // on a coin toss, make it go more or less in one step
        if (new Random().nextBoolean()) {
            alpha = Math.min(1f, alpha + ALPHA_CHANGE_STEPS);
        } else {
            alpha = Math.max(0f, alpha - ALPHA_CHANGE_STEPS);
        }
    }
}
