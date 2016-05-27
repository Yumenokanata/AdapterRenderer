package indi.yume.tools.adapter_renderer.recycler.headerfooter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import indi.yume.tools.adapter_renderer.recycler.BaseRenderer;
import indi.yume.tools.adapter_renderer.recycler.RendererViewHolder;

/**
 * Created by yume on 16-5-25.
 */

public class OtherViewHolder<M> extends RendererViewHolder<M> {
    public OtherViewHolder(View itemView) {
        super(itemView, null);
    }

    @Override
    public BaseRenderer<M> getRenderer() {
        throw new RuntimeException("this is other view holder");
    }
}
