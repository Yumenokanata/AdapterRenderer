package indi.yume.tools.adapter_renderer;

/**
 * Created by yume on 16/2/1.
 */
public interface ExtraDataCallback {
    public void putExtra(String key, Object data);
    public Object getExtraData(String key);
    public int getContentLength();
    public void refreshAllData();
}
