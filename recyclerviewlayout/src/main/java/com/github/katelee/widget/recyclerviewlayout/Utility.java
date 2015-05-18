package com.github.katelee.widget.recyclerviewlayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Kate on 2015/5/18
 */
public class Utility {

    public static int findVisiblePosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            int firstVisibleItem = firstVisibleItems[0];
            for (int tmp : firstVisibleItems) {
                if (firstVisibleItem > tmp) {
                    firstVisibleItem = tmp;
                }
            }
            return firstVisibleItem;
        }
        throw new IllegalArgumentException("only support LinearLayoutManager and StaggeredGridLayoutManager");
    }
}
