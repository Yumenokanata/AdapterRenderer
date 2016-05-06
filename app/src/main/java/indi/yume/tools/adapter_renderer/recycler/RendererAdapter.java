package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import indi.yume.tools.adapter_renderer.ContextAware;
import indi.yume.tools.adapter_renderer.recycler.select.MultipleSelectCollect;
import indi.yume.tools.adapter_renderer.recycler.select.SelectCollect;
import indi.yume.tools.adapter_renderer.recycler.select.SelectMode;
import indi.yume.tools.adapter_renderer.recycler.select.SingleSelectCollect;
import indi.yume.tools.adapter_renderer.util.ListUtil;
import lombok.Setter;

/**
 * Created by yume on 16-2-29.
 */
public class RendererAdapter<M> extends RecyclerView.Adapter<RendererViewHolder<M>> implements RendererCallBack<M>{
    private List<M> contentList;
    private LayoutInflater layoutInflater;
    private Context context;
    private BaseRendererBuilder<M> rendererBuilder;
//    private Map<String, Object> extraDataMap = new HashMap<>();
    @NonNull
    private List<Object> extraDataList;

    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;

    private ItemTouchHelper itemTouchHelper;

    @NonNull
    private SelectCollect selectCollect;
    @SelectMode
    private int selectMode = SelectMode.SELECT_MODE_MULTIPLE;
    private boolean enableSelectable = false;

    public RendererAdapter(List<M> contentList, Context context, Class<? extends BaseRenderer<M>> renderClazz) {
        this(contentList, context, new SingleRenderBuilder<>(renderClazz));
    }

    public RendererAdapter(List<M> contentList, Context context, BaseRendererBuilder<M> rendererBuilder) {
        this.contentList = contentList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rendererBuilder = rendererBuilder;

        extraDataList = ListUtil.newSizeList(contentList.size());
        initSelectCollect();
    }

    public void setSelectMode(@SelectMode int selectMode) {
        this.selectMode = selectMode;
        initSelectCollect();
    }

    private void initSelectCollect() {
        selectCollect = provideSelectCollect(selectMode);
        selectCollect.changeSize(contentList.size());
    }

    @NonNull
    private static SelectCollect provideSelectCollect(@SelectMode int selectMode) {
        switch (selectMode) {
            case SelectMode.SELECT_MODE_MULTIPLE:
                return new MultipleSelectCollect();
            case SelectMode.SELECT_MODE_SINGLE:
                return new SingleSelectCollect();
            default:
                return new MultipleSelectCollect();
        }
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
        for(Integer index : selectCollect.getSelections())
            notifyItemChanged(index);
        selectCollect.deselectAll();
    }

    public boolean isEnableMultipleSelectable() {
        return selectMode == SelectMode.SELECT_MODE_MULTIPLE;
    }

    public void enableMultipleSelectable(boolean enableMultipleSelectable) {
        setSelectMode(enableMultipleSelectable ? SelectMode.SELECT_MODE_MULTIPLE : SelectMode.SELECT_MODE_SINGLE);
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
        extraDataList = ListUtil.newSizeList(extraDataList, contentList.size());
        selectCollect.changeSize(contentList.size());
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
        Collections.swap(extraDataList, from, to);
        selectCollect.swap(from, to);

        if(from < to)
            notifyItemMoved(from, to);
        else
            notifyItemMoved(to, from);
    }

    @Override
    public void insert(int position, M model) {
        contentList.add(position, model);
        extraDataList.add(position, null);
        selectCollect.insertItem(position);

        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void remove(int position) {
        notifyItemRemoved(position);
        contentList.remove(position);
        extraDataList.remove(position);
        selectCollect.removeItem(position);

        notifyItemRangeChanged(0, getItemCount());
    }

    public List<M> remove(Iterable<Integer> deleteIndex) {
        List<M> list = new LinkedList<>();
        for(int position : deleteIndex) {
            list.add(contentList.get(position));

            notifyItemRemoved(position);
            contentList.remove(position);
            extraDataList.remove(position);
            selectCollect.removeItem(position);
        }

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
        contentList.addAll(position, dataSet);

        for(int i = 0; i < dataSet.size(); i++) {
            extraDataList.add(position, null);
            selectCollect.insertItem(position);
        }

        notifyItemRangeInserted(position, dataSet.size());
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void removeRange(int fromPosition, int itemCount) {
        for(int i = 0; i < itemCount; i++)
            if(fromPosition < contentList.size()) {
                contentList.remove(fromPosition);
                extraDataList.remove(fromPosition);
                selectCollect.removeItem(fromPosition);
            }
        notifyItemRangeRemoved(fromPosition, itemCount);
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public void putExtra(int position, Object data) {
        extraDataList.set(position, data);
    }

    @Override
    public Object getExtraData(int position) {
        return extraDataList.get(position);
    }

    @Override
    public void clearAllExtraData() {
        Collections.fill(extraDataList, null);
    }

    @Override
    public int getContentLength() {
        if(contentList == null)
            return 0;

        return contentList.size();
    }

    @Override
    public boolean getIsSelected(int position) {
        return selectCollect.isSelect(position);
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
        return selectCollect.getSelections();
    }

    public Set<M> getSelectedItems() {
        Set<M> selectSet = new HashSet<>();
        Set<Integer> selectIndexSet = selectCollect.getSelections();

        for(int index : selectIndexSet)
            selectSet.add(contentList.get(index));

        return selectSet;
    }

    @Override
    public void toggleSelection(int position) {
        if(!enableSelectable)
            return;

        selectCollect.toggleSelection(position);
        notifyDataSetChanged();
    }

    public void select(Iterable<Integer> selectIndex) {
        selectCollect.select(selectIndex);
        notifyDataSetChanged();
    }

    public void select(int position) {
        selectCollect.select(position);
        notifyDataSetChanged();
    }

    public void deselect() {
        Set<Integer> selectIndexSet = selectCollect.getSelections();
        selectCollect.deselectAll();
        notifyDataSetChanged();
    }

    public void deselect(Iterable<Integer> deselectIndex) {
        selectCollect.deselect(deselectIndex);
        notifyDataSetChanged();
    }

    public void deselect(int position) {
        selectCollect.deselect(position);
        notifyDataSetChanged();
    }

    public List<M> deleteAllSelectedItems() {
        if(!enableSelectable)
            return new ArrayList<>();

        Set<Integer> selectIndexSet = selectCollect.getSelections();
        List<M> list = new LinkedList<>();
        for(int position : selectIndexSet) {
            list.add(contentList.get(position));
            notifyItemRemoved(position);
        }
        selectCollect.deselectAll();

        contentList.removeAll(list);
        notifyDataSetChanged();

        return list;
    }
}
