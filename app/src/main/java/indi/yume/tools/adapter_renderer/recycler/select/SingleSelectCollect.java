package indi.yume.tools.adapter_renderer.recycler.select;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yume on 16-5-6.
 */
public class SingleSelectCollect implements SelectCollect {
    private static final int SELECT_NONE = -1;

    private int selectItemIndex = SELECT_NONE;
    private int maxIndex = 0;

    @Override
    public void changeSize(int newSize) {
        if(selectItemIndex >= newSize)
            selectItemIndex = SELECT_NONE;
        maxIndex = newSize - 1;
    }

    @Override
    public void select(int position) {
        selectItemIndex = position;
    }

    @Override
    public void select(Iterable<Integer> selectIndex) {
        for (Integer index : selectIndex)
            selectItemIndex = index;
    }

    @Override
    public void selectAll() {
        selectItemIndex = maxIndex;
    }

    @Override
    public void deselect(int position) {
        if(selectItemIndex == position)
            selectItemIndex = SELECT_NONE;
    }

    @Override
    public void deselect(Iterable<Integer> deselectIndex) {
        for (Integer index : deselectIndex)
            if(index == selectItemIndex) {
                selectItemIndex = SELECT_NONE;
                break;
            }
    }

    @Override
    public void deselectAll() {
        selectItemIndex = SELECT_NONE;
    }

    @Override
    public void toggleSelection(int position) {
        if(selectItemIndex == position)
            selectItemIndex = SELECT_NONE;
        else
            selectItemIndex = position;
    }

    @Override
    public Set<Integer> getSelections() {
        if(selectItemIndex != SELECT_NONE) {
            Set<Integer> selectSet = new HashSet<>();
            selectSet.add(selectItemIndex);
            return selectSet;
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isSelect(int position) {
        return position == selectItemIndex;
    }

    @Override
    public void swap(int from, int to) {
        if(selectItemIndex == from)
            selectItemIndex = to;
        else if(selectItemIndex == to)
            selectItemIndex = from;
    }

    @Override
    public void removeItem(int position) {
        if(selectItemIndex < position)
            return;
        else if(selectItemIndex == position)
            selectItemIndex = SELECT_NONE;
        else if(selectItemIndex > position)
            selectItemIndex--;
    }

    @Override
    public void insertItem(int position) {
        if(selectItemIndex >= position)
            selectItemIndex++;
    }
}
