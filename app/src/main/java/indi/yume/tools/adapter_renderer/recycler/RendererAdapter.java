package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class RendererAdapter<VH extends BaseRenderer<M>, M> extends RecyclerView.Adapter<RendererViewHolder<M>> implements RendererCallBack<M>{
    private List<M> contentList;
    private LayoutInflater layoutInflater;
    private Context context;
    private BaseRendererBuilder<VH, M> rendererBuilder;
    private Map<String, Object> extraDataMap = new HashMap<>();

    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;

    public RendererAdapter(List<M> contentList, Context context, Class<VH> renderClazz) {
        this(contentList, context, new SingleRenderBuilder<>(renderClazz));
    }

    public RendererAdapter(List<M> contentList, Context context, BaseRendererBuilder<VH, M> rendererBuilder) {
        this.contentList = contentList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rendererBuilder = rendererBuilder;
    }

    @Override
    public RendererViewHolder<M> onCreateViewHolder(ViewGroup parent, int viewType) {
        VH renderer = rendererBuilder.setParent(parent)
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

    @Override
    public void refresh(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void move(int from, int to) {
        Collections.swap(contentList, from, to);
        if(from < to)
            notifyItemMoved(from, to);
        else
            notifyItemMoved(to, from);
        notifyItemRangeChanged(from < to ? from : to, getItemCount());
    }

    @Override
    public void insert(int position, M model) {
        notifyItemInserted(position);
        contentList.add(position, model);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void remove(int position) {
        notifyItemRemoved(position);
        contentList.remove(position);
        notifyItemRangeChanged(position, getItemCount());
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
        notifyItemRangeInserted(position, dataSet.size());
        contentList.addAll(position, dataSet);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void removeRange(int fromPosition, int itemCount) {
        notifyItemRangeRemoved(fromPosition, itemCount);
        for(int i = 0; i < itemCount; i++)
            if(fromPosition < contentList.size())
                contentList.remove(fromPosition);
        notifyItemRangeChanged(fromPosition, getItemCount());
    }

    @Override
    public void putExtra(String key, Object data) {
        extraDataMap.put(key, data);
    }

    @Override
    public Object getExtraData(String key) {
        return extraDataMap.get(key);
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
}
