package halamish.reem.couplefun.views.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Re'em on 2/7/2017.
 *
 * utils
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FloatPoint {
    public float x, y;

    public IntPoint asInt() {
        return new IntPoint((int) x, (int) y);
    }
}