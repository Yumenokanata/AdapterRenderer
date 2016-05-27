package indi.yume.tools.adapter_renderer.recycler;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by yume on 16-4-15.
 */
public abstract class BaseDragHelper extends ItemTouchHelper.Callback {
    protected RendererAdapter adapter;

    private boolean hasMoved = false;

    public BaseDragHelper(RendererAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int viewHolderPosition = viewHolder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();

        if(adapter.checkIndex(viewHolderPosition) && adapter.checkIndex(targetPosition)) {
            adapter.swap(adapter.getReallyIndex(viewHolderPosition),
                    adapter.getReallyIndex(targetPosition));
            hasMoved = true;
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.remove(adapter.getReallyIndex(viewHolder.getAdapterPosition()));
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder instanceof RendererViewHolder) {
            RendererViewHolder rendererViewHolder = (RendererViewHolder) viewHolder;
            if(rendererViewHolder.getRenderer().onSelectedChanged())
                return;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof RendererViewHolder) {
            RendererViewHolder rendererViewHolder = (RendererViewHolder) viewHolder;
            if(rendererViewHolder.getRenderer().onClearView())
                return;
        }
        if(hasMoved) {
            adapter.notifyDataSetChanged();
            hasMoved = false;
        }
        super.clearView(recyclerView, viewHolder);
    }
}
