package halamish.reem.couplefun.views.fonts;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

/**
 * Created by Re'em on 2/4/2017.
 *
 * regular text view
 */

public class TextViewOpenSans extends BaseClassOpenSansTextView {
    public TextViewOpenSans(Context context) {
        super(context);
    }
    public TextViewOpenSans(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TextViewOpenSans(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewOpenSans(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    String hebrew() {
        return "OpenSansHebrew-Regular.ttf";
    }

    @Override
    String english() {
        return "OpenSans-Regular.ttf";
    }
}