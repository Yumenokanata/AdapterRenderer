package indi.yume.tools.adapter_renderer.recycler;

import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * Created by yume on 16-2-29.
 */
public abstract class BaseRenderer<M>{
    private M content;
    private RendererViewHolder<M> viewHolder;
    private RendererCallBack<M> rendererCallBack;

    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onItemClickListener != null) {
                int p = getAdapterPosition();
                if(p >= 0)
                    onItemClickListener.onItemClick(v, p);
            }
        }
    };
    private View.OnLongClickListener viewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int p = getAdapterPosition();
            return onLongClickListener != null && p >=0 && onLongClickListener.onLongClick(v, p);
        }
    };

    public M getContent() {
        return content;
    }

    public View getMainView() {
        return viewHolder.itemView;
    }

    public boolean isSelected() {
        return rendererCallBack.getIsSelected(getAdapterPosition());
    }

    public void setSelect(boolean isSelected) {
        rendererCallBack.setSelect(getAdapterPosition(), isSelected);
    }
    public void toggleSelection() {
        rendererCallBack.toggleSelection(getAdapterPosition());
    }

    public void setContent(M content) {
        this.content = content;
    }

    public RendererViewHolder<M> getViewHolder() {
        return viewHolder;
    }

    public int getAdapterPosition() {
        return rendererCallBack.getReallyIndex(viewHolder.getAdapterPosition());
    }

    public int getLayoutPosition() {
        return viewHolder.getLayoutPosition();
    }

    public int getOldPosition() {
        return viewHolder.getOldPosition();
    }

    public void onCreate(LayoutInflater layoutInflater, ViewGroup parent, RendererCallBack<M> extraDataCallback){
        this.rendererCallBack = extraDataCallback;
        viewHolder = new RendererViewHolder<>(inflate(layoutInflater, parent), this);
        findView(viewHolder.itemView);
        setListener(viewHolder.itemView);
        viewHolder.itemView.setOnClickListener(viewOnClickListener);
        viewHolder.itemView.setOnLongClickListener(viewOnLongClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    protected Object getExtra(){
        if(rendererCallBack == null)
            return null;
        return rendererCallBack.getExtraData(getAdapterPosition());
    }

    protected Object getExtra(Object defaultValue){
        Object data = getExtra();
        return data != null ? data : defaultValue;
    }

    protected Object getExtra(lazyLoadFun lazyLoadFun){
        Object data = getExtra();
        return data != null ? data : lazyLoadFun.getValue();
    }

    protected void putExtra(Object object){
        if(rendererCallBack != null)
            rendererCallBack.putExtra(getAdapterPosition(), object);
    }

    public void refresh() {
        refresh(getAdapterPosition());
    }

    public void refresh(int position) {
        rendererCallBack.refresh(position);
    }

    public void startDrag() {
        rendererCallBack.startDrag(getViewHolder());
    }

    public void bindDrag(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    startDrag();
                }
                return false;
            }
        });
    }

    public boolean onSelectedChanged() {
        return false;
    }

    public boolean onClearView() {
        return false;
    }

    public void moveUp() {
        int position = getAdapterPosition();
        if(position > 0 && rendererCallBack.checkIndex(viewHolder.getAdapterPosition() - 1))
            move(position, position - 1);
    }

    public void moveDown() {
        int position = getAdapterPosition();
        if(position < getContentLength() - 1 && rendererCallBack.checkIndex(viewHolder.getAdapterPosition() + 1))
            move(position, position + 1);
    }

    public void move(int from, int to) {
        rendererCallBack.move(from, to);
    }

    public void insert(int position, M model) {
        rendererCallBack.insert(position, model);
    }

    public void remove() {
        int position = getAdapterPosition();
        if(position >= 0)
            remove(position);
    }

    public void remove(int position) {
        rendererCallBack.remove(position);
    }

    public void refreshAll() {
        rendererCallBack.refreshAll();
    }

    public void refresh(int fromPosition, int itemCount) {
        rendererCallBack.refresh(fromPosition, itemCount);
    }

    public void insertRange(int position, Collection<M> dataList) {
        rendererCallBack.insertRange(position, dataList);
    }

    public void removeRange(int fromPosition, int itemCount) {
        rendererCallBack.removeRange(fromPosition, itemCount);
    }

    public void clearAllExtraData() {
        rendererCallBack.clearAllExtraData();
    }

    protected int getContentLength() {
        return rendererCallBack != null ? rendererCallBack.getContentLength() : 0;
    }

    public abstract void render();

    protected abstract View inflate(LayoutInflater layoutInflater, ViewGroup parent);

    protected abstract void findView(View rootView);

    protected abstract void setListener(View rootView);

    public interface lazyLoadFun{
        Object getValue();
    }
}
