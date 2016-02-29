package indi.yume.tools.adapter_renderer.recycler;

import java.util.Collection;

/**
 * Created by yume on 16-2-29.
 */
interface RendererCallBack<M> {
    void refresh(int position);
    void move(int from, int to);
    void insert(int position, M model);
    void remove(int position);

    void refreshAll();
    void refresh(int fromPosition, int itemCount);
    void insertRange(int position, Collection<M> dataSet);
    void removeRange(int fromPosition, int itemCount);

    void putExtra(String key, Object data);
    Object getExtraData(String key);
    void clearAllExtraData();
    int getContentLength();
}
