package indi.yume.tools.adapter_renderer.recycler;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yume on 16-2-29.
 */
public abstract class BaseRendererBuilder<M> {
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private RendererCallBack<M> rendererCallBack;

    public BaseRendererBuilder<M> setParent(ViewGroup parent) {
        this.parent = parent;
        return this;
    }

    public BaseRendererBuilder<M> setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        return this;
    }

    public BaseRendererBuilder<M> setRendererCallback(RendererCallBack<M> rendererCallBack) {
        this.rendererCallBack = rendererCallBack;
        return this;
    }

    @Nullable
    public BaseRenderer<M> build(int viewType) {
        BaseRenderer<M> renderer = getRenderer(viewType);
        if(renderer != null)
            renderer.onCreate(layoutInflater, parent, rendererCallBack);
        return renderer;
    }

    public abstract int getViewType(int position, M content);
    public abstract BaseRenderer<M> getRenderer(int viewType);
}
