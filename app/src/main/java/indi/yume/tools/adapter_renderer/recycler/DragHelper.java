package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

/**
 * Created by yume on 16-4-13.
 */
public class DragHelper extends BaseDragHelper {

    private boolean isLongPressDragEnabled = true;
    private boolean isItemViewSwipeEnabled = true;

    private int mDefaultSwipeDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private int mDefaultDragDirs = ItemTouchHelper.START | ItemTouchHelper.END;

    public DragHelper(RendererAdapter adapter) {
        super(adapter);
    }

    public DragHelper(RendererAdapter adapter, int dragDirs, int swipeDirs) {
        super(adapter);

        mDefaultSwipeDirs = swipeDirs;
        mDefaultDragDirs = dragDirs;
    }

    public static DragHelper bind(RecyclerView recyclerView, RendererAdapter adapter) {
        DragHelper callback = new DragHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return callback;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
    }

    public void setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        isItemViewSwipeEnabled = itemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDefaultSwipeDirs, mDefaultDragDirs);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }
}
