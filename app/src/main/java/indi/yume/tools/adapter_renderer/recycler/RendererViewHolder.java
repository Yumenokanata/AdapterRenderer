package indi.yume.tools.adapter_renderer.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import indi.yume.tools.adapter_renderer.*;

/**
 * Created by yume on 16-2-29.
 */
public class RendererViewHolder<M> extends RecyclerView.ViewHolder {
    private BaseRenderer<M> renderer;

    public RendererViewHolder(View itemView, BaseRenderer<M> renderer) {
        super(itemView);
        this.renderer = renderer;
    }

    public BaseRenderer<M> getRenderer() {
        return renderer;
    }
}
