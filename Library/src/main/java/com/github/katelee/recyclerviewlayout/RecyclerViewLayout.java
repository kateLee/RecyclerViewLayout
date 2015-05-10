package com.github.katelee.recyclerviewlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Kate on 2015/5/7
 */
public class RecyclerViewLayout extends SwipeRefreshLayout {
    private FrameLayout mFrameLayout;
    private RecyclerView mRecyclerView;
    private boolean mHeaderAdjust = false;
    private View mAdjustHeader;
    private RelativeLayout mHeader;
    private View mFooter;
    private View mLoaderMore;
    private Adapter<? extends RecyclerView.ViewHolder> mAdvanceAdapter;
    private RecyclerViewWithHeaderListener mHeaderListener;
    private RecyclerView.OnScrollListener mScrollListener;

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

        mHeader = new RelativeLayout(context);
        mHeader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        initialize();
    }

    private void initialize() {
        mHeaderListener = new RecyclerViewWithHeaderListener(mRecyclerView) {
            @Override
            View getHeaderView() {
                return mAdjustHeader;
            }
        };
//        mRecyclerView.setAdapter(mAdvanceAdapter = new AdvanceAdapter<RecyclerView.ViewHolder>() {
//            @Override
//            protected RecyclerView.ViewHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
//                if (mAdapter != null) {
//                    return mAdapter.onCreateViewHolder(viewGroup, viewType);
//                }
//                return null;
//            }
//
//            @Override
//            protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {
//                super.onHeaderBindViewHolder(viewHolder);
//
//                if (mAdjustHeader != null) {
//                    viewHolder.itemView.setPadding(0, mAdjustHeader.getHeight(), 0, 0);
//                }
//                else {
//                    viewHolder.itemView.setPadding(0, 0, 0, 0);
//                }
//            }
//
//            @Override
//            protected void onAdapterBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//                if (mAdapter != null) {
//                    mAdapter.onBindViewHolder(viewHolder, position);
//                }
//            }
//
//            @Override
//            protected int getAdapterItemViewType(int position) {
//                if (mAdapter != null) {
//                    return mAdapter.getItemViewType(position);
//                }
//                return 0;
//            }
//
//            @Override
//            public int getAdapterItemCount() {
//                return mAdapter.getItemCount();
//            }
//        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mHeaderAdjust) {
                    mHeaderListener.onScrollStateChanged(recyclerView, newState);
                }
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mHeaderAdjust) {
                    mHeaderListener.onScrolled(recyclerView, dx, dy);
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
        mAdvanceAdapter  = adapter;
        mRecyclerView.setAdapter(mAdvanceAdapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);

        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layout);
            ((GridLayoutManager) layout).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdvanceAdapter.isFullSpan(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public void setAdjustHeaderView(View view) {
        mHeaderAdjust = true;
        if (mAdjustHeader != null) {
            mFrameLayout.removeView(mAdjustHeader);
        }
        mFrameLayout.addView(view);

        mAdjustHeader = view;
        mAdvanceAdapter.setAdjustHeaderView(mAdjustHeader);
        mAdvanceAdapter.notifyHeaderViewChanged();
    }

    public void removeAdjustHeaderView() {
        mHeaderAdjust = false;
        if (mAdjustHeader != null) {
            mFrameLayout.removeView(mAdjustHeader);
        }

        mAdjustHeader = null;
        mAdvanceAdapter.setAdjustHeaderView(mAdjustHeader);
        mAdvanceAdapter.notifyHeaderViewChanged();
    }

    public void setHeaderView(View view) {
        mHeader.removeAllViews();
        mAdvanceAdapter.setHeaderView(mHeader);
    }

//    /**
//     * enable header animate with scroll
//     */
//    public void enableHeaderAdjustWithScroll() {
//        mHeaderAdjust = true;
//    }
//
//    /**
//     * disable header animate with scroll
//     */
//    public void disableHeaderAdjustWithScroll() {
//        mHeaderAdjust = false;
//    }

//    public void setFooterView(View view) {
//        mFooter = view;
//        mAdvanceAdapter.notifyFooterViewChanged();
//    }
//
//    public void removeFooterView() {
//        mFooter = null;
//        mAdvanceAdapter.notifyFooterViewChanged();
//    }
//
//    public void setLoaderMoreView(View view) {
//        mLoaderMore = view;
//        mAdvanceAdapter.notifyLoadMoreViewChanged();
//    }
//
    public void enableLoadMore() {
        mAdvanceAdapter.enableLoadMore();
    }

    public void disableLoadMore() {
        mAdvanceAdapter.disableLoadMore();
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends AdvanceAdapter<VH> {

        private View adjustHeaderView;

        @Override
        protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {
            super.onHeaderBindViewHolder(viewHolder);

            if (adjustHeaderView != null) {
                viewHolder.itemView.setPadding(0, adjustHeaderView.getHeight(), 0, 0);
            }
            else {
                viewHolder.itemView.setPadding(0, 0, 0, 0);
            }
        }

        private void setAdjustHeaderView(View view) {

            this.adjustHeaderView = view;
        }
    }
}
