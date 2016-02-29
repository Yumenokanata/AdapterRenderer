package indi.yume.tools.adapter_renderer.recycler;

/**
 * Created by yume on 16-2-29.
 */
public class SingleRenderBuilder<VH extends BaseRenderer<M>, M> extends BaseRendererBuilder<VH, M> {
    private Class<VH> rendererClazz;

    public SingleRenderBuilder(Class<VH> rendererClazz) {
        this.rendererClazz = rendererClazz;
    }

    @Override
    public int getViewType(int position, M content) {
        return 0;
    }

    @Override
    public VH getRenderer(int viewType) {
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
