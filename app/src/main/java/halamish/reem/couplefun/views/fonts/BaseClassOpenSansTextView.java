package halamish.reem.couplefun.views.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Re'em on 2/4/2017.
 *
 * text view with OpenSans font
 */

@SuppressWarnings("FieldCanBeLocal")
public abstract class BaseClassOpenSansTextView extends TextView {
    abstract String hebrew();
    abstract String english();
    public static Typeface getSpecialTypeface(Context context, BaseClassOpenSansTextView textView) {
//        if (Utils.getCurrentLocale(context).getLanguage().equals("he"))
//            return Typeface.createFromAsset(context.getAssets(), textView.hebrew());
        return Typeface.createFromAsset(context.getAssets(), textView.english());
    }
    private void init(Context context) {
        setTypeface(getSpecialTypeface(context, this));
    }


    public BaseClassOpenSansTextView(Context context) {
        super(context); init(context);
    }
    public BaseClassOpenSansTextView(Context context, AttributeSet attrs) {
        super(context, attrs); init(context);
    }
    public BaseClassOpenSansTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseClassOpenSansTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes); init(context);
    }
}
