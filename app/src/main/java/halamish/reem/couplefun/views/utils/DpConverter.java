package halamish.reem.couplefun.views.utils;

import android.content.Context;

/**
 * Created by Re'em on 2/7/2017.
 *
 * convert dp <---> pixels
 */

public class DpConverter {
    public static int toDp(int pxls, Context context) {
        return (int)(pxls / context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int toPxl(int dps, Context context) {
        return (int)(dps * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
