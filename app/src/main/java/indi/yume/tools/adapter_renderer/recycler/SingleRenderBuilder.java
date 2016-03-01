package indi.yume.tools.adapter_renderer.recycler;

/**
 * Created by yume on 16-2-29.
 */
public class SingleRenderBuilder<M> extends BaseRendererBuilder<M> {
    private Class<? extends BaseRenderer<M>> rendererClazz;

    public SingleRenderBuilder(Class<? extends BaseRenderer<M>> rendererClazz) {
        this.rendererClazz = rendererClazz;
    }

    @Override
    public int getViewType(int position, M content) {
        return 0;
    }

    @Override
    public BaseRenderer<M> getRenderer(int viewType) {
        try {
            return rendererClazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
