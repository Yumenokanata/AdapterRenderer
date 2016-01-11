package indi.yume.tools.adapter_renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yume on 15/10/16.
 */
public class SingleRenderBuilder<T> extends BaseRendererBuilder<T> {
    private List<Class<? extends BaseRenderer>> list;

    public SingleRenderBuilder(Class<? extends BaseRenderer<T>> renderClazz){
        list = new ArrayList<>();
        list.add(renderClazz);
    }

    @Override
    public int getTypeClassIndex(T content) {
        return 0;
    }

    @Override
    public List<Class<? extends BaseRenderer>> getAllRendererClass() {
        return list;
    }
}
