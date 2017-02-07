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

public class TextViewOpenSansBold extends BaseClassOpenSansTextView {
    public TextViewOpenSansBold(Context context) {
        super(context);
    }
    public TextViewOpenSansBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TextViewOpenSansBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewOpenSansBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    String hebrew() {
        return "OpenSansHebrew-Bold.ttf";
    }

    @Override
    String english() {
        return "OpenSans-Bold.ttf";
    }
}