package indi.yume.tools.adapter_renderer.recycler.select;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import indi.yume.tools.adapter_renderer.util.ListUtil;

/**
 * Created by yume on 16-5-6.
 */
public class MultipleSelectCollect implements SelectCollect {
    private List<Boolean> selectList;

    @Override
    public void changeSize(int newSize) {
        selectList = ListUtil.newSizeList(selectList, newSize);
    }

    @Override
    public void select(int position) {
        selectList.set(position, true);
    }

    @Override
    public void select(Iterable<Integer> selectIndex) {
        for(Integer index : selectIndex)
            selectList.set(index, true);
    }

    @Override
    public void selectAll() {
        Collections.fill(selectList, true);
    }

    @Override
    public void deselect(int position) {
        selectList.set(position, false);
    }

    @Override
    public void deselect(Iterable<Integer> deselectIndex) {
        for(Integer index : deselectIndex)
            selectList.set(index, false);
    }

    @Override
    public void deselectAll() {
        Collections.fill(selectList, false);
    }

    @Override
    public void toggleSelection(int position) {
        selectList.set(position, !isSelect(position));
    }

    @Override
    public Set<Integer> getSelections() {
        Set<Integer> selectSet = new HashSet<>();
        int size = selectList.size();

        for(int i = 0; i < size; i++)
            if(isSelect(i))
                selectSet.add(i);

        return selectSet;
    }

    @Override
    public boolean isSelect(int position) {
        Boolean select = selectList.get(position);
        return select != null && select;
    }

    @Override
    public void swap(int from, int to) {
        Collections.swap(selectList, from, to);
    }

    @Override
    public void removeItem(int position) {
        selectList.remove(position);
    }

    @Override
    public void insertItem(int position) {
        selectList.add(position, false);
    }
}
