package indi.yume.tools.adapter_renderer.recycler.select;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/**
 * Created by yume on 16-5-6.
 */
public interface SelectCollect {

    void changeSize(int newSize);

    void select(int position);
    void select(Iterable<Integer> selectIndex);
    void selectAll();
    void deselect(int position);
    void deselect(Iterable<Integer> deselectIndex);
    void deselectAll();
    void toggleSelection(int position);

    Set<Integer> getSelections();
    boolean isSelect(int position);

    void swap(int from, int to);
    void removeItem(int position);
    void insertItem(int position);
}
