package indi.yume.tools.adapter_renderer.recycler;

/**
 * Created by yume on 16-9-21.
 */

public interface SelectCheck<T> {
    public boolean isCanBeSelected(int position, T content);
}
