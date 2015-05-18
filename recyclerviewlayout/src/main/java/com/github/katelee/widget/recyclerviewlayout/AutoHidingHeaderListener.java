package com.github.katelee.widget.recyclerviewlayout;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.github.katelee.widget.recyclerviewlayout.Utility.findVisiblePosition;

/**
 * Created by Kate on 2015/5/5
 */
public abstract class AutoHidingHeaderListener extends RecyclerView.OnScrollListener {
    private boolean slideUp = false;
    private final long frame_slide_time = 250;
    private Handler mHandler = new Handler();
    private final OnStopRunnable mOnStopRunnable;

    public AutoHidingHeaderListener(RecyclerView recyclerView) {
        mOnStopRunnable = new OnStopRunnable(recyclerView);
    }

    protected abstract View getHeaderView();

    private void hideAnimate(int size) {
        if (getHeaderView() == null) {
            return;
        }

        getHeaderView().animate().translationY(-size).setDuration(frame_slide_time);
    }

    private void showAnimate() {
        if (getHeaderView() == null) {
            return;
        }

        getHeaderView().animate().translationY(0).setDuration(frame_slide_time);
    }

    private void adjustState(RecyclerView recyclerView) {
        mHandler.removeCallbacks(mOnStopRunnable);

        if (getHeaderView() == null) {
            return;
        }

        int height = getHeaderView().getHeight();
        float chY = getHeaderView().getTranslationY();
        if (chY == 0 || chY == -height) {
            return;
        }

        boolean isShow;
        if (slideUp) {
            isShow = false;
            int top  = recyclerView.getChildAt(0).getTop();
            if (findVisiblePosition(recyclerView) == 0) {
                if (top != 0) {
                    recyclerView.smoothScrollBy(0, height + top);
                }
                else {
                    isShow = true;
                }
            }
        } else {
            isShow = true;
        }

        if (isShow) {
            showAnimate();
        }
        else {
            hideAnimate(height);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mHandler.removeCallbacks(mOnStopRunnable);

        if (getHeaderView() == null) {
            return;
        }

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
}
