package indi.yume.tools.adapter_renderer.recycler;

import android.view.LayoutInflater;
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

    public M getContent() {
        return content;
    }

    public void setContent(M content) {
        this.content = content;
    }

    public RendererViewHolder<M> getViewHolder() {
        return viewHolder;
    }

    public int getAdapterPosition() {
        return viewHolder.getAdapterPosition();
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
    }

    protected Object getExtra(){
        if(rendererCallBack == null)
            return null;
        return rendererCallBack.getExtraData(getExtraKey(getAdapterPosition()));
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
            rendererCallBack.putExtra(getExtraKey(getAdapterPosition()), object);
    }

    private static String getExtraKey(int position){
        return String.valueOf(position);
    }

    public void refresh() {
        refresh(getAdapterPosition());
    }

    public void refresh(int position) {
        rendererCallBack.refresh(position);
    }

    public void moveUp() {
        if(getAdapterPosition() > 0)
            move(getAdapterPosition(), getAdapterPosition() - 1);
    }

    public void moveDown() {
        if(getAdapterPosition() < getContentLength() - 1)
            move(getAdapterPosition(), getAdapterPosition() + 1);
    }

    public void move(int from, int to) {
        rendererCallBack.move(from, to);
    }

    public void insert(int position, M model) {
        rendererCallBack.insert(position, model);
    }

    public void remove() {
        remove(getAdapterPosition());
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
