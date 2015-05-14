package com.github.katelee.widget.recyclerviewlayout;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Kate on 2015/5/5
 */
public abstract class AutoHidingHeaderListener extends RecyclerView.OnScrollListener {
    private boolean slideUp = false;
    private final long frame_slide_time = 250;
    private Handler mHandler = new Handler();
    private final OnStopRunnable mOnStopRunnable;
//    private boolean isVertical;

    public AutoHidingHeaderListener(RecyclerView recyclerView) {
        mOnStopRunnable = new OnStopRunnable(recyclerView);
    }

    protected abstract View getHeaderView();

    private void hideAnimate(int size) {
        if (getHeaderView() == null) {
            return;
        }

//        if (isVertical) {
            getHeaderView().animate().translationY(-size).setDuration(frame_slide_time);
//        }
//        else {
//            getHeaderView().animate().translationX(-size).setDuration(frame_slide_time);
//        }
    }

    private void showAnimate() {
        if (getHeaderView() == null) {
            return;
        }

//        if (isVertical) {
            getHeaderView().animate().translationY(0).setDuration(frame_slide_time);
//        }
//        else {
//            getHeaderView().animate().translationX(0).setDuration(frame_slide_time);
//        }
    }

    private void adjustState(RecyclerView recyclerView) {
        mHandler.removeCallbacks(mOnStopRunnable);

        if (getHeaderView() == null) {
            return;
        }

//        if (isVertical) {
            int height = getHeaderView().getHeight();
            float chY = getHeaderView().getTranslationY();
            if (chY == 0 || chY == -height) {
                return;
            }

            if (slideUp) {
                if (findVisiblePosition(recyclerView) == 0) {
                    recyclerView.smoothScrollBy(0, height + recyclerView.getChildAt(0).getTop());
                }
                hideAnimate(height);
            } else {
                showAnimate();
            }
//        }
//        else {
//            int width = getHeaderView().getWidth();
//            float chX = getHeaderView().getTranslationY();
//            if (chX == 0 || chX == -width) {
//                return;
//            }
//
//            if (slideUp) {
//                if (findVisiblePosition(recyclerView) == 0) {
//                    recyclerView.smoothScrollBy(width + recyclerView.getChildAt(0).getTop(), 0);
//                }
//                hideAnimate(width);
//            } else {
//                showAnimate();
//            }
//        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mHandler.removeCallbacks(mOnStopRunnable);

        if (getHeaderView() == null) {
            return;
        }

//        if (dy != 0) {
//            isVertical = true;
//        }
//        if (dx != 0) {
//            isVertical = false;
//        }

//        if (isVertical) {
            int height = getHeaderView().getHeight();
            float chY = getHeaderView().getTranslationY();
            float transY = 0;
            if (canScrollDown(recyclerView)) {
                slideUp = dy > 0; //finger move down to slide up view
                transY = chY - dy;
                transY = transY <= -height ? -height : transY;
                transY = transY >= 0 ? 0 : transY;
            }
            getHeaderView().setTranslationY(transY);
//        }
//        else {
//            int width = getHeaderView().getWidth();
//            float chX = getHeaderView().getTranslationX();
//            float transX = 0;
//            if (canScrollEnd(recyclerView)) {
//                slideUp = dx > 0; //finger move down to slide up view
//                transX = chX - dx;
//                transX = transX <= -width ? -width : transX;
//                transX = transX >= 0 ? 0 : transX;
//            }
//            getHeaderView().setTranslationX(transX);
//        }

        mHandler.postDelayed(mOnStopRunnable, 300);
    }

    private class OnStopRunnable implements Runnable {

        private RecyclerView recyclerView;

        private OnStopRunnable(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void run() {
            adjustState(recyclerView);
        }
    }

    /**
     * Check if can scroll down.
     */
    private boolean canScrollDown(RecyclerView recyclerView) {
        return recyclerView.canScrollVertically(1) || findVisiblePosition(recyclerView) != 0;
    }

    /**
     * Check if can scroll end.
     */
    private boolean canScrollEnd(RecyclerView recyclerView) {
        return recyclerView.canScrollHorizontally(1) || findVisiblePosition(recyclerView) != 0;
    }


    private int findVisiblePosition(RecyclerView recyclerView) {
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
