package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import indi.yume.tools.adapter_renderer.ContextAware;

/**
 * Created by yume on 16-2-29.
 */
public class RendererAdapter<M> extends RecyclerView.Adapter<RendererViewHolder<M>> implements RendererCallBack<M>{
    private List<M> contentList;
    private LayoutInflater layoutInflater;
    private Context context;
    private BaseRendererBuilder<M> rendererBuilder;
    private Map<String, Object> extraDataMap = new HashMap<>();

    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;

    private ItemTouchHelper itemTouchHelper;

    private Set<Integer> selections = new HashSet<>();
    private boolean enableSelectable = false;
    private boolean enableMultipleSelectable = true;

    public RendererAdapter(List<M> contentList, Context context, Class<? extends BaseRenderer<M>> renderClazz) {
        this(contentList, context, new SingleRenderBuilder<>(renderClazz));
    }

    public RendererAdapter(List<M> contentList, Context context, BaseRendererBuilder<M> rendererBuilder) {
        this.contentList = contentList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rendererBuilder = rendererBuilder;
    }

    @Override
    public RendererViewHolder<M> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRenderer<M> renderer = rendererBuilder.setParent(parent)
                .setLayoutInflater(layoutInflater)
                .setRendererCallback(this)
                .build(viewType);

        if(renderer != null)
            return renderer.getViewHolder();

