package indi.yume.tools.adapter_renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yume on 15/7/22.
 */
public abstract class BaseRenderer<T> {
    private T content;
    private View view;
    private int position;
    private ExtraDataCallback extraDataCallback;

    public T getContent() {
        return content;
    }

    public void setContent(T content, int position) {
        this.content = content;
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void onCreate(T content, int position, LayoutInflater layoutInflater, ViewGroup parent, ExtraDataCallback extraDataCallback){
        this.extraDataCallback = extraDataCallback;
        this.content = content;
        this.position = position;
        view = inflate(layoutInflater, parent);
        findView(view);
        setListener(view);
    }

    protected Object getExtra(){
        if(extraDataCallback == null)
            return null;
        return extraDataCallback.getExtraData(getExtraKey(position));
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
        if(extraDataCallback != null)
            extraDataCallback.putExtra(getExtraKey(position), object);
    }

    protected void notifyDataHasChanged(){
        if(extraDataCallback != null)
            extraDataCallback.refreshAllData();
    }

    private static String getExtraKey(int position){
        return String.valueOf(position);
    }

    protected int getContentLength() {
        return extraDataCallback != null ? extraDataCallback.getContentLength() : 0;
    }

    public abstract void render();

    protected abstract View inflate(LayoutInflater layoutInflater, ViewGroup parent);


    protected abstract void findView(View rootView);

    protected abstract void setListener(View rootView);

    public interface lazyLoadFun{
        Object getValue();
    }
}
