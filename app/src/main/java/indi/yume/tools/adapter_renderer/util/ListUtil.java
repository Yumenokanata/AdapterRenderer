package indi.yume.tools.adapter_renderer.util;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yume on 16-5-6.
 */
public class ListUtil {
    public static <T> List<T> newSizeList(int newSize) {
        return newSizeList(null, newSize);
    }

    public static <T> List<T> newSizeList(@Nullable List<T> list, int newSize) {
        List<T> newList = new ArrayList<>(newSize);

        if(list == null || list.isEmpty()) {
            for(int i = 0; i < newSize; i++)
                newList.add(null);
        } else if(list.size() < newSize){
            newList.addAll(list);

            int oldSize = list.size();
            for(int i = oldSize; i < newSize; i++)
                newList.add(null);
        } else {
            newList.addAll(list.subList(0, newSize));
        }

        return newList;
    }
}
