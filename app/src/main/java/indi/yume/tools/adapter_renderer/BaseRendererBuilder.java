package indi.yume.tools.adapter_renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yume on 15/7/22.
 */
public abstract class BaseRendererBuilder<T> {
    private T content;
    private int position;
    private View convertView;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private ExtraDataCallback extraDataCallback;

    public BaseRendererBuilder<T> setContent(T content, int position) {
        this.content = content;
        this.position = position;
        return this;
    }

    public BaseRendererBuilder<T> setConvertView(View convertView) {
        this.convertView = convertView;
        return this;
    }

    public BaseRendererBuilder<T> setParent(ViewGroup parent) {
        this.parent = parent;
        return this;
    }

    public BaseRendererBuilder<T> setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        return this;
    }

    public BaseRendererBuilder<T> setExtraDataCallback(ExtraDataCallback extraDataCallback) {
        this.extraDataCallback = extraDataCallback;
        return this;
    }

    public BaseRenderer<T> build(){
        if(isRecyclable(content, convertView)){
            return recycle();
        }else{
            return create();
        }
    }

    private boolean isRecyclable(T content, View convertView){
        if(convertView == null)
            return false;
        Object o = convertView.getTag();
        return (o != null && o.equals(getRendererClassByIndex(getTypeClassIndex(content))));
    }

    private BaseRenderer<T> recycle(){
        BaseRenderer<T> renderer = (BaseRenderer<T>)convertView.getTag();
        renderer.setContent(content, position);
        return renderer;
    }

    protected BaseRenderer<T> create(){
        BaseRenderer<T> renderer;
        renderer = getRendererByIndex(getTypeClassIndex(content));
        renderer.onCreate(content, position, layoutInflater, parent, extraDataCallback);
        return renderer;
    }

    public abstract int getTypeClassIndex(T content);
    public abstract List<Class<? extends BaseRenderer>> getAllRendererClass();

    private Class getRendererClassByIndex(int index){
        return getAllRendererClass().get(index);
    }

    private BaseRenderer<T> getRendererByIndex(int index){
        try {
            return (BaseRenderer<T>) getRendererClassByIndex(index).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
