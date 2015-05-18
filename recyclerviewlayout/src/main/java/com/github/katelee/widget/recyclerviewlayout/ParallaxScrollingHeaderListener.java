package com.github.katelee.widget.recyclerviewlayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.github.katelee.widget.recyclerviewlayout.Utility.findVisiblePosition;

/**
 * Created by Kate on 2015/5/14
 */
public abstract class ParallaxScrollingHeaderListener extends RecyclerView.OnScrollListener {
    protected abstract View getSceneryView();
    protected abstract View getWindowView();
    protected abstract float getVelocity();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (getSceneryView() == null) {
            return;
        }

        if (findVisiblePosition(recyclerView) == 0) {
            int top  = recyclerView.getChildAt(0).getTop();
            if (getWindowView() != null) {
                getWindowView().setTranslationY(top);

                getSceneryView().setTranslationY(-top * (1 - getVelocity()));
            }
            else {
                getSceneryView().setTranslationY(top * getVelocity());
            }
        }
        else {
            if (getWindowView() != null) {
                getWindowView().setTranslationY(-getWindowView().getHeight());
            }
            getSceneryView().setTranslationY(-getSceneryView().getHeight());
        }
    }
}
