package indi.yume.tools.adapter_renderer.recycler;

import android.view.View;

/**
 * Created by yume on 16-2-29.
 */
public interface OnItemClickListener<M> {
    void onItemClick(View view, M content, int position);
}
