package com.github.katelee.recyclerviewlayout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = onHeaderCreateView(viewGroup);
                return new AdvanceHolder(view != null ? view : getDefaultView(viewGroup));

            case TYPE_FOOTER:
                view = onFooterCreateView(viewGroup);
                return new AdvanceHolder(view != null ? view : getDefaultView(viewGroup));

            case TYPE_LOAD_MORE:
                view = onLoadMoreCreateView(viewGroup);
                return new AdvanceHolder(view != null ? view : getDefaultView(viewGroup));

            default:
                return onAdapterCreateViewHolder(viewGroup, viewType);
        }
    }

    protected View onLoadMoreCreateView(ViewGroup viewGroup) {
        return null;
    }

    protected View onFooterCreateView(ViewGroup viewGroup) {
        return null;
    }

    protected View onHeaderCreateView(ViewGroup viewGroup) {
        return null;
    }

    private ViewGroup getDefaultView(View v) {
        ViewGroup viewGroup = new RelativeLayout(v.getContext());
        viewGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        View view = new View(v.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        viewGroup.addView(view);

        return viewGroup;
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
                onLoadMoreBindViewHolder(viewHolder);
                break;

            default:
                onAdapterBindViewHolder((VH) viewHolder, position - getHeaderCount());
                break;
        }

        if (isFullSpan(position)) {
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

    protected void onLoadMoreBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams vlp = viewHolder.itemView.getLayoutParams();
        vlp.height = enableLoadMore ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        viewHolder.itemView.setLayoutParams(vlp);

        if (enableLoadMore && mOnLoadMoreListener != null && !isLoadingMore) {
            mOnLoadMoreListener.loadMore();
            isLoadingMore = true;
        }
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
        enableLoadMore = false;
        notifyLoadMoreViewChanged();
    }

    public void enableLoadMore() {
        enableLoadMore = true;
        notifyLoadMoreViewChanged();
    }

    public boolean isFullSpan(int position) {
        return position == getHeaderPosition() || position == getFooterPosition() || position == getLoadMorePosition();
    }

    public void notifyAdapterItemRangeInserted(int position, int size) {
        notifyItemRangeInserted(getHeaderCount() + position, size);
    }

    public class AdvanceHolder extends RecyclerView.ViewHolder {

        public AdvanceHolder(View itemView) {
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
        notifyLoadMoreViewChanged();
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
}
