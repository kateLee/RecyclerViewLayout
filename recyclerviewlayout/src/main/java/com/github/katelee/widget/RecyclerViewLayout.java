package com.github.katelee.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.github.katelee.widget.recyclerviewlayout.AdvanceAdapter;
import com.github.katelee.widget.recyclerviewlayout.AutoHidingHeaderListener;
import com.github.katelee.widget.recyclerviewlayout.ParallaxScrollingHeaderListener;

/**
 * Created by Kate on 2015/5/7
 */
public class RecyclerViewLayout extends SwipeRefreshLayout {
    private FrameLayout mFrameLayout;
    private FrameLayout mParallaxScrollingLayout;
    private final RecyclerView mRecyclerView;
    private View mAutoHidingHeader;
    private View mParallaxScrollingHeader;
    private Adapter<? extends RecyclerView.ViewHolder> mAdapter;
    private AutoHidingHeaderListener mAutoHidingHeaderListener;
    private ParallaxScrollingHeaderListener mParallaxScrollingListener;
    private RecyclerView.OnScrollListener mScrollListener;
    private float mParallaxScrollingVelocity = 1;

    public RecyclerViewLayout(Context context) {
        this(context, null);
    }

    public RecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFrameLayout = new FrameLayout(context);
        mFrameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mFrameLayout);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(mRecyclerView);

        mParallaxScrollingLayout = new FrameLayout(context);
        mParallaxScrollingLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mFrameLayout.addView(mParallaxScrollingLayout);

        initialize();
    }

    private void initialize() {
        mAutoHidingHeaderListener = new AutoHidingHeaderListener(mRecyclerView) {
            @Override
            public View getHeaderView() {
                return mAutoHidingHeader;
            }
        };
        mParallaxScrollingListener = new ParallaxScrollingHeaderListener() {
            @Override
            public View getSceneryView() {
                return mParallaxScrollingHeader;
            }

            @Override
            public View getWindowView() {
                return mParallaxScrollingLayout;
            }

            @Override
            public float getVelocity() {
                return mParallaxScrollingVelocity;
            }
        };
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mAutoHidingHeader != null) {
                    mAutoHidingHeaderListener.onScrollStateChanged(recyclerView, newState);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingListener.onScrollStateChanged(recyclerView, newState);
                }
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mAutoHidingHeader != null) {
                    mAutoHidingHeaderListener.onScrolled(recyclerView, dx, dy);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingListener.onScrolled(recyclerView, dx, dy);
                }
                if (mScrollListener != null) {
                    mScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    @Override
    public boolean canChildScrollUp() {

        if (android.os.Build.VERSION.SDK_INT < 14) {
            return mRecyclerView.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(mRecyclerView, -1);
        }
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mScrollListener = listener;
    }

    public void setAdapter(Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);

        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layout);
            ((GridLayoutManager) layout).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdapter.isFullSpan(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
        if (mParallaxScrollingLayout != null) {
            mParallaxScrollingLayout.setTranslationY(0);
        }
        if (mParallaxScrollingHeader != null) {
            mParallaxScrollingHeader.setTranslationY(0);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * not recommend to use {@link #setParallaxScrollingHeaderView} at the same time.
     * @param view
     */
    public void setAutoHidingHeaderView(View view) {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }
        mFrameLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mAutoHidingHeader = view;
        mAdapter.setAutoHidingHeaderView(view);
    }

    public void removeAutoHidingHeaderView() {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }

        mAutoHidingHeader = null;
        mAdapter.setAutoHidingHeaderView(null);
        mAdapter.notifyHeaderViewChanged();
    }

    public View getAutoHidingHeaderView() {
        return mAutoHidingHeader;
    }

    /**
     * not recommend to use {@link #setAutoHidingHeaderView} at the same time.
     * @param view
     */
    public void setParallaxScrollingHeaderView(View view) {
        if (mParallaxScrollingHeader != null) {
            mParallaxScrollingLayout.removeView(mParallaxScrollingHeader);
        }
        mParallaxScrollingLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mParallaxScrollingHeader = view;
        mAdapter.setParallaxScrollingHeaderView(view);
    }

    public void removeParallaxScrollingHeaderView() {
        if (mParallaxScrollingHeader != null) {
            mFrameLayout.removeView(mParallaxScrollingHeader);
        }

        mParallaxScrollingHeader = null;
        mAdapter.setParallaxScrollingHeaderView(null);
        mAdapter.notifyHeaderViewChanged();
    }

    public View getParallaxScrollingHeaderView() {
        return mParallaxScrollingHeader;
    }

    /**
     * set header velocity relative proportion with scroll
     * @param velocity recommend 0 to 1
     */
    public void setParallaxScrollingVelocity(float velocity) {
        mParallaxScrollingVelocity = velocity;
    }

    public void setRecyclerViewCilpChildren(boolean clipChildren) {
        mRecyclerView.setClipChildren(clipChildren);
    }

    public void setRecyclerViewClipToPadding(boolean clipToPadding) {
        mRecyclerView.setClipToPadding(clipToPadding);
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
    }

    public void setRecyclerViewItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void setRecyclerViewItemAnimator(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    public void setRecyclerViewItemAnimator(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends AdvanceAdapter<VH> {

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

        private void setAutoHidingHeaderView(View view) {
            this.autoHidingHeaderView = view;

            autoHidingHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnItemGlobalLayoutListener(autoHidingHeaderView) {
                        @Override
                        public void onGlobalLayout(View view) {
                            // make sure it is not called anymore
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            notifyHeaderViewChanged();
                        }
                    });
        }

        private void setParallaxScrollingHeaderView(View view) {
            this.parallaxScrollingHeaderView = view;

            parallaxScrollingHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnItemGlobalLayoutListener(parallaxScrollingHeaderView) {
                        @Override
                        public void onGlobalLayout(View view) {
                            // make sure it is not called anymore
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            notifyHeaderViewChanged();
                        }
                    });
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
}