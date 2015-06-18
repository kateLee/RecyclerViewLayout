package com.github.katelee.widget.recyclerviewlayout;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Kate on 2015/6/1
 */
public abstract class CustomAdapter<VH extends RecyclerView.ViewHolder> extends AdvanceAdapter<VH> {

    private View autoHidingHeaderView;
    private View parallaxScrollingHeaderView;

    @Override
    protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        super.onHeaderBindViewHolder(viewHolder);

        int holdHeight0 = autoHidingHeaderView == null ? 0 : autoHidingHeaderView.getHeight();
        int holdHeight1 = parallaxScrollingHeaderView == null ? 0 : parallaxScrollingHeaderView.getHeight();

        if (viewHolder instanceof AdvanceHolder) {
            ((AdvanceHolder) viewHolder).setHoldHeight(Math.max(holdHeight0, holdHeight1));
        }
    }

    public void setAutoHidingHeaderView(View view) {
        this.autoHidingHeaderView = view;

        if (autoHidingHeaderView.getHeight() == 0) {
            autoHidingHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnItemGlobalLayoutListener(autoHidingHeaderView) {
                        @Override
                        @TargetApi(16)
                        public void onGlobalLayout(View view) {
                            // make sure it is not called anymore
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            notifyHeaderViewChanged();
                        }
                    });
        }
        else {
            notifyHeaderViewChanged();
        }
    }

    public void setParallaxScrollingHeaderView(View view) {
        this.parallaxScrollingHeaderView = view;

        if (parallaxScrollingHeaderView.getHeight() == 0) {
            parallaxScrollingHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnItemGlobalLayoutListener(parallaxScrollingHeaderView) {
                        @Override
                        @TargetApi(16)
                        public void onGlobalLayout(View view) {
                            // make sure it is not called anymore
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            else {
                                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            notifyHeaderViewChanged();
                        }
                    });
        }
        else {
            notifyHeaderViewChanged();
        }
    }

    private abstract class OnItemGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private View view;

        protected OnItemGlobalLayoutListener(View view) {
            this.view = view;
        }

        abstract public void onGlobalLayout(View view);

        @Override
        public void onGlobalLayout() {
            onGlobalLayout(view);
        }
    }
}
