package com.github.katelee.recyclerviewlayout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kate on 2015/4/30
 */
abstract public class AdvanceAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = 11;
    protected static final int TYPE_FOOTER = 12;
    protected static final int TYPE_LOAD_MORE = 13;

    private OnLoadMoreListener mOnLoadMoreListener;
    private static boolean isLoadingMore = false;
    private static boolean enableLoadMore = false;
    private View mHeaderView;
    private View mFooterView;
    private View mLoadMoreView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                if (mHeaderView != null) {
                    return new HeaderHolder(mHeaderView);
                }
                return new HeaderHolder(new View(viewGroup.getContext()));

            case TYPE_FOOTER:
                if (mFooterView != null) {
                    return new FooterHolder(mFooterView);
                }
                return new FooterHolder(new View(viewGroup.getContext()));

            case TYPE_LOAD_MORE:
                if (mLoadMoreView != null) {
                    return new LoadMoreViewHolder(mLoadMoreView);
                }
                return new LoadMoreViewHolder(new View(viewGroup.getContext()));

            default:
                return onAdapterCreateViewHolder(viewGroup, viewType);
        }
    }

    protected abstract VH onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType);

    @SuppressWarnings("unchecked viewHolder type")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_HEADER:
                onHeaderBindViewHolder(viewHolder);
                break;

            case TYPE_FOOTER:
                onFooterBindViewHolder(viewHolder);
                break;

            case TYPE_LOAD_MORE:
                viewHolder.itemView.setVisibility(enableLoadMore ? View.VISIBLE : View.GONE);
                if (mOnLoadMoreListener != null && !isLoadingMore && mLoadMoreView != null) {
                    mOnLoadMoreListener.loadMore();
                    setLoadingMore(true);
                }
                break;

            default:
                onAdapterBindViewHolder((VH) viewHolder, position - getHeaderCount());
                break;
        }

        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
            if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }
        }
    }

    protected void onFooterBindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    protected abstract void onAdapterBindViewHolder(VH viewHolder, int position);

    @Override
    public int getItemViewType(int position) {
        if (position == getHeaderPosition()) {
            return TYPE_HEADER;
        }
        if (position == getFooterPosition()) {
            return TYPE_FOOTER;
        }
        if (position == getLoadMorePosition()) {
            return TYPE_LOAD_MORE;
        }
        int viewType = getAdapterItemViewType(position);
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER || viewType == TYPE_LOAD_MORE) {
            throw new IllegalArgumentException("These viewTypes is for special case!");
        }
        return viewType;
    }

    protected int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getAdapterItemCount() + getFooterCount();
    }

    abstract public int getAdapterItemCount();

    int getHeaderCount() {
        return 1;
    }

    int getFooterCount() {
        return 2;
    }

    public void disableLoadMore() {
        if (!enableLoadMore) {
            return;
        }
        enableLoadMore = false;
        notifyLoadMoreViewChanged();
    }

    public void enableLoadMore() {
        if (enableLoadMore) {
            return;
        }
        enableLoadMore = true;
        notifyLoadMoreViewChanged();
    }

    public boolean isFullSpan(int position) {
        return position == getHeaderPosition() || position == getFooterPosition() || position == getLoadMorePosition();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void notifyHeaderViewChanged() {
        notifyItemChanged(getHeaderPosition());
    }

    public void notifyFooterViewChanged() {
        notifyItemChanged(getFooterPosition());
    }

    public void notifyLoadMoreViewChanged() {
        notifyItemChanged(getLoadMorePosition());
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public void setLoadingMore(boolean flag) {
        isLoadingMore = flag;
    }

    int getHeaderPosition() {
        return 0;
    }

    int getFooterPosition() {
        return getHeaderCount() + getAdapterItemCount();
    }

    int getLoadMorePosition() {
        return getItemCount() - 1;
    }

    public void setHeaderView(View view) {
        mHeaderView = view;
        notifyHeaderViewChanged();
    }
    public void setFooterView(View view) {
        mFooterView = view;
        notifyFooterViewChanged();
    }
    public void setLoadMoreView(View view) {
        mLoadMoreView = view;
        notifyLoadMoreViewChanged();
    }
}
