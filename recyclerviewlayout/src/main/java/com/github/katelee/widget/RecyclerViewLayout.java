package com.github.katelee.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.github.katelee.widget.recyclerviewlayout.AutoHidingHeaderListener;
import com.github.katelee.widget.recyclerviewlayout.CustomAdapter;
import com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView;
import com.github.katelee.widget.recyclerviewlayout.ParallaxScrollingHeaderListener;

/**
 * Created by Kate on 2015/5/7
 */
public class RecyclerViewLayout extends SwipeRefreshLayout {
    private FrameLayout mFrameLayout;
    private FrameLayout mParallaxScrollingLayout;
    private final CustomRecyclerView mRecyclerView;
    private View mAutoHidingHeader;
    private View mParallaxScrollingHeader;
    private AutoHidingHeaderListener mAutoHidingHeaderListener;
    private ParallaxScrollingHeaderListener mParallaxScrollingListener;
    private float mParallaxScrollingVelocity = 1;

    public RecyclerViewLayout(Context context) {
        this(context, null);
    }

    public RecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFrameLayout = new FrameLayout(context);
        mFrameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mFrameLayout);

        mRecyclerView = new CustomRecyclerView(context);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(mRecyclerView);

        mParallaxScrollingLayout = new FrameLayout(context);
        mParallaxScrollingLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mAutoHidingHeader != null) {
                    mAutoHidingHeaderListener.onScrollStateChanged(recyclerView, newState);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingListener.onScrollStateChanged(recyclerView, newState);
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
            }
        });
        mRecyclerView.addOnChangeLayoutManagerListener(new CustomRecyclerView.OnChangeLayoutManagerListener() {
            @Override
            public void onChange(RecyclerView.LayoutManager layout) {

                if (layout instanceof GridLayoutManager) {
                    final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layout);
                    ((GridLayoutManager) layout).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return mRecyclerView.getAdapter().isFullSpan(position) ?
                                    gridLayoutManager.getSpanCount() : 1;
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
        });
    }

    @Override
    public boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(mRecyclerView, -1);// &&
//                !(mRecyclerView.getChildAt(0) != null
//                        && mRecyclerView.getChildAdapterPosition(mRecyclerView.getChildAt(0)) == 0
//                        && mRecyclerView.getChildAt(0).getScrollY() == 0);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link
     * com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setOnScrollListener(
     * RecyclerView.OnScrollListener)} instead
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.setOnScrollListener(listener);
    }

    public CustomRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setAdapter(CustomAdapter)} instead
     */
    public void setAdapter(Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link
     * com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setLayoutManager(RecyclerView.LayoutManager)}
     * instead
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }
     
    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#getLayoutManager()} instead
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * not recommend to Use {@link #setParallaxScrollingHeaderView} at the same time.
     * @param view
     */
    public void setAutoHidingHeaderView(View view) {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }
        mFrameLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mAutoHidingHeader = view;
        mRecyclerView.getAdapter().setAutoHidingHeaderView(view);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(0);
            }
        }, 100);
    }

    public void removeAutoHidingHeaderView() {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }

        mAutoHidingHeader = null;
        mRecyclerView.getAdapter().setAutoHidingHeaderView(null);
        mRecyclerView.getAdapter().notifyHeaderViewChanged();
    }

    public View getAutoHidingHeaderView() {
        return mAutoHidingHeader;
    }

    /**
     * Not recommend to Use {@link #setAutoHidingHeaderView} at the same time.
     * @param view The ParallaxScrollingHeaderView
     */
    public void setParallaxScrollingHeaderView(View view) {
        if (mParallaxScrollingHeader != null) {
            mParallaxScrollingLayout.removeView(mParallaxScrollingHeader);
        }
        mParallaxScrollingLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mParallaxScrollingHeader = view;
        mRecyclerView.getAdapter().setParallaxScrollingHeaderView(view);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(0);
            }
        }, 100);
    }

    public void removeParallaxScrollingHeaderView() {
        if (mParallaxScrollingHeader != null) {
            mFrameLayout.removeView(mParallaxScrollingHeader);
        }

        mParallaxScrollingHeader = null;
        mRecyclerView.getAdapter().setParallaxScrollingHeaderView(null);
        mRecyclerView.getAdapter().notifyHeaderViewChanged();
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

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setClipChildren(boolean)} instead
     */
    public void setRecyclerViewCilpChildren(boolean clipChildren) {
        mRecyclerView.setClipChildren(clipChildren);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setClipToPadding(boolean)} instead
     */
    public void setRecyclerViewClipToPadding(boolean clipToPadding) {
        mRecyclerView.setClipToPadding(clipToPadding);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setPadding(int, int, int, int)} instead
     */
    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link
     * com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#setItemAnimator(RecyclerView.ItemAnimator)}
     * instead
     */
    public void setRecyclerViewItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link
     * com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#addItemDecoration(RecyclerView.ItemDecoration)}
     * instead
     */
    public void setRecyclerViewItemAnimator(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    /**
     * @deprecated Use {@link #getRecyclerView()}
     * {@link
     * com.github.katelee.widget.recyclerviewlayout.CustomRecyclerView#addOnItemTouchListener(
     * RecyclerView.OnItemTouchListener)} instead
     */
    public void setRecyclerViewItemAnimator(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends CustomAdapter<VH> {}
}