package indi.yume.tools.adapter_renderer.recycler;

import android.view.View;

/**
 * Created by yume on 16-2-29.
 */
public interface OnLongClickListener<M> {
    boolean onLongClick(View view, M content, int position);
}