        return null;
    }

    @Override
    public void onBindViewHolder(RendererViewHolder<M> holder, final int position) {
        BaseRenderer<M> renderer = holder.getRenderer();
        renderer.setContent(getItem(position));
        doForEveryRenderer(renderer, holder.getItemViewType());
        if(renderer instanceof ContextAware)
            ((ContextAware)renderer).setContext(context);
        renderer.setOnItemClickListener(onItemClickListener);
        renderer.setOnLongClickListener(onLongClickListener);

        renderer.render();
    }

    public boolean isEnableSelectable() {
        return enableSelectable;
    }

    public void enableSelectable(boolean enableSelectable) {
        this.enableSelectable = enableSelectable;
        for(Integer index : selections)
            notifyItemChanged(index);
        selections.clear();
    }

    public boolean isEnableMultipleSelectable() {
        return enableMultipleSelectable;
    }

    public void enableMultipleSelectable(boolean enableMultipleSelectable) {
        this.enableMultipleSelectable = enableMultipleSelectable;

        Integer index = null;
        for(int i : selections) {
            index = i;
            break;
        }
        if(index == null)
            return;

        for(int position : selections)
            if(position != index)
                notifyItemChanged(index);
        selections.clear();

        selections.add(index);
    }

    protected void doForEveryRenderer(BaseRenderer<M> renderer, int viewType) { }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return rendererBuilder.getViewType(position, getItem(position));
    }

    public M getItem(int position) {
        return contentList.get(position);
    }

    public List<M> getContentList() {
        return contentList;
    }

    public void setContentList(List<M> contentList) {
        this.contentList = contentList;
        selections.clear();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        notifyDataSetChanged();
    }

    /*
     * @hide
     */
    void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public DragHelper enableDrag(RecyclerView recyclerView) {
        return enableDrag(recyclerView, true, true);
    }

    public DragHelper enableDrag(RecyclerView recyclerView, boolean enableLongPressDrag, boolean enableSwipeView) {
        DragHelper dragHelper = new DragHelper(this, enableLongPressDrag, enableSwipeView);
        itemTouchHelper = new ItemTouchHelper(dragHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return dragHelper;
    }

    public void enableDrag(RecyclerView recyclerView, ItemTouchHelper.Callback callback) {
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void refresh(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void move(int from, int to) {
        swap(from, to);
        notifyItemRangeChanged(from < to ? from : to, getItemCount());
    }

    void swap(int from, int to) {
        Collections.swap(contentList, from, to);

        if(selections.contains(from) && !selections.contains(to)) {
            selections.remove(from);
            selections.add(to);
        } else if(selections.contains(to) && !selections.contains(from)) {
            selections.remove(to);
            selections.add(from);
        }

        if(from < to)
            notifyItemMoved(from, to);
        else
            notifyItemMoved(to, from);
    }

    @Override
    public void insert(int position, M model) {
        contentList.add(position, model);

        Set<Integer> newSet = new HashSet<>();
        for(int index : selections)
            if(index >= position) {
                selections.remove(index);
                newSet.add(index + 1);
            } else {
                newSet.add(index);
            }
        selections = newSet;

        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void remove(int position) {
        selections.remove(position);
        notifyItemRemoved(position);
        contentList.remove(position);
        notifyItemRangeChanged(0, getItemCount());
    }

    public List<M> remove(Iterable<Integer> deleteIndex) {
        List<M> list = new LinkedList<>();
        for(int position : deleteIndex) {
            list.add(contentList.get(position));
            selections.remove(position);
            notifyItemRemoved(position);
        }

        contentList.removeAll(list);
        notifyItemRangeChanged(0, getItemCount());

        return list;
    }

    @Override
    public void refreshAll() {
        notifyAll();
    }

    @Override
    public void refresh(int fromPosition, int itemCount) {
        notifyItemRangeChanged(fromPosition, itemCount);
    }

    @Override
    public void insertRange(int position, Collection<M> dataSet) {
        int oldSize = contentList.size();
        contentList.addAll(position, dataSet);
        int newSize = contentList.size();

        int interval = newSize - oldSize;
        Set<Integer> newSet = new HashSet<>();
        for(int index : selections)
            if(index >= position) {
                selections.remove(index);
                newSet.add(index + interval);
            } else {
                newSet.add(index);
            }
        selections = newSet;

        notifyItemRangeInserted(position, dataSet.size());
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void removeRange(int fromPosition, int itemCount) {
        for(int i = 0; i < itemCount; i++)
            if(fromPosition < contentList.size()) {
                contentList.remove(fromPosition);
                selections.remove(fromPosition);
            }
        notifyItemRangeRemoved(fromPosition, itemCount);
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public void putExtra(int position, Object data) {
        extraDataMap.put(getExtraKey(position), data);
    }

    @Override
    public Object getExtraData(int position) {
        return extraDataMap.get(getExtraKey(position));
    }

    @Override
    public void clearAllExtraData() {
        extraDataMap.clear();
    }

    @Override
    public int getContentLength() {
        if(contentList == null)
            return 0;

        return contentList.size();
    }

    @Override
    public boolean getIsSelected(int position) {
        return selections.contains(position);
    }

    @Override
    public void setSelect(int position, boolean isSelected) {
        if(enableSelectable)
            if(isSelected)
                select(position);
            else
                deselect(position);
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if(itemTouchHelper != null)
            itemTouchHelper.startDrag(viewHolder);
    }

    private static String getExtraKey(int position){
        return String.valueOf(position);
    }

    public Set<Integer> getSelections() {
        return selections;
    }

    public Set<M> getSelectedItems() {
        Set<M> itemSet = new HashSet<>();
        for(Integer index : selections)
            itemSet.add(getItem(index));
        return itemSet;
    }

    @Override
    public void toggleSelection(int position) {
        if(!enableSelectable)
            return;

        if(selections.contains(position))
            deselect(position);
        else
            select(position);
    }

    public void select(Iterable<Integer> selectIndex) {
        for(Integer index : selectIndex)
            select(index);
    }

    public void select(int position) {
        if(!enableMultipleSelectable) {
            for(int index : selections)
                if(position != index)
                    notifyItemChanged(index);
            selections.clear();
        }

        selections.add(position);
        notifyItemChanged(position);
    }

    public void deselect() {
        for(int index : selections)
            notifyItemChanged(index);
        selections.clear();
    }

    public void deselect(Iterable<Integer> deselectIndex) {
        for(Integer index : deselectIndex)
            deselect(index);
    }

    public void deselect(int position) {
        selections.remove(position);
        notifyItemChanged(position);
    }

    public List<M> deleteAllSelectedItems() {
        if(!enableSelectable)
            return new ArrayList<>();

        List<M> list = new LinkedList<>();
        for(int position : selections) {
            list.add(contentList.get(position));
            notifyItemRemoved(position);
        }
        selections.clear();

        contentList.removeAll(list);
        notifyItemRangeChanged(0, getItemCount());

        return list;
    }
}
