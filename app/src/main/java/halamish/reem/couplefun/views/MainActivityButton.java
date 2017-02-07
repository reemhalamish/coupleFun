package halamish.reem.couplefun.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import halamish.reem.couplefun.R;

/**
 * Created by Re'em on 2/4/2017.
 *
 * represents one button on the main activity
 */

public class MainActivityButton extends RelativeLayout {
    public MainActivityButton(Context context) {
        super(context);
        init(context);
    }


    public MainActivityButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        init_attrs(context, attrs);
    }

    public MainActivityButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        init_attrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MainActivityButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        init_attrs(context, attrs);
    }


    private void init(Context context) {
        inflate(context, R.layout.activity_main_button, this);
    }

    private void init_attrs(Context context, AttributeSet attrs) {
        ImageView icon = (ImageView) findViewById(R.id.iv_main_btn_icon);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainActivityButtonAttribute);
        Drawable drawable = a.getDrawable(R.styleable.MainActivityButtonAttribute_main_icon);
        if (drawable != null)
            icon.setImageDrawable(drawable);

        TextView tvTitle = (TextView) findViewById(R.id.tv_main_btn_title);
        CharSequence title = a.getString(R.styleable.MainActivityButtonAttribute_title);
        if (title != null)
            tvTitle.setText(title);

        TextView tvSubtitle = (TextView) findViewById(R.id.tv_main_btn_subtitle);
        CharSequence subtitle = a.getString(R.styleable.MainActivityButtonAttribute_subtitle);
        if (subtitle != null)
            tvSubtitle.setText(subtitle);

        boolean useGreyCircle = a.getBoolean(R.styleable.MainActivityButtonAttribute_use_grey_circle, false);
        if (!useGreyCircle) {
            View vGrayCircle = findViewById(R.id.iv_main_btn_bg);
            vGrayCircle.setVisibility(INVISIBLE);
            icon.setPadding(0,0,0,0);// because the padding only were there so it will be displayed on the background circle
        }


        a.recycle();
    }

}
