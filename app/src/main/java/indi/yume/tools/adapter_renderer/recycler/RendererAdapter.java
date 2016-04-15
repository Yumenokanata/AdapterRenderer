package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(from < to)
            notifyItemMoved(from, to);
        else
            notifyItemMoved(to, from);
    }

    @Override
    public void insert(int position, M model) {
        contentList.add(position, model);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void remove(int position) {
        contentList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
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
        contentList.addAll(position, dataSet);
        notifyItemRangeInserted(position, dataSet.size());
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void removeRange(int fromPosition, int itemCount) {
        for(int i = 0; i < itemCount; i++)
            if(fromPosition < contentList.size())
                contentList.remove(fromPosition);
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
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if(itemTouchHelper != null)
            itemTouchHelper.startDrag(viewHolder);
    }

    private static String getExtraKey(int position){
        return String.valueOf(position);
    }
}
