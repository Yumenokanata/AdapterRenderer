package indi.yume.tools.adapter_renderer.recycler.select;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yume on 16-5-6.
 */
@IntDef({SelectMode.SELECT_MODE_SINGLE,
        SelectMode.SELECT_MODE_MULTIPLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SelectMode {
    public static final int SELECT_MODE_SINGLE = 0x11;
    public static final int SELECT_MODE_MULTIPLE = 0x22;
}
