package indi.yume.tools.adapter_renderer.recycler.headerfooter;

import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yume on 16-5-25.
 */

public class OtherViewManger {
    private static final int VIEW_TYPE_MOCK = 0xf0000000;

    public static final int HEADER_VIEW_TYPE = 0x1 << 28;
    public static final int CONTENT_VIEW_TYPE = 0x2 << 28;
    public static final int FOOTER_VIEW_TYPE = 0x3 << 28;

    private List<View> headerViewList = new LinkedList<>();
    private List<View> footerViewList = new LinkedList<>();

    public int getOtherViewCount() {
        return headerViewList.size() + footerViewList.size();
    }

    public int headerViewCount() {
        return headerViewList.size();
    }

    public boolean isHeaderView(View view) {
        return headerViewList.contains(view);
    }

    public boolean removeHeaderView(View view) {
        if(!isHeaderView(view))
            return false;

        headerViewList.remove(view);
        return true;
    }

    public boolean addHeaderView(View view) {
        if(isHeaderView(view))
            return false;

        headerViewList.add(view);
        return true;
    }

    //======= Footer =======

    public int footerViewCount() {
        return footerViewList.size();
    }

    public boolean isFooterView(View view) {
        return footerViewList.contains(view);
    }

    public boolean removeFooterView(View view) {
        if(!isFooterView(view))
            return false;

        footerViewList.remove(view);
        return true;
    }

    public boolean addFooterView(View view) {
        if(isFooterView(view))
            return false;

        footerViewList.add(view);
        return true;
    }

    //======= Public =======

    public int getContentIndex(int position) {
        return position - headerViewList.size();
    }

    public int getAdapterIndex(int contentIndex) {
        return contentIndex + headerViewList.size();
    }

    public boolean checkIndex(int position, int contentSize) {
        return position >= headerViewList.size() && position < headerViewList.size() + contentSize;
    }

    public <T> OtherViewHolder<T> getViewHolder(int mockType) {
        if(isOtherView(mockType)) {
            int type = getType(mockType);
            int index = getIndex(mockType);

            switch (type) {
                case HEADER_VIEW_TYPE:
                    return new OtherViewHolder<>(headerViewList.get(index));
                case FOOTER_VIEW_TYPE:
                    return new OtherViewHolder<>(footerViewList.get(index));
            }
        }
        return null;
    }

    public boolean isInHeader(int position) {
        return position < headerViewList.size();
    }

    public boolean isInFooter(int position, int contentSize) {
        return position >= headerViewList.size() + contentSize;
    }

    public int viewType(int position, int contentSize) {
        if(isInHeader(position))
            return getMockType(HEADER_VIEW_TYPE, position);
        else if(isInFooter(position, contentSize))
            return getMockType(FOOTER_VIEW_TYPE, position - headerViewCount() - contentSize);
        else
            return CONTENT_VIEW_TYPE;
    }

    public boolean isOtherView(int position, int contentSize) {
        return isInHeader(position) || isInFooter(position, contentSize);
    }

    public boolean isOtherView(int viewType) {
        int type = getType(viewType);
        return type == HEADER_VIEW_TYPE || type == FOOTER_VIEW_TYPE;
    }

    static int getMockType(int type, int index) {
        if(index > 0xfffffff)
            throw new IndexTooLargeException();

        return (type & VIEW_TYPE_MOCK) | (~VIEW_TYPE_MOCK & index);
    }

    static int getType(int mockType) {
        return mockType >> 28 << 28;
    }

    static int getIndex(int mockType) {
        return mockType & (~VIEW_TYPE_MOCK);
    }
}
