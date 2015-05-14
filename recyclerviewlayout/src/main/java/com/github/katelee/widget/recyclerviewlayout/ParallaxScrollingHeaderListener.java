package com.github.katelee.widget.recyclerviewlayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kate on 2015/5/14
 */
public abstract class ParallaxScrollingHeaderListener extends RecyclerView.OnScrollListener {
    protected abstract View getHeaderView();
    protected abstract float getParallaxScrollingVelocity();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (getHeaderView() == null) {
            return;
        }

        float transY = getHeaderView().getTranslationY();
        getHeaderView().setTranslationY(transY - dy * getParallaxScrollingVelocity());

//        float transX = getHeaderView().getTranslationX();
//        getHeaderView().setTranslationX(transX - dx * getParallaxScrollingVelocity());
    }
}
