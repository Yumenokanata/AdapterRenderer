package indi.yume.tools.adapter_renderer.recycler;

import android.support.v7.widget.RecyclerView;

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

    void putExtra(int position, Object data);
    Object getExtraData(int position);
    void clearAllExtraData();
    int getContentLength();

    boolean getIsSelected(int position);
    void setSelect(int position, boolean isSelected);
    void toggleSelection(int position);

    void startDrag(RecyclerView.ViewHolder viewHolder);

    int getReallyIndex(int oriPos);
    boolean checkIndex(int pos);
}
