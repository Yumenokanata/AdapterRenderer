package indi.yume.tools.adapter_renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by yume on 15/7/22.
 */
public class RendererAdapter<T> extends BaseAdapter {

    List<T> contentList;
    LayoutInflater layoutInflater;
    Context context;
    BaseRendererBuilder<T> rendererBuilder;

    public RendererAdapter(List<T> contentList, Context context, BaseRendererBuilder<T> rendererBuilder){
        this.contentList = contentList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rendererBuilder = rendererBuilder;
    }

    public RendererAdapter(List<T> contentList, Context context, Class<? extends BaseRenderer<T>> rendererClazz){
        this.contentList = contentList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.rendererBuilder = new SingleRenderBuilder<T>(rendererClazz);
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T content = contentList.get(position);
        BaseRenderer<T> renderer =  rendererBuilder.setContent(content, position)
                .setConvertView(convertView)
                .setLayoutInflater(layoutInflater)
                .setParent(parent)
                .build();
        if(renderer instanceof ContextAware)
            ((ContextAware)renderer).setContext(context);
        renderer.render();

        return renderer.getView();
    }

    @Override
    public int getItemViewType(int position) {
        return rendererBuilder.getTypeClassIndex(contentList.get(position));
    }

    @Override
    public int getViewTypeCount() {
        return rendererBuilder.getAllRendererClass().size();
    }
}
