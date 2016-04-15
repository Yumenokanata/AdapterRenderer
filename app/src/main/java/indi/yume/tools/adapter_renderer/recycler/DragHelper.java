package indi.yume.tools.adapter_renderer.recycler;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by yume on 16-4-13.
 */
public class DragHelper extends BaseDragHelper {
    public static final int TYPE_LINEAR = 0x11;
    public static final int TYPE_GRID = 0x22;

    @IntDef({TYPE_LINEAR, TYPE_GRID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DIRECT_TYPE{}

    private boolean isLongPressDragEnabled = true;
    private boolean isItemViewSwipeEnabled = true;

    private int mDefaultDragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private int mDefaultSwipeDirs = ItemTouchHelper.START | ItemTouchHelper.END;

    public DragHelper(RendererAdapter adapter) {
        super(adapter);
    }

    public DragHelper(RendererAdapter adapter, boolean enableLongPressDrag, boolean enableSwipeView) {
        super(adapter);

        isLongPressDragEnabled = enableLongPressDrag;
        isItemViewSwipeEnabled = enableSwipeView;
    }

    public DragHelper(RendererAdapter adapter, @DIRECT_TYPE int directType) {
        super(adapter);

        setDirectType(directType);
    }

    public DragHelper setDirectType(@DIRECT_TYPE int directType) {
        switch (directType) {
            case TYPE_LINEAR:
                mDefaultDragDirs =  ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                mDefaultSwipeDirs = 0;
                break;
            case TYPE_GRID:
                mDefaultDragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                mDefaultSwipeDirs = ItemTouchHelper.START | ItemTouchHelper.END;
                break;
        }

        return this;
    }

    public static DragHelper bind(RecyclerView recyclerView, RendererAdapter adapter) {
        return bind(recyclerView, adapter, true, true);
    }

    public static DragHelper bind(RecyclerView recyclerView, RendererAdapter adapter, boolean enableLongPressDrag, boolean enableSwipeView) {
        DragHelper callback = new DragHelper(adapter, enableLongPressDrag, enableSwipeView);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelper(itemTouchHelper);

        return callback;
    }

    public DragHelper setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
        return this;
    }

    public DragHelper setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        isItemViewSwipeEnabled = itemViewSwipeEnabled;
        return this;
    }

    public DragHelper setDragDirs(int dragDirs) {
        this.mDefaultDragDirs = dragDirs;
        return this;
    }

    public DragHelper setSwipeDirs(int swipeDirs) {
        this.mDefaultSwipeDirs = swipeDirs;
        return this;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDefaultDragDirs, mDefaultSwipeDirs);
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
