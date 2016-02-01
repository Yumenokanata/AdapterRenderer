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

    public int getPostion() {
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

    protected void putExtra(Object object){
        if(extraDataCallback != null)
            extraDataCallback.putExtra(getExtraKey(position), object);
    }

    private static String getExtraKey(int position){
        return String.valueOf(position);
    }

    public abstract void render();

    protected abstract View inflate(LayoutInflater layoutInflater, ViewGroup parent);


    protected abstract void findView(View rootView);

    protected abstract void setListener(View rootView);
}
