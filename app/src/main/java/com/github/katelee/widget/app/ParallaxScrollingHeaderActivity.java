package com.github.katelee.widget.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.katelee.widget.RecyclerViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 2015/5/8
 */
public class ParallaxScrollingHeaderActivity extends ActionBarActivity {
    RecyclerViewLayout recyclerViewLayout;

    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    GridLayoutManager gridLayoutManager;
    DataAdapter mAdapter;

    List<String> strings = new ArrayList<String>();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view_layout);

        recyclerViewLayout = (RecyclerViewLayout) findViewById(R.id.recyclerViewLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);

        for (int i = 0; i < 10; i++) {
            strings.add(String.valueOf(i));
        }

        recyclerViewLayout.getRecyclerView().setLayoutManager(staggeredGridLayoutManager);
        recyclerViewLayout.getRecyclerView().setAdapter(mAdapter = new DataAdapter());

        recyclerViewLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //simulate request, cause recyclerview is measuring size.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        strings.clear();

                        List<String> tmp = new ArrayList<String>();
                        for (int i = 0; i < 10; i++) {
                            tmp.add(String.valueOf(i));
                        }

                        strings.addAll(tmp);
                        mAdapter.notifyDataSetChanged();

                        recyclerViewLayout.setRefreshing(false);
                        mAdapter.enableLoadMore();
                    }
                }, 500);
            }
        });
        mAdapter.setOnLoadMoreListener(new DataAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //simulate request, cause recyclerview is measuring size.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> tmp = new ArrayList<String>();
                        for (int i = 10; i < 20; i++) {
                            tmp.add(String.valueOf(i));
                        }

                        strings.addAll(tmp);
                        mAdapter.notifyAdapterItemRangeInserted(mAdapter.getAdapterItemCount() - tmp.size(),
                                tmp.size());
                        mAdapter.setLoadingMore(false);

                        mAdapter.disableLoadMore();
                    }
                }, 500);
            }
        });
        mAdapter.enableLoadMore();

        recyclerViewLayout.setParallaxScrollingHeaderView(
                LayoutInflater.from(this).inflate(R.layout.view_parallax_scrolling_header, null));
        recyclerViewLayout.setParallaxScrollingVelocity(0.3f);
    }

    private class DataAdapter extends RecyclerViewLayout.Adapter<DataHolder> {
        @Override
        protected View onHeaderCreateView(ViewGroup viewGroup) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_auto_hiding_header, viewGroup,
                    false);
        }

        @Override
        protected View onLoadMoreCreateView(ViewGroup viewGroup) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_loadmore, viewGroup, false);
        }

        @Override
        protected View onFooterCreateView(ViewGroup viewGroup) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_footer, viewGroup, false);
        }

        @Override
        protected DataHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item, viewGroup, false);
            return new DataHolder(view);
        }

        @Override
        protected void onAdapterBindViewHolder(DataHolder viewHolder, int position) {
            viewHolder.label.setText("position: " + strings.get(position));
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.height = 150 + (position + 1) * 20;
            viewHolder.itemView.setLayoutParams(layoutParams);
        }

        @Override
        protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {
            super.onHeaderBindViewHolder(viewHolder);

            viewHolder.itemView.findViewById(R.id.linearlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(linearLayoutManager);
                }
            });
            viewHolder.itemView.findViewById(R.id.gridlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(gridLayoutManager);
                }
            });
            viewHolder.itemView.findViewById(R.id.staggeredgridlayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewLayout.setLayoutManager(staggeredGridLayoutManager);
                }
            });
        }

        @Override
        public int getAdapterItemCount() {
            return strings.size();
        }
    }

    private class DataHolder extends RecyclerView.ViewHolder {
        TextView label;

        public DataHolder(View itemView) {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
